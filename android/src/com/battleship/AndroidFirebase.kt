package com.battleship

import android.util.Log
import com.badlogic.gdx.Gdx
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.state.MainMenuState
import com.battleship.model.Game
import com.battleship.model.PendingGame
import com.battleship.model.Player
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.runBlocking

/**
 * Implements behavior from [FirebaseController]
 *
 * Class used to setup the database connection
 * declared as object to make it a singleton
 */
object AndroidFirebase : FirebaseController {
    private val db = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private var activeListener: ListenerRegistration? = null
        set(value) {
            field?.remove()
            field = value
        }

    init {
        runBlocking {
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Login", "signInAnonymously successful")
                    } else {
                        Log.w("Login", "signInAnonymously failed", task.exception)
                    }
                }
        }
    }

    /**
     * Creates a new game document in firebase
     * @param userId the id of the user setting up the game
     * @param userName the name of the user setting up the game
     * @param callback function invoked once the process has completed.
     */
    override fun createGame(userId: String, userName: String, callback: (game: Game?) -> Unit) {
        val data = mutableMapOf<String, Any>()
        data["player1Id"] = userId
        data["player1Name"] = userName
        data["player2Id"] = ""
        data["player2Name"] = ""
        data["winner"] = ""
        data["moves"] = mutableListOf<Map<String, Any>>()
        data["treasures"] = mutableMapOf<String, List<Map<String, Any>>>()
        data["playerLeft"] = ""

        db.collection("games").add(data)
            .addOnSuccessListener { documentReference ->
                val gameId = documentReference.id
                Log.d("createGame", "created game with id=$gameId")
                val game = Game(gameId)
                val player1 = Player(userId, userName)
                game.setPlayers(player1, Player())
                Gdx.app.postRunnable { callback(game) }
            }
            .addOnFailureListener { e ->
                val errorCode = (e as FirebaseFirestoreException).code
                val errorMessage = e.message
                Log.w("createGame", "Failed to create game! $errorCode - $errorMessage", e)
                Gdx.app.postRunnable {
                    callback(null)
                }
            }
    }

    /**
     * Method for joining an existing game.
     * @param gameId the id of the game document
     * @param userId the id of the player that should be added
     * @param userName the name of the player that should be added
     * @param callback function invoked once the process has completed.
     */
    override fun joinGame(
        gameId: String,
        userId: String,
        userName: String,
        callback: (game: Game?) -> Unit
    ) {
        val game = Game(gameId)
        val docRef = db.collection("games").document(gameId)
        var player1 = Player()
        var player2 = Player()
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            val player1Id: String = snapshot.getString("player1Id") as String
            val player1Name: String = snapshot.getString("player1Name") as String
            player1 = Player(player1Id, player1Name)

            transaction.update(docRef, "player2Id", userId)
            transaction.update(docRef, "player2Name", userName)
            player2 = Player(userId, userName)
        }.addOnSuccessListener {
            game.setPlayers(player1, player2)
            Log.d("joinGame", "Joined " + player1.playerName + "'s game successfully!")
            Gdx.app.postRunnable { callback(game) }
        }.addOnFailureListener { e ->
            val errorCode = (e as FirebaseFirestoreException).code
            val errorMessage = e.message
            Log.w("joinGame", "Transaction Joined game failed! $errorCode - $errorMessage: ", e)
            Gdx.app.postRunnable { callback(null) }
        }
    }

    /**
     * Method for leaving an existing game.
     * @param gameId the id of the game document.
     * @param playerId the id of the player that leaves.
     * @param callback function invoked once the process has completed.
     */
    override fun leaveGame(gameId: String, playerId: String, callback: () -> Unit) {
        val docRef = db.collection("games").document(gameId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            transaction.update(docRef, "playerLeft", playerId)
        }.addOnSuccessListener {
            Log.d("leaveGame", "Player left game successfully")
            Gdx.app.postRunnable { callback() }
        }.addOnFailureListener { e ->
            val errorCode = (e as FirebaseFirestoreException).code
            val errorMessage = e.message
            Log.w("leaveGame", "Player left game failed! $errorCode - $errorMessage: ", e)
            Gdx.app.postRunnable { callback() }
        }
    }

    /**
     * Method for registering a player's treasures
     * @param gameId the id of the game document
     * @param userId the id of the player
     * @param treasures list containing the treasures that should be added
     */
    override fun registerTreasures(gameId: String, userId: String, treasures: List<Map<String, Any>>) {
        val docRef = db.collection("games").document(gameId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            val dbTreasures =
                snapshot.get("treasures") as MutableMap<String, List<Map<String, Any>>>

            dbTreasures[userId] = treasures
            transaction.update(docRef, "treasures", dbTreasures)
        }.addOnSuccessListener {
            Log.d("registerTreasures", "Treasures registered successfully!")
        }.addOnFailureListener { e ->
            val errorCode = (e as FirebaseFirestoreException).code
            val errorMessage = e.message
            Log.w("registerTreasures", "Treasures registration failed! $errorCode - $errorMessage: ", e)
            Gdx.app.postRunnable {
                GSM.resetGame()
                GSM.set(MainMenuState(this))
            }
        }
    }

    /**
     * Method for registering a move in the game
     * @param gameId the id of the game document
     * @param x x coordinate of move
     * @param y y coordinate of move
     * @param playerId the id of the player making the move
     * @param equipment The name of the equipment used by the player
     */
    override fun registerMove(gameId: String, x: Int, y: Int, playerId: String, equipment: String) {
        val docRef = db.collection("games").document(gameId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            var moves = mutableListOf<Map<String, Any>>()
            if (snapshot.get("moves") != null) {
                moves = snapshot.get("moves") as MutableList<Map<String, Any>>
            }

            val data = mutableMapOf<String, Any>()
            data["x"] = x
            data["y"] = y
            data["playerId"] = playerId
            data["weapon"] = equipment
            moves.add(data)

            transaction.update(docRef, "moves", moves)
        }.addOnSuccessListener {
            Log.d("registerMove", "Move registered successfully!")
        }.addOnFailureListener { e ->
            val errorCode = (e as FirebaseFirestoreException).code
            val errorMessage = e.message
            Log.w("registerMove", "Move registration failed! $errorCode - $errorMessage: ", e)
            Gdx.app.postRunnable {
                GSM.resetGame()
                GSM.set(MainMenuState(this))
            }
        }
    }

    /**
     * Method for setting the winner of the game
     * @param gameId the id of the game document
     * @param userId the id of the player that won
     */
    override fun setWinner(userId: String, gameId: String) {
        db.collection("games").document(gameId).update("winner", userId)
            .addOnSuccessListener {
                Log.d("setWinner", "success")
            }
            .addOnFailureListener { e ->
                val errorCode = (e as FirebaseFirestoreException).code
                val errorMessage = e.message
                Log.w("setWinner", "Failed to set Winner! $errorCode - $errorMessage: ", e)
                GSM.resetGame()
                Gdx.app.postRunnable { GSM.set(MainMenuState(this)) }
            }
    }

    /**
     * Method adding listener to all games where player2 is empty.
     * @param callback function invoked once the process has completed.
     */
    override fun addPendingGamesListener(callback: (pendingGames: ArrayList<PendingGame>) -> Unit) {
        activeListener =
                db.collection("games").whereEqualTo("player2Name", "").whereEqualTo("player2Id", "")
                        .addSnapshotListener { documents, e ->
                            val pendingGames = ArrayList<PendingGame>()
                            if (e != null) {
                                Log.w("addPendingGamesListener", "Listen failed.", e)
                                return@addSnapshotListener
                            }
                            Log.d("addPendingGamesListener", "Found documents containing pending games")
                            for (doc in documents!!) {
                                val player1Id: String = doc.getString("player1Id") as String
                                if (player1Id == GSM.userId) continue
                                val player1Name: String = doc.getString("player1Name") as String
                                val gameId = doc.id
                                if (player1Id != "") {
                                    pendingGames.add(
                                            PendingGame(
                                                    gameId,
                                                    player1Id,
                                                    player1Name
                                            )
                                    )
                                }
                            }
                            Log.d("addPendingGamesListener", "Pending games: $pendingGames")
                            Gdx.app.postRunnable {
                                callback(pendingGames)
                            }
                        }
    }

    /**
     * Function adding listener to a specific game. Listening to when players joins and register treasures.
     * @param gameId the id of the game document
     * @param playerId the id of the player
     */
    override fun addGameListener(gameId: String, playerId: String) {
        val docRef = db.collection("games").document(gameId)
        activeListener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("addGameListener", "Listener failed", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d("addGameListener", "Game data: ${snapshot.data}")
                val opponent = snapshot.data?.get("player2Id")
                val playerLeft = snapshot.data?.get("playerLeft")

                if (opponent == "") {
                    Log.d("addGameListener", "Opponent not joined yet")
                } else if (playerLeft != "") {
                    Log.d("addGameListener", "Opponent left firebase")
                    GSM.activeGame!!.opponentLeft = true
                } else {
                    val player2Id = snapshot.data?.get("player2Id") as String
                    val player2Name = snapshot.data?.get("player2Name") as String
                    val activeGameOpponent = GSM.activeGame!!.opponent
                    if ((activeGameOpponent.playerId == "" || activeGameOpponent.playerName == "") && player2Id != "") {
                        Log.d("addGameListener", "Opponent joined and registered")

                        GSM.activeGame!!.setPlayers(GSM.activeGame!!.player, Player(player2Id, player2Name))
                        GSM.activeGame!!.setGameReadyIfReady()
                    } else {
                        val treasures: MutableMap<String, List<Map<String, Any>>>
                        if (snapshot.data?.get("treasures") != null) {
                            treasures =
                                snapshot.data?.get("treasures") as MutableMap<String, List<Map<String, Any>>>

                            if (treasures.size < 2 && GSM.activeGame!!.isPlayersRegistered()) {
                                Log.d("addGameListener", "opponent's treasures registered")
                                val OplayerId = GSM.activeGame!!.opponent.playerId
                                if (OplayerId in treasures) {
                                    treasures[OplayerId]?.let {
                                        GSM.activeGame!!.opponent.board.setTreasuresList(it)
                                    }
                                    GSM.activeGame!!.setGameReadyIfReady()
                                }
                            } else if (treasures.size == 2 && !GSM.activeGame!!.isTreasuresRegistered()) {
                                val oplayerId = GSM.activeGame!!.opponent.playerId
                                if (oplayerId in treasures) {
                                    treasures[oplayerId]?.let {
                                        GSM.activeGame!!.opponent.board.setTreasuresList(it)
                                    }
                                    GSM.activeGame!!.setGameReadyIfReady()
                                }
                            } else {
                                addPlayListener(gameId)
                            }
                        }
                    }
                }
            } else
                Log.d("addGameListener", "no data")
        }
    }

    /**
     * Function adding listener to a specific game. Listening to when players make moves.
     * @param gameId the id of the game document
     */
    override fun addPlayListener(gameId: String) {
        val docRef = db.collection("games").document(gameId)
        activeListener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("addPlayListener", "Listener failed", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d("addPlayListener", "Game data: ${snapshot.data}")

                var moves = mutableListOf<Map<String, Any>>()
                if (snapshot.data?.get("moves") != null) {
                    moves =
                        snapshot.data?.get("moves") as MutableList<Map<String, Any>>
                }

                val winner = snapshot.data?.get("winner")
                if (winner != "") {
                    Log.d("addPlayListener", "The winner is $winner")
                    GSM.activeGame!!.winner = winner as String
                }
                else {
                    if (moves.size == 0) {
                        Log.d("addPlayListener", "No moves made yet")
                        GSM.activeGame!!.setGameReadyIfReady()
                    } else {
                        val lastMove = moves.get(moves.size - 1)
                        val game = GSM.activeGame!!
                        if (lastMove["playerId"]!!.equals(game.opponent.playerId)) {
                            Log.d("addPlayListener", "----------------------OPPONENT LAST MOVE----------------------- " + lastMove)
                            GSM.activeGame!!.registerMove(lastMove)
                        } else if (lastMove["playerId"]!!.equals(game.player.playerId)) {
                            Log.d("addPlayListener", "----------------------PLAYER LAST MOVE----------------------- " + lastMove)
                        }
                    }
                }
            } else
                Log.d("addPlayListener", "no data")
        }
    }
}

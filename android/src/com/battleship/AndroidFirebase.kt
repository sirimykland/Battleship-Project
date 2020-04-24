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
        // Sign in as an anonymous user to authorize, don't know if runBlocking actually works
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
     * @inheritDoc
     */
    override fun createGame(userId: String, userName: String, callback: (game: Game?) -> Unit) {
        // Set up game data
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
     * @inheritDoc
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

            // Get player1
            val player1Id: String = snapshot.getString("player1Id") as String
            val player1Name: String = snapshot.getString("player1Name") as String
            player1 = Player(player1Id, player1Name)

            // Set player2
            transaction.update(docRef, "player2Id", userId)
            transaction.update(docRef, "player2Name", userName)
            player2 = Player(userId, userName)
        }.addOnSuccessListener {
            // Creates a new game and registers player1 and player2
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
     * @inheritDoc
     */
    override fun leaveGame(gameId: String, playerId: String, callback: () -> Unit) {
        val docRef = db.collection("games").document(gameId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            // val playerLeftId: String = snapshot.getString("player2Id") as String
            // val player2Id: String = snapshot.getString("player2Id") as String
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
     * @inheritDoc
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
     * @inheritDoc
     */
    override fun registerTreasures(gameId: String, userId: String, treasures: List<Map<String, Any>>) {
        val docRef = db.collection("games").document(gameId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            // Get treasures
            val dbTreasures =
                snapshot.get("treasures") as MutableMap<String, List<Map<String, Any>>>

            // Update treasures
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
     * @inheritDoc
     */
    override fun registerMove(gameId: String, x: Int, y: Int, playerId: String, equipment: String) {
        val docRef = db.collection("games").document(gameId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            // Get moves
            var moves = mutableListOf<Map<String, Any>>()
            if (snapshot.get("moves") != null) {
                moves = snapshot.get("moves") as MutableList<Map<String, Any>>
            }

            // add new move
            val data = mutableMapOf<String, Any>()
            data["x"] = x
            data["y"] = y
            data["playerId"] = playerId
            data["weapon"] = equipment
            moves.add(data)

            // Update move
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
     * @inheritDoc
     */
    override fun setWinner(gameId: String, userId: String) {
        db.collection("games").document(gameId).update("winner", userId)
            .addOnSuccessListener {
                Log.d("setWinner", "success")
            }
            .addOnFailureListener { e ->
                val errorCode = (e as FirebaseFirestoreException).code
                val errorMessage = e.message
                Log.w("setWinner", "Failed to set Winner! $errorCode - $errorMessage: ", e)
                GSM.resetGame()
                GSM.set(MainMenuState(this))
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
                // If no opponent has joined yet
                if (opponent == "") {
                    Log.d("addGameListener", "Opponent not joined yet")
                } else if (playerLeft != "") {
                    Log.d("addGameListener", "Opponent left firebase")
                    GSM.activeGame!!.opponentLeft = true
                }
                // If there is an opponent in the game
                else {
                    val player2Id = snapshot.data?.get("player2Id") as String
                    val player2Name = snapshot.data?.get("player2Name") as String
                    if (GSM.activeGame!!.opponent.playerId == "" && player2Id != "") {
                        Log.d("addGameListener", "Opponent joined and registered")

                        GSM.activeGame!!.opponent = Player(player2Id, player2Name)
                        GSM.activeGame!!.setGameReadyIfReady()
                    } else {
                        // Get the field containing the treasures in the database
                        val treasures: MutableMap<String, List<Map<String, Any>>>
                        if (snapshot.data?.get("treasures") != null) {
                            treasures =
                                snapshot.data?.get("treasures") as MutableMap<String, List<Map<String, Any>>>

                            // If there is not enough treasures registered
                            if (treasures.size < 2 && GSM.activeGame!!.isPlayersRegistered()) {
                                Log.d("addGameListener", "opponent's treasures registered")
                                val OplayerId = GSM.activeGame!!.opponent.playerId
                                if (OplayerId in treasures) {
                                    treasures[OplayerId]?.let {
                                        GSM.activeGame!!.opponent.board.setTreasuresList(it)
                                    }
                                    GSM.activeGame!!.setGameReadyIfReady()
                                }
                            }
                            // If there is enough treasures registered in firebase but not in opponents board
                            else if (treasures.size == 2 && !GSM.activeGame!!.isTreasuresRegistered()) {
                                val OplayerId = GSM.activeGame!!.opponent.playerId
                                if (OplayerId in treasures) {
                                    treasures[OplayerId]?.let {
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
     * @inheritDoc
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
                // If a winner has been set
                if (winner != "") {
                    Log.d("addPlayListener", "The winner is $winner")
                    GSM.activeGame!!.winner = winner as String // or something
                }
                // If there is no winner, continue game
                else {
                    // If no moves has been made yet
                    if (moves.size == 0) {
                        Log.d("addPlayListener", "No moves made yet")
                        GSM.activeGame!!.setGameReadyIfReady()
                    } else {
                        // Get the last move
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

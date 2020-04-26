package com.battleship.desktop

import com.badlogic.gdx.Gdx
import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.state.MainMenuState
import com.battleship.model.Game
import com.battleship.model.PendingGame
import com.battleship.model.Player
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.EventListener
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreException
import com.google.cloud.firestore.ListenerRegistration
import com.google.cloud.firestore.QuerySnapshot
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.database.annotations.Nullable
import java.io.FileInputStream

/**
 * Implements behavior from [FirebaseController]
 *
 * Class used to setup the database connection
 * declared as object to make it a singleton
 */
object DesktopFirebase : FirebaseController {
    private const val firebaseUrl = "https://battleshipz.firebaseio.com"
    private val db: Firestore
    private var activeListener: ListenerRegistration? = null
        set(value) {
            field?.remove()
            field = value
        }

    init {
        val firebaseApps = FirebaseApp.getApps()

        if (firebaseApps.size == 0) {
            val serviceAccount = FileInputStream("./adminsdk.json")
            val credentials = GoogleCredentials.fromStream(serviceAccount)
            val options = FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setDatabaseUrl(firebaseUrl)
                .build()
            FirebaseApp.initializeApp(options)
        }

        db = FirestoreClient.getFirestore()
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

        val res = db.collection("games").add(data)
        val gameId = res.get().id
        val game = Game(gameId)
        val player1 = Player(userId, userName)
        val player2 = Player()
        game.setPlayers(player1, player2)
        Gdx.app.postRunnable {
            if (gameId == "") callback(null)
            else callback(game)
        }
    }
    /**
     * Method for joining an existing game.
     * @param gameId the id of the game document
     * @param userId the id of the player that should be added
     * @param userName the name of the player that should be added
     * @param callback function invoked once the process has completed.
     */
    override fun joinGame(gameId: String, userId: String, userName: String, callback: (game: Game?) -> Unit) {
        db.collection("games").document(gameId).update("player2Id", userId)
        db.collection("games").document(gameId).update("player2Name", userName)
        val game = Game(gameId)
        val query = db.collection("games").document(gameId).get()
        val doc = query.get()

        if (doc.exists()) {
            val player1Id: String = doc.getString("player1Id") as String
            val player1Name: String = doc.getString("player1Name") as String
            val player1 = Player(player1Id, player1Name)
            val player2 = Player(userId, userName)
            Gdx.app.log("joinGame", "$player1, $player2")
            game.setPlayers(player1, player2)
        } else {
            Gdx.app.log("setGame", "Failed to set Game!")
            Gdx.app.postRunnable { callback(null) }
        }
        Gdx.app.postRunnable { callback(game) }
    }

    /**
     * Method for leaving an existing game.
     * @param gameId the id of the game document.
     * @param playerId the id of the player that leaves.
     * @param callback function invoked once the process has completed.
     */
    override fun leaveGame(gameId: String, playerId: String, callback: () -> Unit) {
        db.collection("games").document(gameId).update("playerLeft", playerId)
        Gdx.app.postRunnable { callback() }
    }

    /**
     * Method for registering a player's treasures
     * @param gameId the id of the game document
     * @param userId the id of the player
     * @param treasures list containing the treasures that should be added
     */
    override fun registerTreasures(
        gameId: String,
        userId: String,
        treasures: List<Map<String, Any>>
    ) {
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if (game.exists()) {
            val dbTreasures = game.get("treasures") as MutableMap<String, List<Map<String, Any>>>
            dbTreasures[userId] = treasures
            db.collection("games").document(gameId).update("treasures", dbTreasures)
        } else {
            Gdx.app.log("registerTreasures", "Failed to register Treasures!")
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
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if (game.exists()) {
            val moves = game.get("moves") as MutableList<Map<String, Any>>
            val data = mutableMapOf<String, Any>()
            data["x"] = x
            data["y"] = y
            data["playerId"] = playerId
            data["weapon"] = equipment
            moves.add(data)
            db.collection("games").document(gameId).update("moves", moves)
        } else {
            Gdx.app.log("registerMove", "Failed to register move!")
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
    }

    /**
     * Method adding listener to all games where player2 is empty.
     * @param callback function invoked once the process has completed.
     */
    override fun addPendingGamesListener(callback: (pendingGames: ArrayList<PendingGame>) -> Unit) {
        val query = db.collection("games")
                .whereEqualTo("player2Name", "")
                .whereEqualTo("player2Id", "")
        activeListener = query.addSnapshotListener(object : EventListener<QuerySnapshot?> {
            override fun onEvent(
                @Nullable snapshot: QuerySnapshot?,
                @Nullable e: FirestoreException?
            ) {
                if (e != null) {
                    Gdx.app.log("addPendingGamesListener", "Listen failed:", e)
                    return
                }
                if (snapshot != null) {
                    val pendingGames = ArrayList<PendingGame>()
                    for (doc in snapshot.documents) {
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
                        Gdx.app.postRunnable {
                            callback(pendingGames)
                        }
                    }
                }
            }
        })
    }

    /**
     * Function adding listener to a specific game. Listening to when players joins and register treasures.
     * @param gameId the id of the game document
     * @param playerId the id of the player
     */
    override fun addGameListener(gameId: String, playerId: String) {
        val docRef = db.collection("games").document(gameId)
        activeListener = docRef.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(
                @Nullable snapshot: DocumentSnapshot?,
                @Nullable e: FirestoreException?
            ) {
                if (e != null) {
                    Gdx.app.log("addGameListener", "Listen failed:", e)
                    return
                }

                if (snapshot != null && snapshot.exists()) {
                    val player2Id = snapshot.data?.get("player2Id") as String
                    if (snapshot.data?.get("playerLeft") != "") {
                        Gdx.app.log("addGameListener", "Opponent left firebase")
                        GSM.activeGame!!.opponentLeft = true
                    }
                    if (player2Id != "") {
                        val game = GSM.activeGame!!
                        if (game.opponent.playerId == "" || game.opponent.playerName == "") {
                            val player2Name = snapshot.data?.get("player2Name") as String
                            Gdx.app.log("gameListener", "$game, $player2Id, $player2Name")
                            game.setPlayers(game.player, Player(player2Id, player2Name))
                        }
                        val treasures =
                            snapshot.data?.get("treasures") as MutableMap<String, List<Map<String, Any>>>?
                        if (treasures != null) {
                            val opponentId = game.opponent.playerId
                            Gdx.app.log("addGameListener", "$opponentId, ${treasures.keys}")
                            if (opponentId in treasures.keys) {
                                treasures[opponentId]?.let {
                                    Gdx.app.postRunnable {
                                        game.opponent.board.setTreasuresList(it)
                                        game.setGameReadyIfReady()
                                        if (game.gameReady) {
                                            addPlayListener(gameId)
                                        }
                                    }
                                }
                                game.setGameReadyIfReady()
                            }
                            game.setGameReadyIfReady()
                            if (game.gameReady) {
                                addPlayListener(gameId)
                            }
                        }
                        game.setGameReadyIfReady()
                    }
                } else {
                    Gdx.app.log("addGameListener", "Current data: null")
                }
            }
        })
    }

    /**
     * Function adding listener to a specific game. Listening to when players make moves.
     * @param gameId the id of the game document
     */
    override fun addPlayListener(gameId: String) {
        val query = db.collection("games").document(gameId)
        activeListener = query.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(
                @Nullable snapshot: DocumentSnapshot?,
                @Nullable e: FirestoreException?
            ) {
                if (e != null) {
                    Gdx.app.log("addPlayListener", "Listen failed:", e)
                    return
                }
                Gdx.app.log("addPlayListener", "MOVE LISTENER:")
                if (snapshot != null && snapshot.exists()) {
                    var moves = mutableListOf<Map<String, Any>>()
                    if (snapshot.data?.get("moves") != null) {
                        moves =
                            snapshot.data?.get("moves") as MutableList<Map<String, Any>>
                    }

                    val winner = snapshot.data?.get("winner")
                    if (winner != "") {
                        Gdx.app.log("addPlayListener", "The winner is $winner")
                        GSM.activeGame!!.winner = winner as String
                    }
                    else {
                        if (moves.size == 0) {
                            Gdx.app.log("addPlayListener", "No moves made yet")
                            GSM.activeGame!!.setGameReadyIfReady()
                        } else {
                            val lastMove = moves.get(moves.size - 1)
                            val game = GSM.activeGame!!
                            if (lastMove["playerId"]!!.equals(game.opponent.playerId)) {
                                Gdx.app.log("addPlayListener", "----------------------OPPONENT LAST MOVE----------------------- " + lastMove)
                                GSM.activeGame!!.registerMove(lastMove)
                            } else if (lastMove["playerId"]!!.equals(game.player.playerId)) {
                                Gdx.app.log("addPlayListener", "----------------------PLAYER LAST MOVE----------------------- " + lastMove)
                            }
                        }
                    }
                } else {
                    Gdx.app.log("addPlayListener", "Current data: null")
                }
            }
        })
    }
}

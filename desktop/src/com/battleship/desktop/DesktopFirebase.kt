package com.battleship.desktop

import com.badlogic.gdx.Gdx
import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.Game
import com.battleship.model.GameListObject
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
 * Class used to setup the database connection
 * declared as object to make it a singleton
 */
object DesktopFirebase : FirebaseController {
    // The URL of the firebase project
    private const val firebaseUrl = "https://battleshipz.firebaseio.com"

    // Protected variable used by the other controllers to access database
    private val db: Firestore

    // Set up database connection
    init {
        // Get the firebase apps running
        val firebaseApps = FirebaseApp.getApps()

        // If no firebase apps is running, set it up
        if (firebaseApps.size == 0) {
            // Read the account details from file
            val serviceAccount = FileInputStream("./adminsdk.json")
            // Get the credentials from the account details
            val credentials = GoogleCredentials.fromStream(serviceAccount)
            // Set options for connection
            val options = FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl(firebaseUrl)
                    .build()
            FirebaseApp.initializeApp(options)
        }

        // Initialize database connection using the firebase app
        db = FirestoreClient.getFirestore()
    }

    private var activeListener: ListenerRegistration? = null
        set(value) {
            field?.remove()
            field = value
        }

    /**
     * Start new game
     * @param userId the id of the user setting up the game
     */
    override fun createGame(userId: String, userName: String, callback: (game: Game) -> Unit) {
        // Set up game data
        val data = mutableMapOf<String, Any>()
        data["player1Id"] = userId
        data["player1Name"] = userName
        data["player2Id"] = ""
        data["player2Name"] = ""
        data["winner"] = ""
        data["moves"] = mutableListOf<Map<String, Any>>()
        data["treasures"] = mutableMapOf<String, List<Map<String, Any>>>()

        val res = db.collection("games").add(data)
        val gameId = res.get().id
        val game = setGame(gameId)
        Gdx.app.postRunnable { callback(game) }
    }

    /**
     * Function getting all games where there is currently only one player
     */

    override fun addPendingGamesListener(callback: (pendingGames: ArrayList<GameListObject>) -> Unit) {
        val query = db.collection("games")
            .whereEqualTo("player2Name", "")
            .whereEqualTo("player2Id", "")
        activeListener = query.addSnapshotListener(object : EventListener<QuerySnapshot?> {
            override fun onEvent(
                @Nullable snapshot: QuerySnapshot?,
                @Nullable e: FirestoreException?
            ) {
                if (e != null) {
                    System.err.println("Listen failed: $e")
                    return
                }
                if (snapshot != null) {
                    val pendingGames = ArrayList<GameListObject>()
                    for (doc in snapshot.documents) {
                        val player1Id: String = doc.getString("player1Id") as String
                        val player1Name: String = doc.getString("player1Name") as String
                        val gameId = doc.id
                        if (player1Id != "") {
                            pendingGames.add(
                                GameListObject(
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
     * Gets username from a registered player
     * @param userId the id of the user that should be added
     * @return a player object
     */
    private fun getUser(userId: String): Player {
        val docRef = db.collection("users").document(userId).get()
        val user = docRef.get()

        if (user.exists()) {
            val username: String = user.get("username") as String
            if (username != "") {
                return Player(userId, username)
            }
            return Player(userId, "Unknown")
        } else {
            // Add error handling
            throw error("Something went wrong when getting user")
        }
    }

    /**
     * Add userId to a specific game
     * @param gameId the id of the game document
     * @param userId the id of the user that should be added
     */
    override fun joinGame(gameId: String, userId: String, userName: String, callback: (game: Game) -> Unit) {
        // Add the data to the game document
        db.collection("games").document(gameId).update("player2Id", userId)
        db.collection("games").document(gameId).update("player2Name", userName)
        val game = setGame(gameId)
        callback(game)
    }

    /**
     * Registers the treasures on the board for a given user
     * @param gameId the id of the game document
     * @param userId the id of the user owning the treasures
     * @param treasures list containing the treasures that should be added, each described using a map
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
            // TODO: Add exception handling
            println("Something went wrong when registering treasures")
        }
    }

    /**
     * Get the treasures in a game
     * @param gameId the id of the game
     * @return a map containing a list of treasures per user
     * @return a Game object containing game and player
     */
    private fun setGame(gameId: String): Game {
        val game = Game(gameId)
        val query = db.collection("games").document(gameId).get()
        val doc = query.get()

        if (doc.exists()) {
            println(doc)
            val player1Id: String = doc.getString("player1Id") as String
            val player1Name: String = doc.getString("player1Name") as String
            val player1 = Player(player1Id, player1Name)

            val player2Id: String = doc.getString("player2Id") as String
            val player2Name: String = doc.getString("player2Name") as String
            val player2: Player =
                if (player2Id != "" && player2Name != "")
                    Player(player2Id, player2Name)
                else Player()

            game.setPlayers(player1, player2)
            println("setGame: $game")
            return game
        } else {
            throw error("Something went wrong when setting the Game")
        }
    }

    /**
     * Get the ships in a game
     * @param gameId the id of the game
     * @return a Game object containing game and player
     */
    private fun getOpponent(gameId: String) {
        val query = db.collection("games").document(gameId).get()
        val game = query.get()

        if (game.exists()) {
            val player2Id: String = game.get("player2Id") as String

            GSM.activeGame!!.opponent.playerId = player2Id
            GSM.activeGame!!.opponent.playerName = db.collection("users").document(player2Id).get().get().get("username").toString()
            println("opponent name: " + GSM.activeGame!!.opponent.playerName)
        } else {
            throw error("Something went wrong when fetching the Game")
        }
    }

    /**
     * get the treasures from firebase and stores them in a game
     * @param gameId the id of the game
     * @return a map containing a list of treasures per user
     */
    private fun getTreasures(gameId: String) {
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if (game.exists()) {
            val treasures = game.get("treasures") as MutableMap<String, List<Map<String, Any>>>
            // val treasureO = treasures[GSM.activeGame!!.opponent.playerId]
            // if (treasures != null) {
                // GSM.activeGame!!.setTreasures(treasures)
                /* if (GSM.activeGame!!.me.playerId in treasures) {
                    treasures[GSM.activeGame!!.me.playerId]?.let { GSM.activeGame!!.me.board.setTreasuresList(it) }
                } */
                if (GSM.activeGame!!.opponent.playerId in treasures) {
                    treasures[GSM.activeGame!!.opponent.playerId]?.let {
                        GSM.activeGame!!.opponent.board.setTreasuresList(it)
                    }
                    GSM.activeGame!!.setGameReadyifReady()
                }
            // }
        } else {
            // TODO: Add error handling
            throw error("Something went wrong when fetching treasures")
        }
    }

    /**
     * Registers the move
     * @param gameId the id of the game document
     * @param x x coordinate of move
     * @param y y coordinate of
     * @param playerId player making the move
     */
    override fun makeMove(gameId: String, x: Int, y: Int, playerId: String, weapon: String) {
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if (game.exists()) {
            val moves = game.get("moves") as MutableList<Map<String, Any>>
            val data = mutableMapOf<String, Any>()
            data["x"] = x
            data["y"] = y
            data["playerId"] = playerId
            data["weapon"] = weapon
            moves.add(data)
            db.collection("games").document(gameId).update("moves", moves)
            // TODO: Call function that handles what should happen after move is made
        } else {
            // TODO: Add exception handling
            println("Something went wrong when making move")
        }
    }

    /**
     * Set the winner of the game
     * @param userId the id of the winner
     * @param gameId the id of the game document
     */
    override fun setWinner(userId: String, gameId: String) {
        db.collection("games").document(gameId).update("winner", userId)
        // TODO: Call function that should handle what happens when a winner is set
    }

    /**
     * Function adding listener to a specific game
     * TODO: Add exception handling
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
                    System.err.println("Listen failed: $e")
                    return
                }

                if (snapshot != null && snapshot.exists()) {
                    val player2Id = snapshot.data?.get("player2Id") as String
                    // If no opponent has joined yet
                    if (player2Id != "") {
                        val game = GSM.activeGame!!
                        if (game.opponent.playerId == "") {
                            val player2Name = snapshot.data?.get("player2Name") as String
                            game.opponent = Player(player2Id, player2Name)
                        }
                        // Get the field containing the treasures in the database
                        val treasures =
                            snapshot.data?.get("treasures") as MutableMap<String, List<Map<String, Any>>>?
                        if (treasures != null) {
                            val opponentId = game.opponent.playerId
                            println("$opponentId, ${treasures.keys}")
                            if (opponentId in treasures.keys) {
                                treasures[opponentId]?.let {
                                    Gdx.app.postRunnable {
                                        game.opponent.board.setTreasuresList(it)
                                    }
                                }
                                game.setGameReadyifReady()
                            }
                            game.setGameReadyifReady()
                            if (game.gameReady) {
                                addPlayListener(gameId)
                            }
                        }
                        game.setGameReadyifReady()
                    }
                }
            }
        })
    }

    override fun addPlayListener(gameId: String) {
        val query = db.collection("games").document(gameId)
        activeListener = query.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(
                @Nullable snapshot: DocumentSnapshot?,
                @Nullable e: FirestoreException?
            ) {
                if (e != null) {
                    System.err.println("Listen failed: $e")
                    return
                }
                println("addPlayListener: MOVE LISTENER:")
                if (snapshot != null && snapshot.exists()) {
                    println("addPlayListener: Game data: ${snapshot.data}")

                    var moves = mutableListOf<Map<String, Any>>()
                    if (snapshot.data?.get("moves") != null) {
                        moves =
                                snapshot.data?.get("moves") as MutableList<Map<String, Any>>
                    }

                    val winner = snapshot.data?.get("winner")
                    // If a winner has been set
                    if (winner != "") {
                        println("addPlayListener: The winner is $winner")
                        // TODO: Call function that should be called when a winner is registered
                        GSM.activeGame!!.winner = winner as String // or something
                    }
                    // If there is no winner, continue game
                    else {
                        // If no moves has been made yet
                        if (moves.size == 0) {
                            println("addPlayListener: No moves made yet")
                            GSM.activeGame!!.setGameReadyifReady()
                        } else {
                            // Get the last move
                            val lastMove = moves.get(moves.size - 1)
                            val game = GSM.activeGame!!
                            if (lastMove["playerId"]!!.equals(game.opponent.playerId)) {
                                println("----------------------OPPONENT LAST MOVE----------------------- " + lastMove)
                                GSM.activeGame!!.registerMove(lastMove)
                            } else if (lastMove["playerId"]!!.equals(game.player.playerId)) {
                                println("----------------------PLAYER LAST MOVE----------------------- " + lastMove)
                            }
                        }
                    }
                } else {
                    print("Current data: null")
                }
            }
        })
    }
}

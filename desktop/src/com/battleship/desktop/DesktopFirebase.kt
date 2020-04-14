package com.battleship.desktop

import com.battleship.controller.firebase.FirebaseController
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.EventListener
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreException
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

    /**
     * Get all the players registered in the database
     * @return a map containing user id and username
     */
    override fun getPlayers() {
        val query = db.collection("users").get()
        val querySnapshot = query.get()
        val documents = querySnapshot.documents
        val playerMap = mutableMapOf<String, String?>()

        for (document in documents) {
            val id = document.id
            val name = document.getString("username")
            playerMap[id] = name
        }
        //TODO: Call function that saves the playerMap
    }

    /**
     * Register new player in the db
     * @param username the username wanted
     * @return id of player
     */
    override fun addPlayer(username: String) {
        // Add the data to a Map for pushing to db
        val data = mutableMapOf<String, Any>()
        data["username"] = username

        // Push to db
        val addRes = db.collection("users").add(data)
        val playerId = addRes.get().id
        // TODO: Call function saving the playerId
    }

    /**
     * Start new game
     * @param userId the id of the user setting up the game
     */
    override fun createGame(userId: String) {
        // Set up game data
        val data = mutableMapOf<String, Any>()
        data["player1"] = userId
        data["player2"] = ""
        data["winner"] = ""
        data["moves"] = mutableListOf<Map<String, Any>>()
        data["treasures"] = mutableMapOf<String, List<Map<String, Any>>>()

        val res = db.collection("games").add(data)
        val gameId = res.get().id
        // TODO: Call function that saves the gameId for later use
    }

    /**
     * Function getting all games where there is currently only one player
     */
    override fun getPendingGames() {
        val gameQuery = db.collection("games").whereEqualTo("player2", "").get()
        val gameQuerySnapshot = gameQuery.get()
        val gameDocuments = gameQuerySnapshot.documents
        val games = mutableMapOf<String, String>()

        // For each game fitting the criteria, get the id and username of opponent
        for (document in gameDocuments) {
            val id = document.id
            val playerId = document.getString("player1")

            // Find the username of the player in the game to display
            val playerQuery = playerId?.let { db.collection("users").document(playerId).get() }
            val playerQuerySnapshot = playerQuery?.get()
            val playerName = playerQuerySnapshot?.getString("username")
            if (playerName != null) {
                games[id] = playerName
            }
        }
        // TODO: Call function that saves the "games" list of pending games
    }

    /**
     * Add userId to a specific game
     * @param gameId the id of the game document
     * @param userId the id of the user that should be added
     */
    override fun joinGame(gameId: String, userId: String) {
        // Add the data to the game document
        db.collection("games").document(gameId).update("player2", userId)
        // TODO: Call function that handles what should happend when you have joined a game
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
            // TODO: Call function handling what should happend after registering treasure
        } else {
            // TODO: Add exception handling
            println("Something went wrong when registering treasures")
        }
    }

    /**
     * Get the treasures in a game
     * @param gameId the id of the game
     * @return a map containing a list of treasures per user
     */
    override fun getTreasures(gameId: String) {
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if (game.exists()) {
            val treasures = game.get("treasures") as MutableMap<String, List<Map<String, Any>>>
            // TODO: Call a function that saves the map of treasures
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
    override fun makeMove(gameId: String, x: Int, y: Int, playerId: String) {
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if (game.exists()) {
            val moves = game.get("moves") as MutableList<Map<String, Any>>
            val data = mutableMapOf<String, Any>()
            data["x"] = x
            data["y"] = y
            data["playerId"] = playerId
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
     * TODO: Replace println with functionality connected to the cases
     * TODO: Add exception handling
     * @param gameId the id of the game document
     * @param playerId the id of the player
     */
    override fun addGameListener(gameId: String, playerId: String) {
        val docRef = db.collection("games").document(gameId)
        docRef.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(
                @Nullable snapshot: DocumentSnapshot?,
                @Nullable e: FirestoreException?
            ) {
                if (e != null) {
                    System.err.println("Listen failed: $e")
                    return
                }

                if (snapshot != null && snapshot.exists()) {
                    val opponent = snapshot.data?.get("player2")
                    // If no opponent has joined yet
                    if (opponent == "") {
                        println("Opponent not joined yet")
                    }
                    // If there is an opponent in the game
                    else {
                        // Get the field containing the treasures in the database
                        val treasures =
                            snapshot.data?.get("treasures") as MutableMap<String, List<Map<String, Any>>>

                        // If there is not enough treasures registered
                        // TODO: Do this in a better way
                        if (treasures.size < 2) {
                            println("Treasures not registered")
                        } else {

                            // Get the list of moves
                            val moves = snapshot.data?.get("moves") as MutableList<Map<String, Any>>

                            val winner = snapshot.data?.get("winner")
                            // If a winner has been set
                            if (winner != "") {
                                println("The winner is $winner")
                            }
                            // If there is no winner, continue game
                            else {
                                // If no moves has been made yet
                                if (moves.size == 0) {
                                    println("No moves made yet")
                                } else {
                                    // Get the last move
                                    val lastMove = moves.get(moves.size - 1)
                                    // If the last move is performed by opponent
                                    if (!lastMove["playerId"]!!.equals(playerId)) {
                                        println("Motstander hadde siste trekk")
                                    } else {
                                        println("Du hadde siste trekk, vent")
                                    }
                                }
                            }
                        }
                    }
                }
                // If no data is found
                else {
                    print("Current data: null")
                }
            }
        })
    }
}

package com.battleship

import android.util.Log
import com.battleship.controller.firebase.FirebaseController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

object AndroidFirebase : FirebaseController {
    private val db = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()

    init {
        //Sign in as an anonymous user to authorize, don't know if runBlocking actually works
        runBlocking {
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Login", "signInAnonymously successful")
                    } else {
                        //TODO: Handle exception, when this occurs you cannot access firebase
                        Log.w("Login", "signInAnonymously failed", task.exception)
                    }
                }
        }
    }

    /**
     * NOT USED IN THE ACTUAL APP, CAN BE REMOVED
     * Get all the players registered in the database
     * @return a map containing user id and username
     */
    override fun getPlayers() {
        val playerMap = mutableMapOf<String, String?>()
        db.collection("users").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("Player", "${document.id} => ${document.data}")
                    playerMap[document.id] = document.getString("username")
                }
                //TODO IF USED IN APP: Add function here that stores players
            }
            .addOnFailureListener { exception ->

                Log.w("Player", "Error getting documents: ", exception)
            }
    }

    /**
     * Register new player in the db
     * @param username the username wanted
     */
    override fun addPlayer(username: String) {
        // Add the data to a Map for pushing to db
        val data = mutableMapOf<String, Any>()
        data["username"] = username

        // Push to db
        db.collection("users").add(data)
            .addOnSuccessListener { documentReference ->
                val id = documentReference.id
                Log.d("addPlayer", "player added with id=${id}")
                //TODO: Call some function that saves player id for later use
            }
            .addOnFailureListener { exception ->
                Log.w("addPlayer", exception)
                //TODO: Add exception handling
            }
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

        db.collection("games").add(data)
            .addOnSuccessListener { documentReference ->
                val gameId = documentReference.id
                Log.d("createGame", "created game with id=${gameId}")
                //TODO: Call some function that saves the game id for later use
            }
            .addOnFailureListener { exception ->
                Log.w("createGame", exception)
                //TODO: Add exception handling
            }
    }

    /**
     * Function getting all games where there is currently only one player
     */
    override fun getPendingGames() {
        //This call get all games where no player2 i registered
        db.collection("games").whereEqualTo("player2", "").get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    for (document in documents) {
                        Log.d("getPendingGames", "game found with id=${document.id}")
                        val playerId = document.getString("player1")
                        if (playerId != null) {
                            //This call get the username of the player registered under player1
                            db.collection("users").document(playerId).get()
                                .addOnSuccessListener { documentReference ->
                                    val username = documentReference.get("username")
                                    val gameId = document.id
                                    Log.d(
                                        "getPendingGames",
                                        "game found with id=${gameId} and " +
                                            "user=${username}"
                                    )
                                    //TODO: Call some function that adds the gameId and the username to a list of pending games
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("getPendingGames", exception)
                                    //TODO: Add exception handling, could not find player
                                }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("getPendingGames", exception)
                //TODO: Add exception handling, could not find games
            }
    }

    /**
     * Add userId to a specific game
     * @param gameId the id of the game document
     * @param userId the id of the user that should be added
     */
    override fun joinGame(gameId: String, userId: String) {
        // Add the data to the game document
        db.collection("games").document(gameId).update("player2", userId)
            .addOnSuccessListener {
                Log.d("joinGame", "Updated successfully")
                //TODO: Call some function that should be triggered when you joined successfully
            }
            .addOnFailureListener { exception ->
                Log.w("joinGame", exception)
                //TODO: Add exception handling
            }
    }

    /**
     * Registers the treasures on the board for a given user
     * @param gameId the id of the game document
     * @param userId the id of the user owning the treasure
     * @param treasures list containing the treasures that should be added, each described using a map
     */
    override fun registerTreasures(
        gameId: String,
        userId: String,
        treasures: List<Map<String, Any>>
    ) {
        val dbTreasures = mutableMapOf<String, List<Map<String, Any>>>()
        dbTreasures[userId] = treasures
        db.collection("games").document(gameId).update("treasures", dbTreasures)
            .addOnSuccessListener {
                Log.d("registerTreasures", "treasures registered")
                //TODO: Call some function that should be triggered when treasures is added
            }
            .addOnFailureListener { exception ->
                Log.w("registerTreasures", exception)
                //TODO: Add exception handling
            }
    }

    /**
     * Get the treasure in a game
     * @param gameId the id of the game
     * @return a map containing a list of treasure per user
     */
    override fun getTreasures(gameId: String) {
        db.collection("games").document(gameId).get()
            .addOnSuccessListener { document ->
                val treasures = document.get("treasures")
                Log.d("getTreasures", "successful! $treasures")
                //TODO: Call function that stores the treasures
            }
            .addOnFailureListener { exception ->
                Log.w("getTreasures", exception)
                //TODO: Add exception handling
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
        //Get the document for the game using the id of the document
        db.collection("games").document(gameId).get()
            .addOnSuccessListener { document ->
                //Saving existing moves
                var moves = mutableListOf<Map<String, Any>>()
                if (document.get("moves") != null) {
                    moves = document.get("moves") as MutableList<Map<String, Any>>
                }
                //Making a map for the new move
                val data = mutableMapOf<String, Any>()
                data["x"] = x
                data["y"] = y
                data["playerId"] = playerId
                //Add the move to the list of existing moves
                moves.add(data)
                //Push the list of moves to the database
                db.collection("games").document(gameId).update("moves", moves)
                    .addOnSuccessListener {
                        Log.d("makeMove", "move made")
                        //TODO: Call function that should be called when move is made
                    }
                    .addOnFailureListener { exception ->
                        Log.w("makeMove", exception)
                        //TODO: Add exception handling, could not push data to db
                    }
            }
            .addOnFailureListener { exception ->
                Log.w("makeMove", exception)
                //TODO: Add exception handling, could not find game document
            }
    }

    /**
     * Set the winner of the game
     * @param userId the id of the winner
     * @param gameId the id of the game document
     */
    override fun setWinner(userId: String, gameId: String) {
        db.collection("games").document(gameId).update("winner", userId)
            .addOnSuccessListener {
                Log.d("setWinner", "success")
                //TODO: Call function that handles what should happen after a player won
            }
            .addOnFailureListener { exception ->
                Log.w("setWinner", exception)
                //TODO: Add exception handling
            }
    }

    /**
     * Function adding listener to a specific game
     * @param gameId the id of the game document
     * @param playerId the id of the player
     */
    override fun addGameListener(gameId: String, playerId: String) {
        val docRef = db.collection("games").document(gameId)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("addGameListener", "Listener failed", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d("addGameListener", "Game data: ${snapshot.data}")
                val opponent = snapshot.data?.get("player2")
                // If no opponent has joined yet
                if (opponent == "") {
                    Log.d("addGameListener", "Opponent not joined yet")
                    //TODO: Call function that should be called when no player has joined yet
                }
                // If there is an opponent in the game
                else {
                    // Get the field containing the treasures in the database
                    var treasures = mutableMapOf<String, List<Map<String, Any>>>()
                    if (snapshot.data?.get("treasures") != null) {
                        treasures =
                            snapshot.data?.get("treasures") as MutableMap<String, List<Map<String, Any>>>
                    }

                    // If there is not enough treasures registered
                    if (treasures.size < 2) {
                        Log.d("addGameListener", "Treasures not registered")
                        //TODO: Call function that should be called when not enough treasures is registered
                    } else {

                        // Get the list of moves
                        var moves = mutableListOf<Map<String, Any>>()
                        if (snapshot.data?.get("moves") != null) {
                            moves = snapshot.data?.get("moves") as MutableList<Map<String, Any>>
                        }

                        val winner = snapshot.data?.get("winner")
                        // If a winner has been set
                        if (winner != "") {
                            Log.d("addGameListener", "The winner is $winner")
                            //TODO: Call function that should be called when a winner is registered
                        }
                        // If there is no winner, continue game
                        else {
                            // If no moves has been made yet
                            if (moves.size == 0) {
                                Log.d("addGameListener", "No moves made yet")
                                //TODO: Call function that should be called when no moves is registered yet
                            } else {
                                // Get the last move
                                val lastMove = moves.get(moves.size - 1)
                                // If the last move is performed by opponent
                                if (!lastMove["playerId"]!!.equals(playerId)) {
                                    Log.d("addGameListener", "Opponent made the last move")
                                    //TODO: Call function that should be called when it's your turn
                                } else {
                                    Log.d("addGameListener", "You made the last move")
                                    //TODO: Call function that should be called when it's not your turn
                                }
                            }
                        }
                    }
                }
            } else {
                Log.d("addGameListener", "no data")
                //TODO: Error handling when the game object is found, but there is no data in it
            }
        }
    }
}
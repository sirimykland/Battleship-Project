package com.battleship

import android.util.Log
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.Game
import com.battleship.model.GameListObject
import com.battleship.model.Player
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.runBlocking

object AndroidFirebase : FirebaseController {
    private val db = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()

    init {
        // Sign in as an anonymous user to authorize, don't know if runBlocking actually works
        runBlocking {
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Login", "signInAnonymously successful")
                    } else {
                        // TODO: Handle exception, when this occurs you cannot access firebase
                        Log.w("Login", "signInAnonymously failed", task.exception)
                    }
                }
        }
    }

    private var activeListener: ListenerRegistration? = null
        set(value) {
            field?.remove()
            field = value
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
                // TODO IF USED IN APP: Add function here that stores players
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
                val userId = documentReference.id
                Log.d("addPlayer", "player added with id=$userId")
                // TODO: Call some function that saves player id for later use
                GSM.userId = userId
                GSM.username = username
            }
            .addOnFailureListener { exception ->
                Log.w("addPlayer", exception)
                // TODO: Add exception handling
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
                Log.d("createGame", "created game with id=$gameId")
                setGame(gameId)
            }
            .addOnFailureListener { exception ->
                Log.w("createGame", exception)
                // TODO: Add exception handling
            }
    }

    /**
     * Get the treasures in a game
     * @param gameId the id of the game
     * @return a map containing a list of treasures per user
     * @return a Game object containing game and player
     */
    override fun setGame(gameId: String) {
        GSM.activeGame = Game(gameId)
        db.collection("games").document(gameId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("setGame", "game set with id=${document.id}")
                    val player1Id: String = document.getString("player1") as String
                    val player1: Player = Player(player1Id, "Unknown")

                    val player2Id: String = document.getString("player2") as String
                    val player2: Player =
                        if (player2Id != "")
                            Player(player2Id, "Unknown")
                        else Player()
                    GSM.activeGame!!.setPlayers(player1, player2)

                    for (p in listOf(GSM.activeGame!!.player, GSM.activeGame!!.opponent)) {
                        if (p.playerId != "") {
                            // This call get the username of the player registered under player1
                            db.collection("users").document(p.playerId).get()
                                .addOnSuccessListener { documentReference ->
                                    val username = documentReference.get("username")
                                    Log.d(
                                        "setGame",
                                        "user set with id=${p.playerId} and " + "user=$username"
                                    )
                                    if (username != null) {
                                        p.playerName = username as String
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("setGame", exception)
                                    // TODO: Add exception handling, could not find player
                                }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("setGame", exception)
                // TODO: Add exception handling, could not find games
            }
    }

    override fun addPendingGamesListener() {
        val pendingGames = ArrayList<GameListObject>()
        activeListener =
            db.collection("games").whereEqualTo("player2", "").addSnapshotListener { documents, e ->
                if (e != null) {
                    Log.w("addPendingGamesListener", "Listen failed.", e)
                    return@addSnapshotListener
                }
                Log.d("addPendingGamesListener", "Found documents containing pending games")
                var documentsCompleted = 0
                for (doc in documents!!) {
                    val playerId: String = doc.getString("player1") as String
                    val gameId = doc.id
                    if (playerId != "") {
                        pendingGames.add(
                            GameListObject(
                                gameId,
                                playerId,
                                "Testname"
                            )
                        )
                        documentsCompleted++
                        if (documentsCompleted == documents.size()) {
                            println("-------------------------------- " + pendingGames.toString())
                            GSM.pendingGames = pendingGames
                        }
                        // This call get the username of the player registered under player1
                        /*
                        db.collection("users").document(playerId).get()
                            .addOnSuccessListener { documentReference ->
                                val username = documentReference.get("username")
                                val gameId = doc.id
                                Log.d(
                                    "getPendingGames",
                                    "game found with id=$gameId and " +
                                        "user=$username"
                                )
                                if (username != null) {
                                    pendingGames.add(
                                        GameListObject(
                                            gameId,
                                            playerId,
                                            username as String
                                        )
                                    )
                                }
                                documentsCompleted++
                                if (documentsCompleted == documents.size()) {
                                    println("-------------------------------- " + pendingGames.toString())
                                    GSM.pendingGames = pendingGames
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("getPendingGames", exception)
                                // TODO: Add exception handling, could not find player
                            }

                         */
                    }
                }

            }

    }

    /**
     * Function getting all games where there is currently only one player
     */
    /*
    override fun getPendingGames() {
        val pendingGames = ArrayList<GameListObject>()
        //  This call get all games where no player2 i registered
        db.collection("games").whereEqualTo("player2", "").get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    var documentsCompleted = 0
                    for (document in documents) {
                        Log.d("getPendingGames", "game found with id=${document.id}")
                        val playerId: String = document.getString("player1") as String
                        if (playerId != "") {
                            // This call get the username of the player registered under player1
                            db.collection("users").document(playerId).get()
                                .addOnSuccessListener { documentReference ->
                                    val username = documentReference.get("username")
                                    val gameId = document.id
                                    Log.d(
                                        "getPendingGames",
                                        "game found with id=$gameId and " +
                                            "user=$username"
                                    )
                                    if (username != null) {
                                        pendingGames.add(
                                            GameListObject(
                                                gameId,
                                                playerId,
                                                username as String
                                            )
                                        )
                                    }
                                    documentsCompleted++
                                    if (documentsCompleted == documents.size()) {
                                        Log.d("getPendingGames", "all complete")
                                        GSM.pendingGames = pendingGames
                                    }
                                    Log.d("getpendingGames", GSM.pendingGames.toString())
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("getPendingGames", exception)
                                    // TODO: Add exception handling, could not find player
                                }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("getPendingGames", exception)
                // TODO: Add exception handling, could not find games
            }
    }

     */

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
                // TODO: Call some function that should be triggered when you joined successfully
                setGame(gameId)
            }
            .addOnFailureListener { exception ->
                Log.w("joinGame", exception)
                // TODO: Add exception handling
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
        db.collection("games").document(gameId).get()
            .addOnSuccessListener { document ->
                val dbTreasures =
                    document.get("treasures") as MutableMap<String, List<Map<String, Any>>>
                dbTreasures[userId] = treasures
                db.collection("games").document(gameId).update("treasures", dbTreasures)
                    .addOnSuccessListener {
                        Log.d("registerTreasures", "treasures registered")
                    }
                    .addOnFailureListener { exception ->
                        Log.w("registerTreasures", exception)
                        // TODO: Add exception handling
                    }
            }
            .addOnFailureListener { exception ->
                Log.w("registerTreasures", exception)
                // TODO: Add exception handling
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
                if (document != null) {
                    val treasures = document.get("treasures") as Map<String, List<Map<String, Any>>>
                    Log.d("getTreasures", "successful! $treasures")
                    // TODO: Call function that stores the treasures
                    // GSM.activeGame!!.setTreasures(treasures as Map<String, List<Map<String, Any>>>)
                    // if (GSM.activeGame!!.me.playerId in treasures) treasures[GSM.activeGame!!.me.playerId]?.let { GSM.activeGame!!.me.board.setTreasuresList(it) }
                    val OplayerId = GSM.activeGame!!.opponent.playerId
                    if (OplayerId in treasures) {
                        println("yes1 it is in treasures")
                        treasures[OplayerId]?.let {
                            GSM.activeGame!!.opponent.board.setTreasuresList(it)
                        }
                        GSM.activeGame!!.setGameReadyifReady()
                    }
                    /*
                    else if (treasures.containsKey(OplayerId)) {
                        println("yes2 it is in treasures")
                        treasures[OplayerId]?.let {
                            GSM.activeGame!!.opponent.board.setTreasuresList(it)
                        }
                        GSM.activeGame!!.isGameReady()
                    }

                     */
                }
            }
            .addOnFailureListener { exception ->
                Log.w("getTreasures", exception)
                // TODO: Add exception handling
            }
    }

    /**
     * Get the ships in a game
     * @param gameId the id of the game
     * @return a Game object containing game and player
     */
    override fun getOpponent(gameId: String) {
        db.collection("games").document(gameId).get()
            .addOnSuccessListener { document ->
                val player2Id = document.get("player2")
                Log.d("getOpponent", "successful! $player2Id")
                // TODO: Call function that stores the treasures
                GSM.activeGame!!.opponent.playerId = player2Id as String
                db.collection("users").document(player2Id).get()
                    .addOnSuccessListener { document ->
                        GSM.activeGame!!.opponent.playerName = document.get("username") as String
                        GSM.activeGame!!.setGameReadyifReady()
                    }.addOnFailureListener { exception ->
                        Log.w("getOpponent", exception)
                        // TODO: Add exception handling
                    }
            }
            .addOnFailureListener { exception ->
                Log.w("getOpponent", exception)
                // TODO: Add exception handling
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
        // Get the document for the game using the id of the document
        db.collection("games").document(gameId).get()
            .addOnSuccessListener { document ->
                // Saving existing moves
                var moves = mutableListOf<Map<String, Any>>()
                if (document.get("moves") != null) {
                    moves = document.get("moves") as MutableList<Map<String, Any>>
                }
                // Making a map for the new move
                val data = mutableMapOf<String, Any>()
                data["x"] = x
                data["y"] = y
                data["playerId"] = playerId
                data["weapon"] = weapon
                // Add the move to the list of existing moves
                moves.add(data)
                // Push the list of moves to the database
                db.collection("games").document(gameId).update("moves", moves)
                    .addOnSuccessListener {
                        Log.d("makeMove", "move made")
                        // TODO: Call function that should be called when move is made
                    }
                    .addOnFailureListener { exception ->
                        Log.w("makeMove", exception)
                        // TODO: Add exception handling, could not push data to db
                    }
            }
            .addOnFailureListener { exception ->
                Log.w("makeMove", exception)
                // TODO: Add exception handling, could not find game document
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
                // TODO: Call function that handles what should happen after a player won
            }
            .addOnFailureListener { exception ->
                Log.w("setWinner", exception)
                // TODO: Add exception handling
            }
    }

    /**
     * Function adding listener to a specific game
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
                val opponent = snapshot.data?.get("player2")
                // If no opponent has joined yet
                if (opponent == "") {
                    Log.d("addGameListener", "Opponent not joined yet")
                    // TODO: Call function that should be called when no player has joined yet
                }
                // If there is an opponent in the game
                else {
                    // println("opponent id $opponent")
                    if (GSM.activeGame!!.opponent.playerId == "") {
                        getOpponent(gameId)
                    } else {
                        // Get the field containing the treasures in the database
                        var treasures = mutableMapOf<String, List<Map<String, Any>>>()
                        if (snapshot.data?.get("treasures") != null) {
                            treasures =
                                snapshot.data?.get("treasures") as MutableMap<String, List<Map<String, Any>>>

                            // If there is not enough treasures registered
                            if (treasures.size < 2 && GSM.activeGame!!.isplayersRegistered()) {
                                Log.d("addGameListener", "Treasures not registered")
                                println("Size: " + treasures.size + " TESTING")
                                getTreasures(gameId)
                            }
                            // If there is enough treasures registered in firebase but not in opponents board
                            else if (treasures.size == 2 && !GSM.activeGame!!.isTreasuresRegistered()) {
                                val OplayerId = GSM.activeGame!!.opponent.playerId
                                if (OplayerId in treasures) {
                                    treasures[OplayerId]?.let {
                                        GSM.activeGame!!.opponent.board.setTreasuresList(it)
                                    }
                                    GSM.activeGame!!.setGameReadyifReady()
                                }
                            } else {
                                addPlayListener(gameId)
                            }
                        }
                    }
                }
            } else
                Log.d("addGameListener", "no data")
            // TODO: Error handling when the game object is found, but there is no data in it
        }
    }

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
                    // TODO: Call function that should be called when a winner is registered
                    GSM.activeGame!!.winner = winner as String // or something
                }
                // If there is no winner, continue game
                else {
                    // If no moves has been made yet
                    if (moves.size == 0) {
                        Log.d("addPlayListener", "No moves made yet")
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
            } else
                Log.d("addPlayListener", "no data")
            // TODO: Error handling when the game object is found, but there is no data in it
        }
    }
}

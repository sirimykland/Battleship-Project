package com.battleship.controller.firebase

import com.battleship.GSM
import com.battleship.model.Game
import com.battleship.model.GameListObject
import com.battleship.model.Player
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.EventListener
import com.google.cloud.firestore.FirestoreException
import com.google.cloud.firestore.ListenerRegistration
import com.google.firebase.database.annotations.Nullable

/**
 * Controller handling all database activity concerned with game flow
 */
@Suppress("UNCHECKED_CAST")
class GameController : FirebaseController() {
    var registration: ListenerRegistration? = null
    /**
     * Start new game
     * @param userId the id of the user setting up the game
     */
    fun createGame(userId: String): String {
        // Set up game data
        val data = mutableMapOf<String, Any>()
        data["player1"] = userId
        data["player2"] = ""
        data["winner"] = ""
        data["moves"] = mutableListOf<Map<String, Any>>()
        data["treasures"] = mutableMapOf<String, List<Map<String, Any>>>()

        val res = db.collection("games").add(data)

        // Return the id of the game
        return res.get().id
    }

    /**
     * Function getting all games where there is currently only one player
     */
    fun getPendingGames(): ArrayList<GameListObject> {
        val gameQuery = db.collection("games").whereEqualTo("player2", "").get()
        val gameQuerySnapshot = gameQuery.get()
        val gameDocuments = gameQuerySnapshot.documents
        val games = ArrayList<GameListObject>()
        // For each game fitting the criteria, get the id and username of opponent
        for (document in gameDocuments) {
            val id = document.id
            val playerId = document.getString("player1")

            // Find the username of the player in the game to display
            val playerQuery = playerId?.let { db.collection("users").document(playerId).get() }
            val playerQuerySnapshot = playerQuery?.get()
            val playerName = playerQuerySnapshot?.getString("username")
            if (playerName != null) {
                games.add(GameListObject(id, playerId, playerName))
            }
        }
        return games
    }

    /**
     * Gets username from a registered player
     * @param userId the id of the user that should be added
     * @return a player object
     */
    fun getUser(userId: String): Player {
        val userQuery = userId.let { db.collection("users").document(userId).get() }
        val userQuerySnapshot = userQuery?.get()
        val username = userQuerySnapshot?.getString("username")
        if (username != null) {
            return Player(userId, username.toString())
        }
        return Player(userId, "Unknown")
    }

    /**
     * Add userId to a specific game
     * @param gameId the id of the game document
     * @param userId the id of the user that should be added
     */
    fun joinGame(gameId: String, userId: String): Boolean {
        // Add the data to the game document
        db.collection("games").document(gameId).update("player2", userId)
        // TODO sjekke om suksess og returnere true
        val game = db.collection("games").document(gameId).get()
        if (game.get().get("player2") == userId) return true
        return false
    }

    /**
     * Registers the ships on the board for a given user
     * @param gameId the id of the game document
     * @param userId the id of the user owning the ships
     * @param ships list containing the ships that should be added, each described using a map
     */
    fun registerTreasures(gameId: String, userId: String, treasures: List<Map<String, Any>>) {
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if (game.exists()) {
            val dbTreasures = game.get("treasures") as MutableMap<String, List<Map<String, Any>>>
            dbTreasures[userId] = treasures
            db.collection("games").document(gameId).update("treasures", dbTreasures)
        } else {
            // Add error handling
            println("Something went wrong when registering ships")
        }
    }

    /**
     * Get the ships in a game
     * @param gameId the id of the game
     * @return a Game object containing game and player
     */
    fun setGame(gameId: String) {
        val query = db.collection("games").document(gameId).get()
        val game = query.get()

        if (game.exists()) {
            val player1Id: String = game.get("player1") as String
            val player2Id: String = game.get("player2") as String

            val player1 = getUser(player1Id)
            val treasures = getTreasures(gameId)
            if (player1Id in treasures) treasures[player1Id]?.let { player1.board.setTreasuresList(it) }
            println("id er : " + (player2Id != ""))
            val player2 = if (player2Id != "") {
                getUser(player2Id)
            } else Player("", "")

            if (player2Id in treasures) treasures[player2Id]?.let { player2.board.setTreasuresList(it) }

            println("player1: ${player1.playerName}, player2: ${player2.playerName}")
            GSM.activeGame = Game(gameId, player1, player2)
        } else {
            throw error("Something went wrong when fetching the Game")
        }
    }

    /**
     * Get the ships in a game
     * @param gameId the id of the game
     * @return a Game object containing game and player
     */
    fun setOpponent(gameId: String) {
        val query = db.collection("games").document(gameId).get()
        val game = query.get()

        if (game.exists()) {
            val player2Id: String = game.get("player2") as String
            val treasures = getTreasures(gameId)
            val player2 = if (player2Id != "") {
                getUser(player2Id)
            } else Player("", "")

            if (player2Id in treasures) treasures[player2Id]?.let { player2.board.setTreasuresList(it) }

            println("opponent fetched: ${player2.playerName}")
            GSM.activeGame.initOpponent(player2)
        } else {
            throw error("Something went wrong when fetching the Game")
        }
    }

    /**
     * Get the treasures in a game
     * @param gameId the id of the game
     * @return a map containing a list of treasures per user
     */
    fun getTreasures(gameId: String): MutableMap<String, List<Map<String, Any>>> {
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if (game.exists()) {
            return game.get("treasures") as MutableMap<String, List<Map<String, Any>>>
        } else {
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
    fun makeMove(gameId: String, x: Float, y: Float, playerId: String, weapon: String) {
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
        } else {
            // Add error handling
            println("Something went wrong when making move")
        }
    }

    /**
     * Set the winner of the game
     * @param userId the id of the winner
     * @param gameId the id of the game document
     */
    fun setWinner(userId: String, gameId: String) {
        db.collection("games").document(gameId).update("winner", userId)
    }

    fun detachGameListener(gameId: String) {
        registration?.remove()
    }

    /**
     * Function adding listener to a specific game
     * TODO: Replace println with functionality connected to the cases
     * TODO: Add exception handling
     * @param gameId the id of the game document
     * @param playerId the id of the player
     */
    fun addGameListener(gameId: String) {

        val query = db.collection("games").document(gameId)
        registration = query.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
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
                        // addOpponentListener(gameId)
                    } //need to notify the other user if player2 has changed
                    // If there is an opponent in the game
                    else {
                        if(!GSM.activeGame.gameReady) {
                            setOpponent(gameId)
                            println("opponent is set: "+ opponent)
                        }
                        // Get the field containing the treasures in the database
                        val treasures = snapshot.data?.get("treasures") as MutableMap<String, List<Map<String, Any>>>

                        // If there is not enough treasures registered
                        if (treasures.size < 2) {
                            println("Treasures not registered")
                        } else {

                            // Get the list of moves
                            val moves = snapshot.data?.get("moves") as MutableList<Map<String, Any>>

                            val winner = snapshot.data?.get("winner")
                            // If a winner has been set
                            if (winner != "") {
                                println("The winner is $winner")
                                GSM.activeGame.winner = winner as String
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
                                    val game = GSM.activeGame
                                    if (lastMove["playerId"]!!.equals(game.opponent.playerId)) {
                                        println("Motstander hadde siste trekk - din tur")
                                        GSM.activeGame.flipPlayer()
                                    } else if (lastMove["playerId"]!!.equals(game.me.playerId)) {
                                        println("Du hadde siste trekk, vent")
                                        GSM.activeGame.flipPlayer()
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

    fun addOpponentListener(gameId: String) {
        val query = db.collection("games").document(gameId)
        val opponentListener = query.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(
                    @Nullable snapshot: DocumentSnapshot?,
                    @Nullable e: FirestoreException?
            ) {
                if (e != null) {
                    System.err.println("Listen failed: $e")
                    return
                }
                println("   OPPONENT LISTENER:")
                if (snapshot != null && snapshot.exists()) {
                    val opponent = snapshot.data?.get("player2")
                    // If no opponent has joined yet
                    if (opponent != "") {
                        println("Opponent HAS joined yet")
                        setGame(gameId)
                    }
                }
                // If no data is found
                else {
                    print("Current data: null")
                }
            }
        })
        opponentListener.remove()
    }
}
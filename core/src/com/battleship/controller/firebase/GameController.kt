package com.battleship.controller.firebase

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.EventListener
import com.google.cloud.firestore.FirestoreException
import com.google.firebase.database.annotations.Nullable


/**
 * Controller handling all database activity concerned with game flow
 */
@Suppress("UNCHECKED_CAST")
class GameController : FirebaseController(){

    /**
     * Start new game
     * @param userId the id of the user setting up the game
     */
    fun createGame(userId: String) : String{
        //Set up game data
        val data = mutableMapOf<String, Any>()
        data["player1"] = userId
        data["player2"] = ""
        data["winner"] = ""
        data["moves"] = mutableListOf<Map<String, Any>>()
        data["ships"] = mutableMapOf<String,List<Map<String, Any>>>()

        val res = db.collection("games").add(data)

        //Return the id of the game
        return res.get().id
    }

    /**
     * Function getting all games where there is currently only one player
     */
    fun getPendingGames() : Map<String, String>{
        val gameQuery = db.collection("games").whereEqualTo("player2", "").get()
        val gameQuerySnapshot = gameQuery.get()
        val gameDocuments = gameQuerySnapshot.documents
        val games = mutableMapOf<String, String>()

        //For each game fitting the criteria, get the id and username of opponent
        for (document in gameDocuments) {
            val id = document.id
            val playerId = document.getString("player1")

            //Find the username of the player in the game to display
            val playerQuery = playerId?.let { db.collection("users").document(playerId).get() }
            val playerQuerySnapshot = playerQuery?.get()
            val playerName = playerQuerySnapshot?.getString("username") as String

            games[id] = playerName
        }
        return games
    }

    /**
     * Add userId to a specific game
     * @param gameId the id of the game document
     * @param userId the id of the user that should be added
     */
    fun joinGame(gameId: String, userId: String) {
        //Add the data to the game document
        db.collection("games").document(gameId).update("player2", userId)
    }

    /**
     * Registers the ships on the board for a given user
     * @param gameId the id of the game document
     * @param userId the id of the user owning the ships
     * @param ships list containing the ships that should be added, each described using a map
     */
    fun registerShips(gameId: String, userId: String, ships: List<Map<String, Any>>){
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if(game.exists()){
            val dbShips=  game.get("ships") as MutableMap<String,List<Map<String, Any>>>
            dbShips[userId]=ships
            db.collection("games").document(gameId).update("ships", dbShips)
        }
        else{
            //Add error handling
            println("Something went wrong when registering ships")
        }
    }

    /**
     * Get the ships in a game
     * @param gameId the id of the game
     * @return a map containing a list of ships per user
     */
    fun getShips(gameId: String) : MutableMap<String,List<Map<String, Any>>>{
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if(game.exists()) {
            return game.get("ships") as MutableMap<String, List<Map<String, Any>>>
        }
        else{
            throw error("Something went wrong when fetching ships")
        }
    }

    /**
     * Registers the move
     * @param gameId the id of the game document
     * @param x x coordinate of move
     * @param y y coordinate of
     * @param playerId player making the move
     */
    fun makeMove(gameId: String, x: Int, y: Int, playerId: String){
        val query = db.collection("games").document(gameId).get()
        val game = query.get()
        if(game.exists()){
            val moves=  game.get("moves") as MutableList<Map<String, Any>>
            val data = mutableMapOf<String, Any>()
            data["x"]=x
            data["y"]=y
            data["playerId"]=playerId
            moves.add(data)
            db.collection("games").document(gameId).update("moves", moves)
        }
        else{
            //Add error handling
            println("Something went wrong when making move")
        }
    }

    /**
     * Set the winner of the game
     * @param userId the id of the winner
     * @param gameId the id of the game document
     */
    fun setWinner(userId: String, gameId: String){
        db.collection("games").document(gameId).update("winner", userId)
    }

    /**
     * Function adding listener to a specific game
     * TODO: Replace println's with functionality connected to the cases
     * TODO: Add exception handling
     * @param gameId the id of the game document
     * @param playerId the id of the player
     */
    fun addGameListener(gameId: String, playerId: String){
        val docRef = db.collection("games").document(gameId)
        docRef.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(@Nullable snapshot: DocumentSnapshot?,
                                 @Nullable e: FirestoreException?) {
                if (e != null) {
                    System.err.println("Listen failed: $e")
                    return
                }

                if (snapshot != null && snapshot.exists()) {
                    val opponent = snapshot.data?.get("player2")
                    //If no opponent has joined yet
                    if (opponent==""){
                        println("Opponent not joined yet")
                    }
                    //If there is an opponent in the game
                    else{
                        //Get the field containing the ships in the database
                        val ships = snapshot.data?.get("ships") as MutableMap<String,List<Map<String, Any>>>

                        //If there is not enough ships registered
                        if(ships.size < 2){
                            println("Ships not registered")
                        }

                        //Get the list of moves
                        val moves = snapshot.data?.get("moves") as MutableList<Map<String, Any>>

                        val winner = snapshot.data?.get("winner")
                        //If a winner has been set
                        if(winner!=""){
                            println("The winner is $winner")
                        }
                        //If there is no winner, continue game
                        else{
                            //If no moves has been made yet
                            if(moves.size == 0){
                                println("No moves made yet")
                            }
                            else {
                                //Get the last move
                                val lastMove = moves.get(moves.size - 1)
                                //If the last move is performed by opponent
                                if (!lastMove["playerId"]!!.equals(playerId)) {
                                    println("Motstander hadde siste trekk")
                                } else {
                                    println("Du hadde siste trekk, vent")
                                }
                            }
                        }
                    }
                }
                //If no data is found
                else {
                    print("Current data: null")
                }
            }
        })
    }
}

package com.battleship.controller.firebase

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.EventListener
import com.google.cloud.firestore.FirestoreException
import com.google.firebase.database.annotations.Nullable


/**
 * Controller handling all database activity concerned with game flow
 */
class GameController : FirebaseController(){

    /**
     * Start new game
     * @param userId: the id of the user setting up the game
     */
    fun createGame(userId: String) : String{
        //Set up game data
        val data = mutableMapOf<String, Any>()
        data["player1"] = userId
        data["player2"] = ""
        data["moves"] = mutableListOf<Map<String, Any>>()
        data["winner"] = ""

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
     * @param gameId: the id of the game document
     * @param userId: the id of the user that should be added
     */
    fun joinGame(gameId: String, userId: String) {
        //Add the data to the game document
        db.collection("games").document(gameId).update("player2", userId)
    }

    /**
     * Registers the move
     * @param gameId: the id of the game document
     * @param x: x coordinate of move
     * @param y: y coordinate of
     * @param playerId: player making the move
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
     * @param userId: the id of the winner
     * @param gameId: the id of the game document
     */
    fun setWinner(userId: String, gameId: String){
        db.collection("games").document(gameId).update("winner", userId)
    }

    /**
     * Function adding listener to specific game
     * TO DO Replace println's with functionality connected to the cases
     * @param gameId: the id of the game document
     * @param playerId: the id of the player
     */
    fun addGameListener(gameId: String, playerId: String){
        val docRef = db.collection("games").document(gameId)
        docRef.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(@Nullable snapshot: DocumentSnapshot?,
                                 @Nullable e: FirestoreException?) {
                if (e != null) {
                    //TO DO add error handling
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
                        val moves = snapshot.data?.get("moves") as MutableList<Map<String, Any>>
                        val winner = snapshot.data?.get("winner")
                        //If a winner has been set
                        if(winner!=""){
                            println("The winner is $winner")
                        }
                        //If there is no winner, continue game
                        else{
                            //Get the last move
                            val lastMove = moves.get(moves.size-1)
                            //If the last move is performed by opponent
                            if(!lastMove["playerId"]!!.equals(playerId)){
                                println("Motstander hadde siste trekk")
                            }
                            else{
                                println("Du hadde siste trekk, vent")
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

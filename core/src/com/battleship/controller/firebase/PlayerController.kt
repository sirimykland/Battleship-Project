package com.battleship.controller.firebase

/**
 * Controller handling all database activity concerned with player management
 */
class PlayerController : FirebaseController(){

    /**
     * Get all the players registered in the database
     */
    fun getPlayers(): MutableMap<String, String?> {
        val query = db.collection("users").get()
        val querySnapshot = query.get()
        val documents = querySnapshot.documents
        val playerMap = mutableMapOf<String, String?>()

        for (document in documents) {
            val id = document.getId()
            val name = document.getString("username")
            playerMap[id] = name
        }
        return playerMap
    }
}

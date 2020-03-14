package com.battleship.controller.firebase

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.OutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection


/**
 * Controller handling all database activity concerned with player management
 */
class PlayerController : FirebaseController(){

    /**
     * Get all the players registered in the database
     * @return a map containing user id and username
     */
    fun getPlayers(): MutableMap<String, String?> {
        val query = db.collection("users").get()
        val querySnapshot = query.get()
        val documents = querySnapshot.documents
        val playerMap = mutableMapOf<String, String?>()

        for (document in documents) {
            val id = document.id
            val name = document.getString("username")
            playerMap[id] = name
        }
        return playerMap
    }

    /**
     * Register new player in the db
     * @param username: the username wanted
     * @return id of player
     */
    fun addPlayer(username : String) : String{
        //Add the data to a Map for pushing to db
        val data = mutableMapOf<String, Any>()
        data["username"] = username;

        //Push to db
        val addRes = db.collection("users").add(data)

        return addRes.get().id
    }
}

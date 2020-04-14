package com.battleship.controller.firebase

interface FirebaseController {

    fun getPlayers()
    fun addPlayer(username: String)
    fun createGame(userId: String)
    fun getPendingGames()
    fun joinGame(gameId: String, userId: String)
    fun registerTreasures(gameId: String, userId: String, treasures: List<Map<String, Any>>)
    fun getTreasures(gameId: String)
    fun makeMove(gameId: String, x: Int, y: Int, playerId: String, weapon: String)
    fun setWinner(userId: String, gameId: String)
    fun addGameListener(gameId: String, playerId: String)

}
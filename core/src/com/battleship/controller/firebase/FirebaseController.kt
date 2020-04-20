package com.battleship.controller.firebase

interface FirebaseController {
    fun createGame(userId: String, userName: String)
    fun joinGame(gameId: String, userId: String, userName: String)
    fun registerTreasures(gameId: String, userId: String, treasures: List<Map<String, Any>>)
    fun registerMove(gameId: String, x: Int, y: Int, playerId: String, weapon: String)
    fun setWinner(userId: String, gameId: String)
    fun addPendingGamesListener()
    fun addGameListener(gameId: String, playerId: String)
    fun addPlayListener(gameId: String)
}

package com.battleship.controller.firebase

import com.battleship.model.Game
import com.battleship.model.GameListObject

interface FirebaseController {
    fun createGame(userId: String, userName: String, callback: (game: Game) -> Unit)
    fun joinGame(gameId: String, userId: String, userName: String, callback: (game: Game) -> Unit)
    fun registerTreasures(gameId: String, userId: String, treasures: List<Map<String, Any>>)
    fun registerMove(gameId: String, x: Int, y: Int, playerId: String, equipment: String)
    fun setWinner(userId: String, gameId: String)
    fun addPendingGamesListener(callback: (pendingGames: ArrayList<GameListObject>) -> Unit)
    fun addGameListener(gameId: String, playerId: String)
    fun addPlayListener(gameId: String)
}

package com.battleship.model

import com.battleship.GameStateManager

class Game(val gameId: String) {
    // val boardSize: Int? = 10
    var player1 = Player()
    var player2 = Player()

    constructor (gameId: String, playerId: String, playerName: String) : this(gameId) {
        this.player1 = Player(playerId, playerName)
    }

    constructor (gameId: String, player1: Player, player2: Player) : this(gameId) {
        this.player1 = player1
        this.player2 = player2
    }

    fun getMe(): Player {
        if (player1.playerId.equals(GameStateManager.userId))
            return player1
        else if (player2.playerId.equals(GameStateManager.userId))
            return player2
        else
            return Player("errorMe", "Invalid")
    }

    fun getOpponent(): Player {
        if (player1.playerId.equals(GameStateManager.userId))
            return player2
        else if (player2.playerId.equals(GameStateManager.userId))
            return player1
        else
            return Player("errorOppnent", "Invalid")
    }
}
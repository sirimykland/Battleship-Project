package com.battleship.model

import com.battleship.GSM

class Game(val gameId: String, var player1: Player, var player2: Player) {
    var activePlayer = player2 // must be made dynamically
    var winner: String = ""

    fun flipPlayer() {
        if (activePlayer == player1) activePlayer = player2
        else if (activePlayer == player2) activePlayer = player1
    }

    fun getMe(): Player {
        println("getMe(): " + player1.playerName + " - " + player2.playerName)
        if (player1.playerId == GSM.userId)
            return player1
        else if (player2.playerId == GSM.userId)
            return player2
        else
            return Player("errorMe", "InvalidMe")
    }

    fun isMyTurn(): Boolean {
        // println("isMyTurn() ")
        return activePlayer.playerId == GSM.userId
    }

    fun getOpponent(): Player {
        println("getOpponent(): " + player1.playerName + "  " + player2.playerName)
        if (player1.playerId == GSM.userId)
            return player2
        else if (player2.playerId == GSM.userId)
            return player1
        else
            return Player("errorOppnent", "Invalidopponnet")
    }
}
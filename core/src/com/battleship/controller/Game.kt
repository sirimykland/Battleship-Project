package com.battleship.controller

class Game(val gameId: String, playerId: String, playerName: String) {
    val player1: Player = Player(playerId, playerName)
    var player2: Player? = null

    // TODO use official Player Class
    class Player(val id: String, val name: String)
}
package com.battleship.model

import com.battleship.model.weapons.WeaponSet

class Player() {
    var boardSize: Int = 10
    var playerId: String = ""
    var playerName: String = ""

    constructor(playerId: String, playerName: String) : this() {
        this.playerId = playerId
        this.playerName = playerName
    }

    var weaponSet = WeaponSet()
    var board: Board = Board(boardSize)
    var health: Int = board.getAllShipHealth()

    fun updateHealth() {
        this.health = board.getAllShipHealth()
    }

    override fun toString(): String {
        return "Player(playerId='$playerId', playerName='$playerName', board=$board)"
    }
}

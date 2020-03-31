package com.battleship.model

import com.battleship.model.weapons.BigWeapon
import com.battleship.model.weapons.RadarWeapon
import com.battleship.model.weapons.SmallWeapon
import com.battleship.model.weapons.WeaponSet

class Player(var playerId: String = "", var playerName: String = "") {
    val boardSize: Int = 10
    var weaponSet = WeaponSet(arrayListOf(SmallWeapon(), BigWeapon(), RadarWeapon()))
    var board: Board = Board(boardSize)
    var health: Int = board.getAllShipHealth()


    fun updateHealth() {
        this.health = board.getAllShipHealth()
    }

    override fun toString(): String {
        return "Player(playerId='$playerId', playerName='$playerName', board=$board)"
    }
}

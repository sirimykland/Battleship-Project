package com.battleship.model

import com.battleship.model.equipment.EquipmentSet
import com.battleship.model.weapons.BigWeapon
import com.battleship.model.weapons.RadarWeapon
import com.battleship.model.weapons.SmallWeapon
import com.battleship.model.weapons.WeaponSet

class Player(var playerId: String = "", var playerName: String = "") {
    val boardSize: Int = 10
    var equipmentSet = EquipmentSet(arrayListOf(SmallWeapon(), BigWeapon(), RadarWeapon()))
    var board: Board = Board(boardSize)
    var health: Int = board.getAllTreasueHealth()

    fun updateHealth() {
        this.health = board.getAllTreasueHealth()
    }

    override fun toString(): String {
        return "Player(playerId='$playerId', playerName='$playerName', board=$board)"
    }
}

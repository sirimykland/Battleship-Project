package com.battleship.model

import com.battleship.model.equipment.EquipmentSet
import com.battleship.model.equipment.*

class Player(var playerId: String = "", var playerName: String = "") {
    val boardSize: Int = 10
    var equipmentSet = EquipmentSet(arrayListOf(Shovel(), BigEquipment(), MetalDetector()))
    var board: Board = Board(boardSize)
    var health: Int = board.getCombinedTreasureHealth()

    fun updateHealth() {
        health = board.getCombinedTreasureHealth()
    }

    override fun toString(): String {
        return "Player(playerId='$playerId', playerName='$playerName', board=$board)"
    }
}

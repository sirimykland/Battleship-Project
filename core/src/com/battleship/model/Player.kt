package com.battleship.model

import com.battleship.model.equipment.EquipmentSet

class Player(boardSize: Int) {
    // var id:Int
    // var name:String
    var equipmentSet = EquipmentSet()
    var board: Board = Board(boardSize)
    var health: Int = board.getAllTreasureHealth()

    fun updateHealth() {
        health = board.getAllTreasureHealth()
    }
}

package com.battleship.model

import com.battleship.model.weapons.WeaponSet

class Player(boardSize: Int) {
    // var id:Int
    // var name:String
    var weaponSet = WeaponSet()
    var board: Board = Board(boardSize)
    var health: Int = board.getAllShipHealth()

    fun updateHealth() {
        this.health = board.getAllShipHealth()
    }
}

package com.battleship.model

import com.battleship.model.weapons.WeaponSet

class Player(boardSize: Int) {
    // var id:Int
    // var name:String
    var health: Int = 100
    var weaponSet = WeaponSet()
    var board: Board = Board(boardSize)

    fun updateHealth() {
        this.health = board.getAllShipHealth()
    }
}

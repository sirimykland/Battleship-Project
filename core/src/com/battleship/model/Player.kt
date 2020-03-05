package com.battleship.model

import com.battleship.model.weapons.WeaponSet

class Player(boardSize: Int) {
    var weaponSet = WeaponSet()
    var board: Board = Board(boardSize)
}
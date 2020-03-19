package com.battleship.model.weapons

class BigWeapon : Weapon() {
    override var damage: Int = 1
    override var radius: Int = 1
    override var ammunition: Int = 10
    override var name: String = "Big Weapon"
    override var active: Boolean = false
}
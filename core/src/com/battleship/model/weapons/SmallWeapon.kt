package com.battleship.model.weapons

class SmallWeapon : Weapon() {
    override var damage: Int = 1
    override var radius: Int = 1
    override var ammunition: Int = 10
    override var name: String = "Small Weapon"
    override var active: Boolean = false
}

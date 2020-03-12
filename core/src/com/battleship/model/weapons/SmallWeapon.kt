package com.battleship.model.weapons

class SmallWeapon : Weapon() {
    override var damage: Int = 1
    override var radius: Int = 0
    override var ammunition: Int = 100
    override var name: String = "Small Weapon"
    override var active: Boolean = false
}

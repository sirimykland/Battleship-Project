package com.battleship.model.weapons

class RadarWeapon : Weapon() {
    override var damage: Int = 0
    override var radius: Int = 1
    override var ammunition: Int = 1
    override var name: String = "RadarWeapon"
    override var active: Boolean = false
}

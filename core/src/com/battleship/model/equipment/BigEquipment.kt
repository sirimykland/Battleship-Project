package com.battleship.model.equipment

class BigEquipment : Equipment() {
    override var searchRadius: Int = 1
    override var uses: Int = 10
    override var name: String = "Big equipment"
    override var active: Boolean = false
}

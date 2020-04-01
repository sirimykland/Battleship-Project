package com.battleship.model.equipment

class Shovel : Equipment() {
    override var searchRadius: Int = 0
    override var uses: Int = 100
    override var name: String = "Shovel"
    override var active: Boolean = false
}

package com.battleship.model.equipment

class MetalDetector : Equipment() {
    override var searchRadius: Int = 1
    override var uses: Int = 1
    override var name: String = "Metal detector"
    override var active: Boolean = false
}

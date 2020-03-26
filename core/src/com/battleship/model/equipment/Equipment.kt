package com.battleship.model.equipment

import com.battleship.model.GameObject

abstract class Equipment : GameObject() {
    abstract var searchRadius: Int
    abstract var uses: Int
    abstract var name: String
    abstract var active: Boolean

    fun hasMoreUses(): Boolean {
        return uses > 0
    }

    fun use() {
        uses--
    }
}

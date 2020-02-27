package com.battleship.model.buttons

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.battleship.model.GameObject

abstract class Button : GameObject() {
    abstract val btn: TextButton

    abstract fun addListener()

    fun addToTable(table: Table) {
        table.add(btn)
        table.row()
    }
}

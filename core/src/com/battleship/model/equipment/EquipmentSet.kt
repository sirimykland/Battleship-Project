package com.battleship.model.equipment

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject
import com.battleship.model.ui.GuiObject
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette

class EquipmentSet : GameObject() {
    var equipments: ArrayList<Equipment> = ArrayList()
    var activeEquipment: Equipment? = null
    private lateinit var equipmentButtons: Array<GuiObject>

    override fun draw(batch: SpriteBatch, position: Vector2, dimension: Vector2) {
        equipmentButtons = arrayOf(*(0 until equipments.size).map { a: Int ->
            joinEquipmentButton(
                a,
                position,
                dimension
            )
        }.toTypedArray())
        Gdx.input.inputProcessor =
            InputMultiplexer(*equipmentButtons.filter { it.isClickable }.map { it.listener }.toTypedArray())

        batch.begin()
        equipmentButtons.forEach {
            it.draw(batch)
        }
        batch.end()
    }

    fun setEquipmentActive(equipment: Equipment) {
        this.activeEquipment?.active = false
        this.activeEquipment = equipment
        this.activeEquipment?.active = true
        println(this.activeEquipment?.name + " satt aktiv")
    }

    private fun joinEquipmentButton(
        index: Int,
        position: Vector2,
        dimension: Vector2
    ): GuiObject {
        val equipment = equipments[index]

        var borderColor = Palette.LIGHT_GREY
        if (equipment.active) {
            borderColor = Palette.GREEN
        }

        return GUI.textButton(
<<<<<<< HEAD:core/src/com/battleship/model/equipment/EquipmentSet.kt
            position.x + dimension.x / equipments.size * index + index * 1,
=======
            position.x + dimension.x / equipments.size * index + index * 2,
>>>>>>> 01d3cd4701192492c25f2815a6f1289b330be360:core/src/com/battleship/model/equipment/EquipmentSet.kt
            position.y,
            dimension.x / equipments.size,
            dimension.y,
            equipment.name + " " + equipment.uses,
            font = Font.TINY_BLACK,
            color = Palette.GREY,
            borderColor = borderColor
        ) {
            setEquipmentActive(equipment)
        }
    }
}

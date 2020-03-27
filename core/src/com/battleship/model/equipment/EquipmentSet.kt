package com.battleship.model.equipment

import com.battleship.model.GameObject

class EquipmentSet : GameObject() {
    var equipments: ArrayList<Equipment> = ArrayList()
    var activeEquipment: Equipment? = null
    //private lateinit var equipmentButtons: Array<GuiObject>

    init {
        equipments.add(Shovel())
        equipments.add(BigEquipment())
        equipments.add(MetalDetector())
        setEquipmentActive(equipments.first())
    }

    fun setEquipmentActive(equipment: Equipment) {
        this.activeEquipment?.active = false
        this.activeEquipment = equipment
        this.activeEquipment?.active = true
        println(this.activeEquipment?.name + " satt aktiv")
    }
    /*
    override fun draw(batch: SpriteBatch, position: Vector2, dimension: Vector2) {
        equipmentButtons = arrayOf(*(0 until equipments.size).map { a: Int ->
            joinWeaponButton(
                a,
                position,
                dimension
            )
        }.toTypedArray())
        //Gdx.input.inputProcessor = InputMultiplexer(*equipmentButtons.filter { it.isClickable }.map { it.listener }.toTypedArray())

        batch.begin()
        equipmentButtons.forEach {
            it.draw(batch)
        }
        batch.end()
    }



    private fun joinWeaponButton(
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
            position.x + dimension.x / equipments.size * index + index * 2,
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

     */
}

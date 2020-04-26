package com.battleship.model.equipment

import com.battleship.model.GameObject

/**
 * EquipmentSet inherits from [GameObject].
 * Manages a list of equipment
 */
class EquipmentSet : GameObject() {
    var equipments: ArrayList<Equipment> = ArrayList()
    var activeEquipment: Equipment? = null
        set(equipment) {
            this.activeEquipment?.active = false
            field = equipment
            this.activeEquipment?.active = true
        }

    /**
     * Initialization block adds equipments to the equipments list,
     * and sets first element as the active equipment.
     */
    init {
        equipments.add(Shovel())
        equipments.add(Dynamite())
        activeEquipment = equipments.first()
    }
}

package com.battleship.model.equipment

import com.battleship.model.GameObject

class EquipmentSet(var equipments: ArrayList<Equipment>) : GameObject() {
    // var equipments: ArrayList<Equipment> = ArrayList()
    var activeEquipment: Equipment? = null
        set(equipment) {
            this.activeEquipment?.active = false
            field = equipment
            this.activeEquipment?.active = true
        }

    init {
        equipments.add(Shovel())
        equipments.add(BigEquipment())
        activeEquipment = equipments.first()
    }
}

package com.battleship.model.weapons

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject
import com.battleship.model.ui.GuiObject
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette

class WeaponSet : GameObject() {
    var weapons: ArrayList<Weapon> = ArrayList()
    var weapon: Weapon? = null
    private lateinit var weaponButtons: Array<GuiObject>

    override fun draw(batch: SpriteBatch, position: Vector2, dimension: Vector2) {
        weaponButtons = arrayOf(*(0 until weapons.size).map { a: Int ->
            joinWeaponButton(
                a,
                position,
                dimension
            )
        }.toTypedArray())
        Gdx.input.inputProcessor =
            InputMultiplexer(*weaponButtons.filter { it.hasListener }.map { it.listener }.toTypedArray())

        batch.begin()
        weaponButtons.forEach {
            it.draw(batch)
        }
        batch.end()
    }

    fun setActiveWeapon(weapon: Weapon) {
        this.weapon?.active = false
        this.weapon = weapon
        this.weapon?.active = true
        println(this.weapon?.name + " satt aktiv")
    }

    private fun joinWeaponButton(
        index: Int,
        position: Vector2,
        dimension: Vector2
    ): GuiObject {
        val weapon = weapons[index]

        var borderColor = Palette.LIGHT_GREY
        if (weapon.active) {
            borderColor = Palette.GREEN
        }

        return GUI.textButton(
            position.x + dimension.x / weapons.size * index + index * 2,
            position.y,
            dimension.x / weapons.size,
            dimension.y,
            weapon.name + " " + weapon.ammunition,
            font = Font.TINY_BLACK,
            color = Palette.GREY,
            borderColor = borderColor
        ) {
            setActiveWeapon(weapon)
        }
    }
}

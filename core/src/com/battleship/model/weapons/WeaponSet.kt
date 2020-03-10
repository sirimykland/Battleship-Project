package com.battleship.model.weapons

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.battleship.model.GameObject

class WeaponSet : GameObject() {
    var weapons: ArrayList<Weapon> = ArrayList()
    var weapon: Weapon? = null
    var stage: Stage = Stage()

    override fun draw(batch: SpriteBatch, position: Vector2, dimension: Vector2) {
        /*
        var amount = weapons.size
        var tileSize = dimension.x / amount
        var x = position.x
        var y = position.y

        for (weapon in weapons) {
            weapon.draw(batch, Vector2(x, y), Vector2(tileSize, dimension.y))
            // Padding?
            x += tileSize + 1
        }
        */
        buildWeaponMenu(position, dimension)
        stage.act()
        stage.draw()
    }

    fun setActiveWeapon(weapon: Weapon) {
        this.weapon?.active = false
        this.weapon = weapon
        this.weapon?.active = true
        println(this.weapon?.name + " satt aktiv")
    }

    fun buildWeaponMenu(position: Vector2, dimension: Vector2) {
        val menuTable = Table()
        menuTable.setPosition(position.x, position.y)
        menuTable.setSize(dimension.x, dimension.y)
        menuTable.align(Align.center)

        val font = BitmapFont()
        val style = TextButtonStyle()
        style.font = font
        style.fontColor = Color.WHITE

        for (weapon in weapons) {
            val button: TextButton = TextButton(weapon.name, style)
            button.addListener(object : ClickListener() {
                override fun clicked(
                    event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                    x: Float,
                    y: Float
                ) {
                    setActiveWeapon(weapon)
                }
            })
            menuTable.add(button).pad(10f)
        }

        stage.addActor(menuTable)
        Gdx.input.setInputProcessor(stage)
    }
}

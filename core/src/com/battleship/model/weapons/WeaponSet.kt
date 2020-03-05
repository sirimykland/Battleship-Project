package com.battleship.model.weapons

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject

class WeaponSet : GameObject() {
    var weapons: ArrayList<Weapon> = ArrayList()

    override fun draw(batch: SpriteBatch, position: Vector2, dimension: Vector2) {
        var amount = weapons.size
        var tileSize = dimension.x / amount
        var x = position.x
        var y = position.y

        for (weapon in weapons) {
            weapon.draw(batch, Vector2(x, y), Vector2(tileSize, dimension.y))
            // Padding?
            x += tileSize + 1
        }
    }
}
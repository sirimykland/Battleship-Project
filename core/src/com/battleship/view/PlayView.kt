package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject
import com.battleship.model.equipment.EquipmentSet
import com.battleship.model.ui.Board
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.equipmentSetPosition
import com.battleship.utility.GdxGraphicsUtil.equipmentSetSize

/**
 * Subclass of [View] that renderes gui and game objects to screen
 */
class PlayView : View() {

    /**
     * Renders game and gui related such as text, buttons, board, etc.
     * Objects are drawn onto the screen using the gameobjects' draw method.
     * The individual draw methods are provided with parameters such as
     * SpritBatch, SpriteRendrer, position and size. The parameters size
     * and position are relevant in size to the screen rendered on.
     *
     * @param gameObjects: GameObject - variable length of drawable
     * objects to render
     */
    override fun render(vararg gameObjects: GameObject) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        background.setCenter(Gdx.graphics.width / 1.85f, Gdx.graphics.height.toFloat() / 2)
        background.draw(batch)
        batch.end()

        for (obj in gameObjects) {
            when (obj) {
                is Board -> obj.draw(
                    batch,
                    shapeRenderer,
                    Gdx.graphics.boardPosition(),
                    Vector2(Gdx.graphics.boardWidth(), Gdx.graphics.boardWidth())
                )
                is EquipmentSet -> obj.draw(
                    batch,
                    Gdx.graphics.equipmentSetPosition(),
                    Gdx.graphics.equipmentSetSize()
                )
                is GuiObject -> {
                    batch.begin()
                    obj.draw(batch)
                    batch.end()
                }
            }
        }
    }
}

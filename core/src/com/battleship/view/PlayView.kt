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

class PlayView : View() {
    override fun render(vararg gameObjects: GameObject) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(
            background,
            Gdx.graphics.width - background.width * backgroundScale,
            Gdx.graphics.height - background.height * backgroundScale,
            background.width * backgroundScale,
            background.height * backgroundScale
            )
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

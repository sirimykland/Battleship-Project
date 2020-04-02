package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.battleship.model.Board
import com.battleship.model.GameObject
import com.battleship.model.equipment.EquipmentSet
import com.battleship.model.ui.GameInfo
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.gameInfoPosition
import com.battleship.utility.GdxGraphicsUtil.gameInfoSize
import com.battleship.utility.GdxGraphicsUtil.equipmentsetPosition
import com.battleship.utility.GdxGraphicsUtil.equipmentsetSize

class PlayView : View() {
    override fun render(vararg gameObjects: GameObject) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        var background = Sprite(Texture("images/background.png"))

        batch.begin()
        background.setCenter(Gdx.graphics.width / 1.85f, Gdx.graphics.height.toFloat())
        background.draw(batch)
        batch.end()

        for (obj in gameObjects) {
            // TODO: Draw board background
            when (obj) {
                is Board -> obj.draw(
                    batch,
                    Gdx.graphics.boardPosition(),
                    Vector2(Gdx.graphics.boardWidth(), Gdx.graphics.boardWidth())
                )
                is EquipmentSet -> obj.draw(
                    batch,
                    Gdx.graphics.equipmentsetPosition(),
                    Gdx.graphics.equipmentsetSize()
                )
                is GameInfo -> obj.draw(
                    batch,
                    Gdx.graphics.gameInfoPosition(),
                    Gdx.graphics.gameInfoSize()
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

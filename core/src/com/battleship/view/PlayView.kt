package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.battleship.model.Board
import com.battleship.model.GameObject
import com.battleship.model.weapons.WeaponSet
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.weaponsetPosition
import com.battleship.utility.GdxGraphicsUtil.weaponsetSize

class PlayView() : View() {
    /*
     *  uses com.battleship.utility.GdxGraphicsUtil.*

    override fun render(board: Board, weaponSet: WeaponSet) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameObject.draw(batch,
                Gdx.graphics.boardPosition(), Gdx.graphics.boardWidth())
        weaponSet.draw(batch, Vector2(0f,0f), Gdx.graphics.boardWidth())
    }

     */

    override fun render(vararg gameObject: GameObject) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        for (obj in gameObject) {
            when (obj) {
                is Board -> obj.draw(
                    batch,
                    Gdx.graphics.boardPosition(),
                    Vector2(Gdx.graphics.boardWidth(), Gdx.graphics.boardWidth())
                )
                is WeaponSet -> obj.draw(
                    batch,
                    Gdx.graphics.weaponsetPosition(),
                    Gdx.graphics.weaponsetSize()
                )
            }
        }
    }
}

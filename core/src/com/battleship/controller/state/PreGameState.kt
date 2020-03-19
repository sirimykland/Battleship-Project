package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameInfo
import com.battleship.model.Player
import com.battleship.model.weapons.SmallWeapon
import com.battleship.utility.GdxGraphicsUtil.boardRectangle
import com.battleship.utility.GdxGraphicsUtil.size
import com.battleship.view.PlayView
import com.battleship.view.View


class PreGameState : State() {
    override var view: View = PlayView()
    var boardSize = 10
    var player: Player = Player(boardSize)
    var gameInfo = GameInfo(player)

    override fun create() {
        player.board.randomPlacement(4)
        player.board.ships.first().rotateShip()
        player.weaponSet.weapons.add(SmallWeapon())
        player.weaponSet.weapons.add(SmallWeapon())
        player.weaponSet.weapons.add(SmallWeapon())
        player.weaponSet.weapons.first().active = true
    }

    override fun render() {
        this.view.render(player.board, player.weaponSet, gameInfo)
    }

    override fun update(dt: Float) {
        handleInput()
    }

    /*
     * uses com.battleship.utility.CoordinateUtil.*
     *  and com.battleship.utility.GdxGraphicsUtil.*
     */
    fun handleInput() {
        //drag ship / set position relative to global
        if (Gdx.input.justTouched()) {
            println("touch ")
            val touchPos =
                    Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat())
            val screenSize = Gdx.graphics.size()
            // if input on board
            if (Gdx.graphics.boardRectangle().contains(touchPos)) {
                // if input on ship
                println("   on board")
                //if(player.board.validateShipPosition(touchPos.toCoordinate(boardPos, Gdx.graphics.boardWidth(), boardSize)))
            }
        }
        //if (Gdx.input.)
    }
}
package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.firebase.UpdatePlayData
import com.battleship.model.GameInfo
import com.battleship.model.Player
import com.battleship.model.weapons.BigWeapon
import com.battleship.model.weapons.SmallWeapon
import com.battleship.utility.CoordinateUtil.toCoordinate
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.view.PlayView
import com.battleship.view.View

class PlayState : State() {
    override var view: View = PlayView()
    override var firebaseController: FirebaseController = UpdatePlayData()
    var boardSize = 10
    var player: Player = Player(boardSize)
    var gameInfo = GameInfo(player)

    override fun create() {
        player.board.addSmallShip(3, 2)
        player.board.addMediumShip(4, 4)
        player.weaponSet.weapons.add(SmallWeapon())
        player.weaponSet.weapons.add(BigWeapon())
        player.weaponSet.setActiveWeapon(player.weaponSet.weapons.last())
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
        if (Gdx.input.justTouched()) {
            val touchPos =
                Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat())
            val boardWidth = Gdx.graphics.boardWidth()
            val boardPos = Gdx.graphics.boardPosition()
            val boardBounds = Rectangle(boardPos.x, boardPos.y, boardWidth, boardWidth)
            if (boardBounds.contains(touchPos)) {
                player.board.updateTile(touchPos.toCoordinate(boardPos, boardWidth, boardSize))
            }
        }
    }
}

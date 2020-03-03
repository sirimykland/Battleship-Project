package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.firebase.UpdatePlayData
import com.battleship.model.Player
import com.battleship.view.PlayView
import com.battleship.view.View

class PlayState : State() {
    override var view: View = PlayView()
    override var firebaseController: FirebaseController = UpdatePlayData()
    var boardSize = 10
    var player: Player = Player(boardSize)

    override fun create() {
        player.board.addSmallShip(2, 2)
        player.board.addMediumShip(4, 4)
    }

    override fun render() {
        this.view.render(player.board)
        handleInput()
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    fun handleInput() {
        if (Gdx.input.justTouched()) {
            val touchPos =
                Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat())
            var boardWidth = Gdx.graphics.width.toFloat() * 0.9f
            val boardPos = Vector2(
                Gdx.graphics.width.toFloat() * 0.05f,
                Gdx.graphics.height / 2f - boardWidth / 2f
            )
            var rect = Rectangle(boardPos.x, boardPos.y, boardWidth, boardWidth)
            if (rect.contains(touchPos)) {
                player.board.updateTile(convertCoordinate(touchPos, boardPos, boardWidth))
            }
        }
    }

    fun convertCoordinate(touch: Vector2, boardPos: Vector2, boardWidth: Float): Vector2 {
        var tileSize = boardWidth / boardSize
        var tileX = (touch.x - boardPos.x) / tileSize
        var tileY = (touch.y - boardPos.y) / tileSize

        println("(" + tileX.toInt() + ", " + tileY.toInt() + ")")
        return Vector2(tileY, tileX)
    }
}

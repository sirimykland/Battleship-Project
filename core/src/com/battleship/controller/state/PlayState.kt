package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.battleship.GameStateManager
import com.battleship.controller.firebase.GameController
import com.battleship.model.ui.GuiObject
import com.battleship.model.weapons.BigWeapon
import com.battleship.model.weapons.RadarWeapon
import com.battleship.model.weapons.SmallWeapon
import com.battleship.utility.CoordinateUtil.toCoordinate
import com.battleship.utility.GUI
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.gameInfoPosition
import com.battleship.utility.GdxGraphicsUtil.gameInfoSize
import com.battleship.view.PlayView
import com.battleship.view.View

class PlayState() : GuiState() {
    override var view: View = PlayView()
    var boardSize = 10
    // var player: Player = Player()
    private val gameController = GameController()
    private var activePlayer = GameStateManager.activeGame.getOpponent()

    private val testText = GUI.text(
            Gdx.graphics.gameInfoPosition().x,
            Gdx.graphics.gameInfoPosition().y,
            Gdx.graphics.gameInfoSize().x,
            Gdx.graphics.gameInfoSize().y,
            activePlayer.playerName)
    override val guiObjects: List<GuiObject> = listOf(
            testText
    )

    override fun create() {
        super.create()
        // kan kanskje flyttes til GameController init block
        gameController.addGameListener(GameStateManager.activeGame.gameId, GameStateManager.userId)

        activePlayer.weaponSet.weapons.add(SmallWeapon())
        activePlayer.weaponSet.weapons.add(BigWeapon())
        activePlayer.weaponSet.weapons.add(RadarWeapon())
        activePlayer.weaponSet.setActiveWeapon(activePlayer.weaponSet.weapons.first())
    }

    override fun render() {
        this.view.render(*guiObjects.toTypedArray(), activePlayer.board, activePlayer.weaponSet) // , gameInfo)
    }

    override fun update(dt: Float) {
        handleInput()
        activePlayer.updateHealth()
        if (activePlayer.health == 0) {
            println("You won!")
            GameStateManager.set(MainMenuState())
        }
    }

    fun handleInput() {
        if (Gdx.input.justTouched()) {
            val touchPos =
                    Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat())
            val boardWidth = Gdx.graphics.boardWidth()
            val boardPos = Gdx.graphics.boardPosition()
            val boardBounds = Rectangle(boardPos.x, boardPos.y, boardWidth, boardWidth)
            if (boardBounds.contains(touchPos)) {
                val boardTouchPos = touchPos.toCoordinate(boardPos, boardWidth, boardSize)
                if (activePlayer.weaponSet.weapon!!.hasAmmunition()) {
                    activePlayer.board.shootTiles(boardTouchPos, activePlayer.weaponSet.weapon!!)
                    activePlayer.weaponSet.weapon!!.shoot()
                } else {
                    println(activePlayer.weaponSet.weapon!!.name + "Has no ammo")
                }
            }
        }
    }
}

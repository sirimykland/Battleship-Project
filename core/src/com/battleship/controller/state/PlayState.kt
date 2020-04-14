package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.battleship.GameStateManager
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.Board
import com.battleship.model.Player
import com.battleship.model.treasures.Treasure.TreasureType
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.CoordinateUtil.toCoordinate
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.equipmentSetPosition
import com.battleship.utility.GdxGraphicsUtil.equipmentSetSize
import com.battleship.utility.Palette
import com.battleship.view.PlayView
import com.battleship.view.View
import java.util.*
import kotlin.concurrent.schedule

class PlayState(private val controller : FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()
    private var boardSize = 10
    private var player: Player = Player(boardSize)
    private var opponent: Player = Player(boardSize)
    private var playerBoard: Boolean = false
    private var playerTurn: Boolean = true
    private var newTurn: Boolean = false

    private val header = GUI.header("Your turn")

    private val equipmentButtons: Array<GuiObject> =
        arrayOf(*(0 until player.equipmentSet.equipments.size).map { a: Int ->
            joinEquipmentButton(
                a,
                Gdx.graphics.equipmentSetPosition(),
                Gdx.graphics.equipmentSetSize()
            )
        }.toTypedArray())

    private val switchBoardButton = GUI.imageButton(
        87f,
        90f,
        8f,
        8f,
        "icons/refresh_white.png",
        onClick = {
            playerBoard = !playerBoard
        }
    )
    private val opponentsBoardText = GUI.text(
        5f,
        2f,
        90f,
        10f,
        "Opponent's board",
        font = Font.LARGE_BLACK
    )

    override val guiObjects: List<GuiObject> = listOf(
        *equipmentButtons, header, switchBoardButton, opponentsBoardText
    )

    override fun create() {
        super.create()
        player.board.setTilesUnopened()
        player.board.createAndPlaceTreasures(1, TreasureType.TREASURECHEST, true)
        player.board.createAndPlaceTreasures(2, TreasureType.GOLDCOIN, true)
        player.board.createAndPlaceTreasures(2, TreasureType.GOLDKEY, true)

        opponent.board.setTilesUnopened()
        opponent.board.createAndPlaceTreasures(1, TreasureType.TREASURECHEST, false)
        opponent.board.createAndPlaceTreasures(2, TreasureType.GOLDCOIN, false)
        opponent.board.createAndPlaceTreasures(2, TreasureType.GOLDKEY, false)

        opponentsBoardText.hide()
    }

    override fun render() {
        view.render(
            *guiObjects.toTypedArray(),
            if (playerBoard) player.board else opponent.board
        )
    }

    override fun update(dt: Float) {
        handleInput()
        updateGUIObjects()
        updateHealth()
    }

    private fun updateHealth() {
        player.updateHealth()
        opponent.updateHealth()
        if (player.health == 0) {
            println("Opponent won!")
            GameStateManager.set(GameOverState(controller, false))
        } else if (opponent.health == 0) {
            println("You won!")
            GameStateManager.set(GameOverState(controller, true))
        }
    }

    private fun handleInput() {
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()
            val touchPos = Vector2(touchX, touchY)

            val boardWidth = Gdx.graphics.boardWidth()
            val boardPos = Gdx.graphics.boardPosition()
            val boardBounds = Rectangle(boardPos.x, boardPos.y, boardWidth, boardWidth)

            if (boardBounds.contains(touchPos)) {
                val boardTouchPos = touchPos.toCoordinate(boardPos, boardWidth, boardSize)
                if (playerTurn && !playerBoard) {
                    val result = opponent.board.shootTiles(
                        boardTouchPos,
                        player.equipmentSet.activeEquipment!!
                    )
                    handleResult(result)
                }
                // TODO remove else if. Handles opponent's moves
                else if (!playerTurn && playerBoard) {
                    val result = player.board.shootTiles(
                        boardTouchPos,
                        opponent.equipmentSet.activeEquipment!!
                    )
                    handleResult(result)
                }
            }
        }
    }

    private fun handleResult(result: Board.Result) {
        when (result) {
            Board.Result.NOT_VALID -> println("Not valid, try again")
            Board.Result.NO_USES_LEFT -> println("No more uses left")
            Board.Result.HIT -> println("Hitted")
            Board.Result.FOUND -> println("Found")
            Board.Result.MISS -> {
                playerTurn = !playerTurn
                println("Missed")
                Timer().schedule(2000) {
                    newTurn = true
                }
            }
        }
    }

    private fun updateGUIObjects() {
        equipmentButtons.forEachIndexed { i, _ ->
            val button = equipmentButtons[i]
            val equipment = player.equipmentSet.equipments[i]

            // Updates text and border of equipment buttons
            button.set(Text(equipment.name + " " + equipment.uses, Font.TINY_BLACK))
            if (equipment.active) {
                button.set(Border(Palette.GREEN))
            } else {
                button.set(Border(Palette.BLACK))
            }

            // Hides and shows equipment buttons and opponent's board text
            if (playerBoard) {
                button.hide()
                opponentsBoardText.show()
            } else {
                button.show()
                opponentsBoardText.hide()
            }
        }

        // Updates header text
        if (playerTurn) {
            header.set(Text("Your turn"))
        } else {
            header.set(Text("Waiting for opponent's move..."))
        }

        // Auto switching of boards
        if (playerTurn && playerBoard && newTurn) {
            playerBoard = !playerBoard
            newTurn = false
        } else if (!playerTurn && !playerBoard && newTurn) {
            playerBoard = !playerBoard
            newTurn = false
        }
    }

    private fun joinEquipmentButton(
        index: Int,
        position: Vector2,
        dimension: Vector2
    ): GuiObject {
        val equipment = player.equipmentSet.equipments[index]

        return GUI.textButton(
            position.x + dimension.x / player.equipmentSet.equipments.size * index + index * 2,
            position.y,
            dimension.x / player.equipmentSet.equipments.size,
            dimension.y,
            equipment.name + " x" + equipment.uses,
            borderColor = if (equipment.active) Palette.GREEN else Palette.BLACK,
            onClick = { player.equipmentSet.activeEquipment = equipment }
        )
    }
}

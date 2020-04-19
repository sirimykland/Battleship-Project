package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.Player
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

class PlayState(private val controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()
    private var player: Player = GSM.activeGame!!.player
    private var gameOver: Boolean = false
    private var showDialog: Boolean = false
    private var winningRenders: Int = 0

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
        "icons/swap_horiz_white.png",
        onClick = { GSM.activeGame!!.playerBoard = !GSM.activeGame!!.playerBoard }
    )
    private val opponentsBoardText = GUI.textBox(
        5f,
        2f,
        90f,
        10f,
        "Opponent's board",
        font = Font.MEDIUM_WHITE,
        color = Palette.DARK_GREY,
        borderColor = Palette.DARK_GREY
    )

    private val gameOverDialog = GUI.dialog(
            "Some text",
            listOf(
                    Pair("Main menu", { GSM.set(MainMenuState(controller)) }),
                    Pair("Play again", { GSM.set(MatchmakingState(controller)) }))
    )

    override val guiObjects: List<GuiObject> = listOf(
        *equipmentButtons, header, switchBoardButton, opponentsBoardText, *gameOverDialog
    )

    override fun create() {
        super.create()
        print("---PLAYSTATE---")
    }

    override fun render() {
        view.render(
            *guiObjects.toTypedArray(),
            if (GSM.activeGame!!.playerBoard) GSM.activeGame!!.player.board else GSM.activeGame!!.opponent.board
        )
    }

    override fun update(dt: Float) {
        updateBoardSwitching()
        handleInput()
        updateGUIObjects()
        updateHealth()
    }

    private fun updateHealth() {
        player.updateHealth()
        GSM.activeGame!!.opponent.updateHealth()

        if (player.health == 0) {
            controller.setWinner(GSM.userId, GSM.activeGame!!.gameId)
        } else if (GSM.activeGame!!.opponent.health == 0) {
            println("You won!")
            controller.setWinner(GSM.userId, GSM.activeGame!!.gameId)
            GSM.set(GameOverState(controller, true))
            gameOverDialog.forEachIndexed { index, guiObject ->
                guiObject.show()
            }
        }
    }

    private fun handleInput() {
        // Check if it is players turn and opponents board is showing.
        if (GSM.activeGame!!.isPlayersTurn() && !GSM.activeGame!!.playerBoard) {
            if (Gdx.input.justTouched()) {
                val touchX = Gdx.input.x.toFloat()
                val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()
                val touchPos = Vector2(touchX, touchY)
                val boardWidth = Gdx.graphics.boardWidth()
                val boardPos = Gdx.graphics.boardPosition()
                val boardBounds = Rectangle(boardPos.x, boardPos.y, boardWidth, boardWidth)

                if (boardBounds.contains(touchPos)) {
                    val boardTouchPos = touchPos.toCoordinate(boardPos, boardWidth, 10)
                    controller.makeMove(
                        GSM.activeGame!!.gameId,
                        boardTouchPos.x.toInt(),
                        boardTouchPos.y.toInt(),
                        GSM.activeGame!!.player.playerId,
                        GSM.activeGame!!.player.equipmentSet.activeEquipment!!.name
                    )
                    GSM.activeGame!!.makeMove(boardTouchPos)
                }
            }
        }
    }

    private fun updateBoardSwitching() {
        // Auto switching of boards
        if (GSM.activeGame!!.isPlayersTurn() && GSM.activeGame!!.playerBoard && GSM.activeGame!!.newTurn) {
            GSM.activeGame!!.playerBoard = !GSM.activeGame!!.playerBoard
            GSM.activeGame!!.newTurn = false
        } else if (!GSM.activeGame!!.isPlayersTurn() && !GSM.activeGame!!.playerBoard && GSM.activeGame!!.newTurn) {
            GSM.activeGame!!.playerBoard = !GSM.activeGame!!.playerBoard
            GSM.activeGame!!.newTurn = false
        }
    }

    private fun updateGUIObjects() {
        equipmentButtons.forEachIndexed { i, _ ->
            val button = equipmentButtons[i]
            val equipment = player.equipmentSet.equipments[i]

            // Updates text and border of equipment buttons
            button.set(Text(equipment.name + " x " + equipment.uses, Font.SMALL_BLACK))
            if (equipment.active) {
                button.set(Border(Palette.GREEN))
            } else {
                button.set(Border(Palette.BLACK))
            }

            // Hides and shows equipment buttons and opponent's board text
            if (GSM.activeGame!!.playerBoard) {
                button.hide()
                opponentsBoardText.show()
            } else {
                button.show()
                opponentsBoardText.hide()
            }
        }

        // Updates header text
        if (GSM.activeGame!!.isPlayersTurn()) {
            header.set(Text("Your turn"))
        } else {
            header.set(Text("Waiting for opponent's move..."))
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
            equipment.name + " x " + equipment.uses,
            borderColor = if (equipment.active) Palette.GREEN else Palette.DARK_GREY,
            onClick = { player.equipmentSet.activeEquipment = equipment },
            font = Font.SMALL_BLACK
        )
    }
}

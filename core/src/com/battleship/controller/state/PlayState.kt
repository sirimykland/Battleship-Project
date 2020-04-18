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

    private val mainMenuButton = GUI.textButton(
        5f,
        2f,
        42.5f,
        10f,
        "Main menu",
        font = Font.MEDIUM_BLACK,
        onClick = { GSM.set(MainMenuState(controller)) }
    ).hide()

    private val newGameButton = GUI.textButton(
        52.5f,
        2f,
        42.5f,
        10f,
        "Play another game",
        font = Font.MEDIUM_BLACK,
        onClick = { GSM.set(MatchmakingState(controller)) }
    ).hide()

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
            listOf(Pair("Dismiss", { showDialog = false }))
    )

    override val guiObjects: List<GuiObject> = listOf(
        header, switchBoardButton, *equipmentButtons, opponentsBoardText, mainMenuButton,
        newGameButton, *gameOverDialog
    )

    // TODO: Delete
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
        gameOver = GSM.activeGame!!.winner != ""

        if (gameOver) {
            // Hide equipment buttons
            for (btn in equipmentButtons) { btn.hide() }

            // Show game over action buttons
            mainMenuButton.show()
            newGameButton.show()
            showDialog = true

            // This does not work
            gameOverDialog[0] = GUI.textBox(10f, 40f, 80f, 20f, "Congrats you won")

            // Save winner to Firebase
            controller.setWinner(GSM.userId, GSM.activeGame!!.gameId)
        } else {
            updateGUIObjects()
            handleInput()
            updateBoardSwitching()
            GSM.activeGame!!.updateWinner()
        }

        if(showDialog) {
            gameOverDialog.forEach { guiObject -> guiObject.show() }
        } else {
            gameOverDialog.forEach { guiObject -> guiObject.hide() }
        }

        if (gameOver && GSM.activeGame!!.playerBoard) {
            header.set(Text("Opponent's board"))
        } else if(gameOver && GSM.activeGame!!.playerBoard) {
            header.set(Text("Your board"))
        }
        println("Active game: ${GSM.activeGame}")
        println("Active game board: ${GSM.activeGame!!.playerBoard}")
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
            val eqButton = equipmentButtons[i]
            val equipment = player.equipmentSet.equipments[i]

            // Updates text and border of equipment buttons
            eqButton.set(Text(equipment.name + " x " + equipment.uses, Font.SMALL_BLACK))
            if (equipment.active) {
                eqButton.set(Border(Palette.GREEN))
            } else {
                eqButton.set(Border(Palette.BLACK))
            }

            // Hides and shows equipment buttons based on which board you are viewing
            if (GSM.activeGame!!.playerBoard) {
                eqButton.hide()
            } else {
                eqButton.show()
            }
        }


        // Updates header text and show/hide opponent board text
        if (GSM.activeGame!!.isPlayersTurn()) {
            header.set(Text("Your turn"))
            opponentsBoardText.hide()
        } else {
            header.set(Text("Waiting for opponent's move..."))
            opponentsBoardText.show()
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
            font = Font.MEDIUM_BLACK
        )
    }
}

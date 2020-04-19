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
        "${GSM.activeGame!!.opponent.playerName}'s board",
        font = Font.MEDIUM_WHITE,
        color = Palette.DARK_GREY,
        borderColor = Palette.DARK_GREY
    )

    private val gameOverDialog = GUI.dialog(
            "Some text",
            listOf(Pair("Dismiss", { GSM.set(MainMenuState(controller)) }))
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
            if (GSM.activeGame!!.playerBoard) GSM.activeGame!!.player.board else GSM.activeGame!!.opponent.board,
            *guiObjects.toTypedArray()
        )
    }

    override fun update(dt: Float) {
        /* Oold
        updateBoardSwitching()
        handleInput()
        updateGUIObjects()
        updateHealth()
        */

        gameOver = GSM.activeGame!!.winner != ""

        if (gameOver) {
            if (winningRenders < 2 ) winningRenders++
            updateGUIObjectsGameOver()
            gameOverDialog.forEachIndexed() { i, element ->
                if (i == 0 && GSM.activeGame!!.youWon)
                    element.set(Text("Congratulations, you won!", font = Font.LARGE_WHITE))
                else if (i == 0 && !GSM.activeGame!!.youWon)
                    element.set(Text("Sorry, you lost :(", font = Font.LARGE_WHITE))
            }
            if(winningRenders == 1) { // First game over render
                // Save winner to Firebase
                controller.setWinner(GSM.userId, GSM.activeGame!!.gameId)
                showDialog = true
            }
        } else {
            autoBoardSwitching()
            updateGUIObjectsInGame()
            handleInput()
            // Update game status in GameStateManager
            GSM.activeGame!!.updateWinner()
        }
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

    private fun autoBoardSwitching() {
        if (GSM.activeGame!!.isPlayersTurn() && GSM.activeGame!!.playerBoard && GSM.activeGame!!.newTurn) {
            GSM.activeGame!!.playerBoard = !GSM.activeGame!!.playerBoard
            GSM.activeGame!!.newTurn = false
        } else if (!GSM.activeGame!!.isPlayersTurn() && !GSM.activeGame!!.playerBoard && GSM.activeGame!!.newTurn) {
            GSM.activeGame!!.playerBoard = !GSM.activeGame!!.playerBoard
            GSM.activeGame!!.newTurn = false
        }
    }

    private fun updateGUIObjectsInGame() {
        equipmentButtons.forEachIndexed { i, eqButton ->
            val equipment = player.equipmentSet.equipments[i]

            // Updates text and border of equipment buttons
            eqButton.set(Text(equipment.name + " x " + equipment.uses, Font.SMALL_BLACK))
            if (equipment.active) eqButton.set(Border(Palette.GREEN))
            else eqButton.set(Border(Palette.BLACK))

            // Show equipment buttons only if you are viewing your own board and it's your turn
            if (!GSM.activeGame!!.playerBoard && GSM.activeGame!!.isPlayersTurn()) eqButton.show()
            else eqButton.hide()
        }

        // Updates header text
        if (GSM.activeGame!!.isPlayersTurn()) header.set(Text("Your turn"))
        else header.set(Text("Waiting for ${GSM.activeGame!!.opponent.playerName}'s move..."))

        // Show/hide opponents board text
        if (GSM.activeGame!!.playerBoard) opponentsBoardText.show()
        else opponentsBoardText.hide()
    }

    private fun updateGUIObjectsGameOver() {
        equipmentButtons.forEach { button -> button.hide()}
        opponentsBoardText.hide()
        if (GSM.activeGame!!.playerBoard) {
            header.set(Text("Your board"))
        } else {
            header.set(Text("${GSM.activeGame!!.opponent.playerName}'s board"))
        }

        /*
        mainMenuButton.show()
        newGameButton.show()*/

        if(showDialog) {
            gameOverDialog.forEach { guiObject -> guiObject.show() }
        } else {
            gameOverDialog.forEach { guiObject -> guiObject.hide() }
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

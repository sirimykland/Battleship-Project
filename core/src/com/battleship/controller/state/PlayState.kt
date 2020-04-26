package com.battleship.controller.state

import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.input.BoardHandler
import com.battleship.model.Player
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.GdxGraphicsUtil.equipmentSetPosition
import com.battleship.utility.GdxGraphicsUtil.equipmentSetSize
import com.battleship.utility.Palette
import com.battleship.utility.SoundEffects
import com.battleship.view.PlayView
import com.battleship.view.View

/**
 * Create and handle components in the pre-game state placing ships on the board
 *
 * Inherits behavior from [GuiState]
 *
 * @param controller: FirebaseController - interface handling storing and retrieving data from Firebase
 */
class PlayState(controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()
    private var player: Player = GSM.activeGame!!.player
    private var gameOver: Boolean = false
    private var gameOverRendered: Boolean = false

    private val header = GUI.header("Your turn")

    private val equipmentButtons: Array<GuiObject> =
        arrayOf(*(0 until player.equipmentSet.equipments.size).map { a: Int ->
            joinEquipmentButton(
                a,
                equipmentSetPosition(),
                equipmentSetSize()
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
        "Main menu"
    ) {
        GSM.resetGame()
        GSM.set(MainMenuState(controller))
    }
        .hide()

    private val newGameButton = GUI.textButton(
        52.5f,
        2f,
        42.5f,
        10f,
        "Find new game"
    ) {
        GSM.resetGame()
        GSM.set(MatchmakingState(controller))
    }
        .hide()

    private val boardText = GUI.textBox(
        5f,
        2f,
        90f,
        10f,
        "${GSM.activeGame!!.opponent.playerName}'s board",
        font = Font.MEDIUM_WHITE,
        color = Palette.DARK_GREY,
        borderColor = Palette.DARK_GREY
    ).hide()

    private val gameOverDialog = GUI.dialog(
        "Some text",
        listOf(Pair("Dismiss", { toggleDialog(show = false) }))
    )

    /**
     * List of drawable gui and game objects
     */
    override val guiObjects: List<GuiObject> = listOf(
        header,
        switchBoardButton,
        *equipmentButtons,
        boardText,
        mainMenuButton,
        newGameButton,
        GUI.listener("boardHandler", BoardHandler(controller) {
            gameOver
        }),
        *gameOverDialog
    )

    /**
     * Called when the State should render itself.
     */
    override fun render() {
        view.render(
            if (GSM.activeGame!!.playerBoard) GSM.activeGame!!.player.board else GSM.activeGame!!.opponent.board,
            *guiObjects.toTypedArray()
        )
    }

    /**
     * Updates as often as the game renders itself.
     *
     * @param dt: Float - delta time since last call
     */
    override fun update(dt: Float) {
        gameOver = GSM.activeGame!!.winner != ""
        if (gameOver) {
            // only run once when game is over
            if (!gameOverRendered) {
                updateGUIObjectsGameOver()
                if (GSM.activeGame!!.youWon) {
                    gameOverDialog[1].set(
                        Text(
                            "Congratulations, you won!",
                            font = Font.LARGE_WHITE
                        )
                    )
                    SoundEffects.playVictory()
                } else {
                    gameOverDialog[1].set(Text("Sorry, you lost :(", font = Font.LARGE_WHITE))
                    SoundEffects.playLosing()
                }
                controller.setWinner(GSM.userId, GSM.activeGame!!.gameId)
                toggleDialog(show = true)
                gameOverRendered = true
            }
            if (GSM.activeGame!!.playerBoard) header.set(Text("Your board"))
            else header.set(Text("${GSM.activeGame!!.opponent.playerName}'s board"))
        } else {
            autoBoardSwitching()
            updateGUIObjectsInGame()
            // Update game status in GameStateManager
            GSM.activeGame!!.updateWinner()
        }
    }

    /**
     * Handles the switching of boards based on who's turn it is
     */
    private fun autoBoardSwitching() {
        if (GSM.activeGame!!.isPlayersTurn() && GSM.activeGame!!.playerBoard && GSM.activeGame!!.newTurn) {
            GSM.activeGame!!.playerBoard = !GSM.activeGame!!.playerBoard
            GSM.activeGame!!.newTurn = false
        } else if (!GSM.activeGame!!.isPlayersTurn() && !GSM.activeGame!!.playerBoard && GSM.activeGame!!.newTurn) {
            GSM.activeGame!!.playerBoard = !GSM.activeGame!!.playerBoard
            GSM.activeGame!!.newTurn = false
        }
    }

    /**
     * Updates the GUI objects relative to who's turn it is and which board is showing.
     */
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

        // Show/hide opponents bord text
        if (GSM.activeGame!!.playerBoard && !GSM.activeGame!!.isPlayersTurn()) {
            boardText.show()
            boardText.set(Text("Your board"))
        } else if (!GSM.activeGame!!.playerBoard && !GSM.activeGame!!.isPlayersTurn()) {
            boardText.show()
            boardText.set(Text("${GSM.activeGame!!.opponent.playerName}'s board"))
        } else if (GSM.activeGame!!.playerBoard && GSM.activeGame!!.isPlayersTurn()) {
            boardText.show()
            boardText.set(Text("Your board"))
        } else boardText.hide()
    }

    /**
     * Method for showing and hiding the Dialog.
     * @param show: Boolean - If it should show or hide the dialog
     */
    private fun toggleDialog(show: Boolean) {
        gameOverDialog.forEach { guiObject -> if (show) guiObject.show() else guiObject.hide() }
    }

    /**
     * Updates the GUI objects when the game is over.
     */
    private fun updateGUIObjectsGameOver() {
        equipmentButtons.forEach { button -> button.hide() }
        boardText.hide()
        if (GSM.activeGame!!.playerBoard) {
            header.set(Text("Your board"))
        } else {
            header.set(Text("${GSM.activeGame!!.opponent.playerName}'s board"))
        }

        mainMenuButton.show()
        newGameButton.show()
    }

    /**
     * Generates a button for each equipment in the equipmentSet.
     *
     * @param index: Int - Index in list of equipments, decides equipment and horizontal placement
     * @param position: Vector2 - The position of first equipment button
     * @param dimension: Vector2 - The dimension of equipment buttons area
     * @return [GuiObject] - The created button
     */
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

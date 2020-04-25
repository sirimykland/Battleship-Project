package com.battleship.controller.state

import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View
/**
 * Create and handle components in the matchmaking menu
 *
 * Inherits behavior from [GuiState]
 *
 * @param controller: FirebaseController - interface handling storing and retrieving data from Firebase
 */
class MatchmakingState(controller: FirebaseController) : GuiState(controller) {
    override var view: View = BasicView()
    private val itemsPerPage = 7
    private var page: Int = 0

    private val playerButtons: Array<GuiObject> =
        arrayOf(*(0 until itemsPerPage).map { a: Int -> joinUserButton(a) }.toTypedArray())

    private val nextPageButton = GUI.textButton(
        70f,
        19f,
        20f,
        5f,
        "Next"
    ) {
        page++
        updateButtons()
    }.hide()

    private val previousPageButton = GUI.textButton(
        10f,
        19f,
        20f,
        5f,
        "Previous"
    ) {
        page--
        updateButtons()
    }.hide()

    private val createGameButton = GUI.textButton(
        10f,
        2f,
        80f,
        10f,
        "Create new game"
    ) {
        createGame()
    }

    private val header = GUI.header("Choose your opponent ${GSM.username}")

    override val guiObjects: List<GuiObject> = listOf(
        header,
        nextPageButton,
        previousPageButton,
        *playerButtons,
        createGameButton,
        GUI.backButton { GSM.set(NameSelectionState(controller)) }
    )

    /**
     * Called once when the State is first initialized.
     * Adds listener for changes in available games
     */
    override fun create() {
        super.create()
        updateButtons()
        controller.addPendingGamesListener { pendingGames ->
            GSM.pendingGames = pendingGames
            updateButtons()
        }
    }

    /**
     * Updates the buttons on the page based on the data that has been retrieved from Firebase and
     * stored in the [GSM]
     */

    private fun updateButtons() {
        val index = page * itemsPerPage
        playerButtons.forEachIndexed { i, guiObject ->
            val j = index + i
            if (j < GSM.pendingGames.size) {
                guiObject.set(
                    Text(
                        "Join ${GSM.pendingGames[j].playerName}'s game",
                        font = Font.MEDIUM_BLACK
                    )
                )
                guiObject.show()
            } else {
                guiObject.hide()
            }
        }

        if (index + itemsPerPage < GSM.pendingGames.size) nextPageButton.show() else nextPageButton.hide()
        if (index > 0) previousPageButton.show() else previousPageButton.hide()
    }

    /**
     * Generates a button that can be used to show a game that is available for joining
     *
     * @param index: Int - Index in list of buttons, decides vertical placement
     * @return [GuiObject] - The created button
     */

    private fun joinUserButton(index: Int): GuiObject {
        return GUI.textButton(
            10f,
            75f - index * 8f,
            80f,
            6f,
            "Loading user"
        ) {
            val i = (page * itemsPerPage) + index
            if (GSM.userId !== "") {
                controller.joinGame(GSM.pendingGames[i].gameId, GSM.userId, GSM.username) { game ->
                    if (game != null) {
                        GSM.activeGame = game
                        GSM.set(PreGameState(controller))
                    } else {
                        GSM.resetGame()
                        GSM.set(MainMenuState(controller))
                    }
                }
            }
        }
            .hide()
    }

    /**
     * Uses the [controller] to create a new game and save it to Firebase.
     * When the game has been successfully saved to Firebase, the state is changed to [PreGameState]
     * If the process is unsuccessfull, the state is changed to [MainMenuState]
     */

    private fun createGame() {
        controller.createGame(GSM.userId, GSM.username) { game ->
            if (game != null) {
                GSM.activeGame = game
                GSM.set(PreGameState(controller))
            } else {
                GSM.resetGame()
                GSM.set(MainMenuState(controller))
            }
        }
    }

    /**
     * Updates as often as the game renders itself.
     *
     * @param dt: Float - delta time since last call
     */
    override fun update(dt: Float) {}

    /**
     * Called when the State should render itself.
     */
    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }
}

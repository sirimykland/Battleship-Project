package com.battleship.controller.state

import com.battleship.GameStateManager
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Image
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

/**
 * Create and handle components in the Usage Guide (help guide).
 * An introduction to the game for new players.
 *
 * Inherits behavior from [GuiState]
 *
 * @property controller: FirebaseController - interface handling storing and retrieving data from Firebase
 */
class UsageGuideState(private val controller: FirebaseController) : GuiState(controller) {
    override var view: View = BasicView()
    private var pageIndex: Int = 0

    // TODO: Update this with real description when game is more completed
    private val descriptions: List<String> = listOf(
        "First page, bla bla bla",
        "Second page, bla bla bla",
        "Third page, bla bla bla",
        "Fourth page, bla bla bla"
    )

    // TODO: Update this with real screenshots from the game when it is more completed
    private val imagePaths: List<String> = listOf(
        "helpGuide/page1.png",
        "helpGuide/page2.png",
        "helpGuide/page3.png",
        "helpGuide/page4.png"
    )

    private var currentDescription: GuiObject = GUI.text(
        3.13f,
        21.25f,
        93.75f,
        11.25f,
        descriptions[0],
        Font.MEDIUM_BLACK
    )
    private var currentImage: GuiObject = GUI.image(
        18.75f,
        32.5f,
        62.5f,
        50f,
        imagePaths[0]
    )

    private val nextPageButton = GUI.textButton(
        64f,
        3.75f,
        28.125f,
        10f,
        "Next"
    ) {
        pageIndex++
        updateButtons()
    }

    private val previousPageButton = GUI.textButton(
        4.6875f,
        3.75f,
        28.125f,
        10f,
        "Previous"
    ) {
        pageIndex--
        updateButtons()
    }.hide()

    override val guiObjects: List<GuiObject> = listOf(
        GUI.header("Usage guide"),
        nextPageButton,
        previousPageButton,
        currentDescription,
        currentImage,
        GUI.backButton { GameStateManager.set(SettingsState(controller)) }
    )

    /**
     * Called once when the State is first initialized.
     */
    override fun create() {
        super.create()
        updateButtons()
    }

    /**
     * Decide which page of the guide to show i.e. image, text and buttons
     */
    private fun updateButtons() {
        if (pageIndex < imagePaths.size - 1) {
            nextPageButton.show()
        } else {
            nextPageButton.hide()
        }

        if (pageIndex > 0)
            previousPageButton.show()
        else
            previousPageButton.hide()

        currentDescription.set(Text(descriptions[pageIndex]))
        currentImage.set(Image(imagePaths[pageIndex]))
    }

    /**
     * Updates as often as the game renders itself.
     *
     * @param dt: Float - delta time since last call
     */
    override fun update(dt: Float) {

    }

    /**
     * Called when the State should render itself.
     */
    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}

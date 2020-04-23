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
 * State handling all logic related to the help menu
 */
class UsageGuideState(private val controller: FirebaseController) : GuiState(controller) {
    override var view: View = BasicView()
    private var pageIndex: Int = 0

    private val descriptions: List<String> = listOf(
        "Choose a username",
        "Select opponent to play against or create a new game",
        "Place your treasures by dragging them to their desired position",
        "Wait for your opponent to get ready",
        "Play the game!"
    )

    private val imagePaths: List<String> = listOf(
        "images/usageGuide/page1.png",
        "images/usageGuide/page2.png",
        "images/usageGuide/page3.png",
        "images/usageGuide/page4.png",
        "images/usageGuide/page5.png"
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
        if (pageIndex == imagePaths.size - 1) {
            GameStateManager.set(MainMenuState(controller))
        } else {
            pageIndex++
            updateButtons()
        }
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

    override fun create() {
        super.create()
        updateButtons()
    }

    /**
     * Decide which buttons to show and update image and text
     */
    private fun updateButtons() {
        if (pageIndex == imagePaths.size - 1)
            nextPageButton.set(Text("Finish", Font.MEDIUM_BLACK))
        else
            nextPageButton.set(Text("Next", Font.MEDIUM_BLACK))
        if (pageIndex > 0)
            previousPageButton.show()
        else
            previousPageButton.hide()

        currentDescription.set(Text(descriptions[pageIndex]))
        currentImage.set(Image(imagePaths[pageIndex]))
    }

    override fun update(dt: Float) {
    }

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}

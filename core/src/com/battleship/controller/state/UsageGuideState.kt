package com.battleship.controller.state

import com.battleship.GameStateManager
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.Background
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Image
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
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
class UsageGuideState(
    controller: FirebaseController,
    private val firstimeOpen: Boolean = false
) : GuiState(controller) {

    override var view: View = BasicView()
    private var pageIndex: Int = 0

    private val descriptions: List<String> = listOf(
        "First, let's show you some basics of how to play a game.\nThe game follows standard battleships rules.",
        "Choose a username which other opponents can see.",
        "Select opponent to play against or create a new game.",
        "Place your treasures by dragging them to their desired position.",
        "Wait for your opponent to get ready.",
        "Play the game!\nChoose equipments and try to locate all of your opponent's treasures."
    )

    private val imagePaths: List<String> = listOf(
        "images/usageGuide/firstPage.png",
        "images/usageGuide/secondPage.png",
        "images/usageGuide/thirdPage.png",
        "images/usageGuide/fourthPage.png",
        "images/usageGuide/fifthPage.png"
    )

    private var endIndex: Int = descriptions.size - 1

    private var currentImage: GuiObject =
        GuiObject( 15f, 21f, 70f, 65f)
            .with(Image(imagePaths[0]))
            .with(Border(Palette.ORANGE)
    )

    private var currentDescription: GuiObject = GUI.textBox(
        5f,
        11f,
        90f,
        8f,
        descriptions[0],
        color = Palette.DARK_GREY
    )

    private val nextPageButton = GUI.textButton(
        70f,
        2f,
        25f,
        7f,
        "Next"
    ) {
        if (pageIndex == endIndex) {
            GameStateManager.set(MainMenuState(controller))
        } else {
            pageIndex++
            updateButtons()
        }
    }

    private val previousPageButton = GUI.textButton(
        5f,
        2f,
        25f,
        7f,
        "Previous"
    ) {
        pageIndex--
        updateButtons()
    }.hide()

    private val background: GuiObject = GuiObject(0f, 0f, 100f, 88f)
        .with(Background(Palette.GREY_TRANSPARENT))

    private val backButton: GuiObject =
        GUI.backButton { GameStateManager.set(SettingsState(controller)) }

    override val guiObjects: List<GuiObject> = listOf(
        background,
        if (firstimeOpen) GUI.header("Welcome to the Treasure Hunt game!") else GUI.header("Usage guide"),
        nextPageButton,
        previousPageButton,
        currentDescription,
        currentImage,
        backButton
    )

    /**
     * Called once when the State is first initialized.
     */
    override fun create() {
        super.create()
        if (firstimeOpen) backButton.hide()
        updateButtons()
    }

    /**
     * Decide which page of the guide to show i.e. image, text and buttons
     */
    private fun updateButtons() {
        if (pageIndex == endIndex)
            nextPageButton.set(Text("Finish", Font.MEDIUM_BLACK))
        else
            nextPageButton.set(Text("Next", Font.MEDIUM_BLACK))
        if (pageIndex > 0)
            previousPageButton.show()
        else
            previousPageButton.hide()

        currentDescription.set(Text(descriptions[pageIndex]))
        if(pageIndex == 0) currentImage.hide()
        else {
            currentImage.set(Image(imagePaths[pageIndex - 1]))
            currentImage.show()
        }
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

package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.model.GameObject
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Image
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

class HelpState : GuiState() {
    override var view: View = BasicView()

    private var pageIndex: Int = 0
    private val numberOfPages: Int = 4 // Need to be equal to size of images/descriptions lists

    // TODO: Update this with real screenshots from the game when it is more completed
    private val images: List<Image> = listOf(
        Image(200f, Gdx.graphics.height / 2 - 100f, "badlogic.jpg"),
        Image(200f, Gdx.graphics.height / 2 - 100f, "badlogic.jpg"),
        Image(200f, Gdx.graphics.height / 2 - 100f, "badlogic.jpg"),
        Image(200f, Gdx.graphics.height / 2 - 100f, "badlogic.jpg")
    )
    // TODO: Update this with real description when game is more completed
    private val descriptions: List<TextBox> = listOf(
        TextBox(20f, 150f, Gdx.graphics.width - 40f, 90f, "First page"),
        TextBox(20f, 150f, Gdx.graphics.width - 40f, 90f, "Second page"),
        TextBox(20f, 150f, Gdx.graphics.width - 40f, 90f, "Third page"),
        TextBox(20f, 150f, Gdx.graphics.width - 40f, 90f, "Fourth page")
    )
    private val nextPageButton = GUI.textButton(
        Gdx.graphics.width - 150f,
        30f,
        100f,
        90f,
        "->"
    ) {
        pageIndex++
        updateButtons()
    }
    private val previousPageButton = GUI.textButton(
        30f,
        30f,
        100f,
        70f,
        "<-"
    ) {
        pageIndex--
        updateButtons()
    }.hide()

    override val guiObjects: List<GuiObject> = listOf(
        GUI.text(
            20f,
            Gdx.graphics.height - 220f,
            Gdx.graphics.width - 40f,
            90f,
            "Matchmaking",
            Font.LARGE_WHITE
        ),
        nextPageButton,
        previousPageButton,
        *playerButtons,
        GUI.backButton
    )
    private fun updateButtons() {
        
    }



    override fun update(dt: Float) {
    }

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}

    /*
    private var uiElements: Array<GameObject> = arrayOf()
    override fun update(dt: Float) {
        /*uiElements = arrayOf(
            TextButton(20f, Gdx.graphics.height - 110f, 150f, 90f, "Back") { GameStateManager.set(MainMenuState()) },
            Header(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 130f, 300f, 150f, "Help"),
            *buttons.toTypedArray()
        )
    }
    override fun render() {
        /*when (currentPageIndex) {
            0 -> buttons = listOf(
                TextButton(Gdx.graphics.width - 170f, 20f, 150f, 90f, "-->") {
                    currentPageIndex += 1
                }
            )
            (numberOfPages - 1) -> buttons = listOf(
                TextButton(20f, 20f, 150f, 90f, "<--") {
                    currentPageIndex -= 1
                }
            )
            in 1..numberOfPages -> buttons = listOf(
                TextButton(20f, 20f, 150f, 90f, "<--") {
                    currentPageIndex -= 1
                },
                TextButton(Gdx.graphics.width - 170f, 20f, 150f, 90f, "-->") {
                    currentPageIndex += 1
                }
            )
        }*/
        view.render(*uiELeements.toTypedArray(), image, description)
    }

    override var buttons: List<Button> = listOf(
        TextButton(20f, Gdx.graphics.height - 110f, 150f, 90f, "Back") {
            GameStateManager.set(MainMenuState())
        },
        TextButton(20f, 20f, 150f, 90f, "<--") {
            if(currentPageIndex > 0) currentPageIndex -= 1
        },
        TextButton(Gdx.graphics.width - 170f, 20f, 150f, 90f, "-->") {
            if (currentPageIndex < numberOfPages - 1) currentPageIndex += 1
        }
    )*/
}

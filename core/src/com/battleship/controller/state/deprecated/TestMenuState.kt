package com.battleship.controller.state.deprecated

import com.battleship.GameStateManager
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.state.GuiState
import com.battleship.controller.state.MainMenuState
import com.battleship.model.ui.Background
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View

@Deprecated("Only used initially for testing")
class TestMenuState(private val controller: FirebaseController) : GuiState(controller) {
    private val greenBox = GuiObject(330f, 500f, 240f, 120f)
        .with(Background(Palette.GREEN))
        .with(Border(Palette.DARK_GREEN))
        .with(Text("green"))

    private val blueButton = GuiObject(430f, 300f, 200f, 110f)
        .with(Background(Palette.LIGHT_BLUE))
        .with(Border(Palette.BLUE, 20f, 10f, 0f, 10f))
        .with(Text("blue"))
        .onClick {
            println("Click blue")
        }

    private val boxButton = GuiObject(100f, 100f, 300f, 150f)
        .with(Text("box", Font.LARGE_WHITE))
        .with(Background(Palette.DARK_PURPLE))
        .with(Border(Palette.PINK))
        .onClick {
            println("Click box")
        }

    private val testText = GUI.text(100f, 400f, 200f, 100f, "Just text")

    override val guiObjects: List<GuiObject> = listOf(
        greenBox, blueButton, boxButton, testText, GUI.backButton {
            GameStateManager.set(
                MainMenuState(controller)
            )
        }
    )

    override var view: View = BasicView()

    override fun update(dt: Float) {
    }

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}

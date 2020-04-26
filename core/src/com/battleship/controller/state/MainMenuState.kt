package com.battleship.controller.state

import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

/**
 * State handling the main menu
 */
class MainMenuState(controller: FirebaseController) : GuiState(controller) {
    override var view: View = BasicView()

    /**
     * List of menu buttons
     */
    private val menuList = listOf(
            Pair("Settings") { GSM.set(SettingsState(controller)) },
            Pair("Play") { GSM.set(NameSelectionState(controller)) }
    )

    private val title: GuiObject = GUI.image(
            10f,
            66f,
            80f,
            12.5f,
            "font/title.png"
    )

    /**
     * List of drawable gui and game objects
     */
    override val guiObjects: List<GuiObject> = menuList
        .mapIndexed { i, (name, func) ->
            GUI.menuButton(
                25f,
                22f + 22f * i,
                name,
                onClick = func
            )
        }

    /**
     * Called when the State should render itself.
     */
    override fun render() {
        view.render(title, *guiObjects.toTypedArray())
    }
}

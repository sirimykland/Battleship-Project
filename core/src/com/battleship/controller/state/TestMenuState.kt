package com.battleship.controller.state

import com.battleship.model.ui.Button
import com.battleship.model.ui.TextBox
import com.battleship.model.ui.TextButton
import com.battleship.utility.Font
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View

class TestMenuState : MenuState() {

    val button = TextButton(100f, 100f, 300f, 150f, "box",
        Font.LARGE_WHITE, Palette.PINK, Palette.DARK_PURPLE) { println("aosidf") }
    val textbox = TextBox(430f, 300f, 200f, 110f, "blue")
    val textbox2 = TextBox(330f, 500f, 240f, 120f, "green",
        borderColor = Palette.DARK_GREEN, color = Palette.GREEN)

    override val buttons: List<Button> = listOf(button)
    override var view: View = BasicView()

    override fun update(dt: Float) {
    }

    override fun render() {
        view.render(*buttons.toTypedArray(), textbox, textbox2)
    }
}

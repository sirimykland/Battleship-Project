package com.battleship.utility

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.battleship.controller.input.BackButtonHandler
import com.battleship.model.ui.Background
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Image
import com.battleship.model.ui.Text
/**
 * Singleton class which acts as a factory for creating some common [GuiObject] configurations
 */
object GUI {

    /**
     * Creates a [GuiObject] which displays just text
     * @param posX: Float - The x position of the element on the screen in percentages
     * @param posY: Float - The y position of the element on the screen in percentages
     * @param sizeX: Float - The width of the element in percentages
     * @param sizeY: Float - The height of the element in percentages
     * @param text: String - The text that should be displayed
     * @param font: BitmapFont - The font that should be used for the text
     * @return [GuiObject] - The created object
     */
    fun text(
        posX: Float,
        posY: Float,
        sizeX: Float,
        sizeY: Float,
        text: String,
        font: BitmapFont = Font.MEDIUM_WHITE
    ): GuiObject {
        return GuiObject(posX, posY, sizeX, sizeY)
            .with(Text(text, font))
    }
    /**
     * Creates a [GuiObject] which displays text in a box
     * @param posX: Float - The x position of the element on the screen in percentages
     * @param posY: Float - The y position of the element on the screen in percentages
     * @param sizeX: Float - The width of the element in percentages
     * @param sizeY: Float - The height of the element in percentages
     * @param text: String - The text that should be displayed
     * @param font: BitmapFont - The font that should be used for the text
     * @param color: TextureRegion - The color of the background of the box, should be one of the
     * constants in [Palette]
     * @param borderColor TextureRegion - The color of the border of the box, should be one of the
     * constants in [Palette]
     * @return [GuiObject] - The created object
     */
    fun textBox(
        posX: Float,
        posY: Float,
        sizeX: Float,
        sizeY: Float,
        text: String,
        font: BitmapFont = Font.MEDIUM_BLACK,
        color: TextureRegion = Palette.LIGHT_GREY,
        borderColor: TextureRegion = Palette.DARK_GREY
    ): GuiObject {
        return GuiObject(posX, posY, sizeX, sizeY)
            .with(Background(color))
            .with(Border(borderColor))
            .with(Text(text, font))
    }

    /**
     * Creates a [GuiObject] which displays text in a box and can be clicked
     * @param posX: Float - The x position of the element on the screen in percentages
     * @param posY: Float - The y position of the element on the screen in percentages
     * @param sizeX: Float - The width of the element in percentages
     * @param sizeY: Float - The height of the element in percentages
     * @param text: String - The text that should be displayed
     * @param font: BitmapFont - The font that should be used for the text
     * @param color: TextureRegion - The color of the background of the box, should be one of the
     * constants in [Palette]
     * @param borderColor: TextureRegion - The color of the border of the box, should be one of the
     * constants in [Palette]
     * @param onClick: () -> Unit - The function that should be invoked when the object is clicked
     * @return [GuiObject] - The created object
     */
    fun textButton(
        posX: Float,
        posY: Float,
        sizeX: Float,
        sizeY: Float,
        text: String,
        font: BitmapFont = Font.MEDIUM_BLACK,
        color: TextureRegion = Palette.LIGHT_GREY,
        borderColor: TextureRegion = Palette.DARK_GREY,
        onClick: () -> Unit
    ): GuiObject {
        return GuiObject(posX, posY, sizeX, sizeY)
            .with(Background(color))
            .with(Border(borderColor))
            .with(Text(text, font))
            .onClick(onClick)
    }

    /**
     * Creates a [GuiObject] which displays text in a box and can be clicked. Similar to [textButton]
     * but the design of the button is predetermined to make the menus consistent
     * @param posX: Float - The x position of the element on the screen in percentages
     * @param posY: Float - The y position of the element on the screen in percentages
     * @param text: String - The text that should be displayed
     * @param onClick: () -> Unit - The function that should be invoked when the object is clicked
     * @return [GuiObject] - The created object
     */
    fun menuButton(
        posX: Float,
        posY: Float,
        text: String,
        onClick: () -> Unit
    ): GuiObject {
        return GuiObject(posX, posY, 50f, 12.5f)
            .with(Background(Palette.LIGHT_GREY))
            .with(Border(Palette.DARK_GREY))
            .with(Text(text, Font.MEDIUM_BLACK))
            .onClick(onClick)
    }

    /**
     * Creates a [GuiObject] which displays text in a header at the top of the app
     * @param text: String - The text that should be displayed
     * @return [GuiObject] - The created object
     */

    fun header(
        text: String
    ): GuiObject {
        return GuiObject(0f, 88f, 100f, 12f)
            .with(Background(Palette.DARK_GREY))
            .with(Border(Palette.BLACK, widthBottom = 1f))
            .with(Text(text, Font.MEDIUM_WHITE))
    }

    /**
     * Creates a [GuiObject] which is a white arrow in the top left of the app which should send you
     * back to the previous page
     * @param onClick: () -> Unit - The function that should be invoked when the object is clicked or
     * the back button on an Android phone is pressed
     * @return [GuiObject] - The created object
     */

    fun backButton(
        onClick: () -> Unit
    ): GuiObject {
        return GuiObject(0.15f, 90f, 10f, 8f)
            .with(Image("icons/round_arrow_back_white.png"))
            .onClick(onClick)
            .listen("onAndroidBack", BackButtonHandler(onClick))
    }

    /**
     * Creates a [GuiObject] which displays an image
     * @param posX: Float - The x position of the element on the screen in percentages
     * @param posY: Float - The y position of the element on the screen in percentages
     * @param sizeX: Float - The width of the element in percentages
     * @param sizeY: Float - The height of the element in percentages
     * @param texturePath: String - The path to the image that should be displayed
     * @return [GuiObject] - The created object
     */

    fun image(
        posX: Float,
        posY: Float,
        sizeX: Float,
        sizeY: Float,
        texturePath: String
    ): GuiObject {
        return GuiObject(posX, posY, sizeX, sizeY)
            .with(Image(texturePath))
    }

    /**
     * Creates a [GuiObject] which displays an image that can be clicked
     * @param posX: Float - The x position of the element on the screen in percentages
     * @param posY: Float - The y position of the element on the screen in percentages
     * @param sizeX: Float - The width of the element in percentages
     * @param sizeY: Float - The height of the element in percentages
     * @param texturePath: String - The path to the image that should be displayed
     * @param onClick: () -> Unit - The function that should be invoked when the object is clicked
     * @return [GuiObject] - The created object
     */

    fun imageButton(
        posX: Float,
        posY: Float,
        sizeX: Float,
        sizeY: Float,
        texturePath: String,
        onClick: () -> Unit
    ): GuiObject {
        return GuiObject(posX, posY, sizeX, sizeY)
            .with(Image(texturePath))
            .onClick(onClick)
    }

    /**
     * Creates a list of [GuiObject] which together comprises a dialogue window
     * @param text: String - The text that should be displayed at the top of the dialogue
     * @param buttons: List<Pair<String, () -> Unit>> - a list of strings and functions where each
     * pair defines a button in the dialogue window
     * @return Array<GuiObject> - The created objects
     */

    fun dialog(
        text: String,
        buttons: List<Pair<String, () -> Unit>>
    ): Array<GuiObject> {
        val dialogComponents = ArrayList<GuiObject>()

        dialogComponents.add(GuiObject(0f, 0f, 100f, 88f)
            .with(Background(Palette.GREY_TRANSPARENT))
            .onClick {
                // Stopping propagation
            }
            .hide()
        )

        dialogComponents.add(textBox(10f, 40.5f, 80f, 15f, text,
            color = Palette.DARK_GREY, font = Font.LARGE_WHITE).hide())

        val numberOfButtons = buttons.size
        buttons.forEachIndexed { index, btn ->
            dialogComponents.add(
                GuiObject(10f + (80f / numberOfButtons) * index, 30f, 80f / numberOfButtons, 10f)
                    .with(Background(Palette.LIGHT_GREY))
                    .with(Border(Palette.BLACK))
                    .with(Text(btn.first, Font.MEDIUM_BLACK))
                    .onClick(btn.second)
                    .hide()
            )
        }
        return dialogComponents.toTypedArray()
    }

    /**
     * Creates a [GuiObject] which is just a listener
     * @param key: String - The key that identifies the listener
     * @param listener: InputProcessor - The listener
     * @return Array<GuiObject> - The created object
     */

    fun listener(
        key: String,
        listener: InputProcessor
    ): GuiObject {
        return GuiObject(0f, 0f, 100f, 100f)
            .listen(key, listener)
    }
}

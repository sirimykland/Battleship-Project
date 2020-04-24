package com.battleship.utility

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.battleship.controller.input.BackButtonHandler
import com.battleship.model.ui.Background
import com.battleship.model.ui.Border
import com.battleship.model.ui.BottomBorder
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Image
import com.battleship.model.ui.Text

object GUI {
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

    fun header(
        text: String
    ): GuiObject {
        return GuiObject(0f, 88f, 100f, 12f)
            .with(Background(Palette.DARK_GREY))
            .with(BottomBorder(Palette.BLACK))
            .with(Text(text, Font.MEDIUM_WHITE))
    }

    fun backButton(
        onClick: () -> Unit
    ): GuiObject {
        return GuiObject(0.15f, 90f, 10f, 8f)
            .with(Image("icons/round_arrow_back_white.png"))
            .onClick(onClick)
            .listen("onAndroidBack", BackButtonHandler(onClick))
    }

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

    fun imageButton(
        posX: Float,
        posY: Float,
        sizeX: Float,
        sizeY: Float,
        texturePath: String,
        keepRatio: Boolean = false,
        onClick: () -> Unit
    ): GuiObject {
        return GuiObject(posX, posY, sizeX, if (keepRatio) sizeX * (Gdx.graphics.width.toFloat() / Gdx.graphics.height.toFloat()) else sizeY)
            .with(Image(texturePath))
            .onClick(onClick)
    }

    fun dialog(
        text: String,
        buttons: List<Pair<String, () -> Unit>>
    ): Array<GuiObject> {
        val dialogComponents = ArrayList<GuiObject>()
        dialogComponents.add(GuiObject(0f, 0f, 100f, 88f)
            .with(Background(Palette.GREY_TRANSPARENT))
            .onClick {
                println("Stopping propagation")
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

    fun listener(
        key: String,
        listener: InputProcessor,
        posX: Float = 0f,
        posY: Float = 0f,
        sizeX: Float = 100f,
        sizeY: Float = 100f
    ): GuiObject {
        return GuiObject(posX, posY, sizeX, sizeY)
            .listen(key, listener)
    }
}

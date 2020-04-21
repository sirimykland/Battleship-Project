package com.battleship.model.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.battleship.controller.input.ButtonHandler
import com.battleship.controller.input.KeyboardHandler
import com.battleship.model.GameObject
import com.battleship.utility.SoundEffects

class GuiObject(
    val position: Vector2,
    val size: Vector2
) : GameObject() {
    constructor(posX: Float, posY: Float, sizeX: Float, sizeY: Float) : this(Vector2(posX, posY), Vector2(sizeX, sizeY))
    private val parts: MutableList<GuiElement> = mutableListOf()
    var listener: InputAdapter = InputAdapter()
    var hasListener: Boolean = false
    var hidden: Boolean = false

    fun hide(): GuiObject {
        hidden = true
        return this
    }

    fun show(): GuiObject {
        hidden = false
        return this
    }

    fun with(element: GuiElement): GuiObject {
        parts.forEachIndexed { index, guiElement ->
            if (guiElement.zIndex > element.zIndex) {
                parts.add(index, element)
                return this
            }
        }
        parts.add(element)
        return this
    }

    fun set(element: GuiElement): GuiObject {
        parts.forEachIndexed { index, guiElement ->
            if (guiElement.javaClass == element.javaClass) {
                parts.removeAt(index)
                parts.add(index, element)
                return this
            }
        }
        parts.forEachIndexed { index, guiElement ->
            if (guiElement.zIndex > element.zIndex) {
                parts.add(index, element)
                return this
            }
        }
        parts.add(element)
        return this
    }

    fun remove(element: GuiElement): GuiObject {
        parts.forEachIndexed { index, guiElement ->
            if (guiElement.javaClass == element.javaClass) {
                parts.removeAt(index)
                return this
            }
        }
        return this
    }

    fun onClick(onClick: () -> Unit): GuiObject {
        if (hasListener) {
            throw IllegalStateException("This GuiObject already has a listener assigned")
        }
        listener = ButtonHandler(
            position.cpy().scl(Gdx.graphics.width / 100f, Gdx.graphics.height / 100f),
            size.cpy().scl(Gdx.graphics.width / 100f, Gdx.graphics.height / 100f)
        ) {
            if (!hidden) {
                SoundEffects.playClick()
                onClick()
            }
        }
        hasListener = true
        return this
    }

    fun noClick(): GuiObject {
        if (hasListener && listener is ButtonHandler) {
            listener = InputAdapter()
            return this
        }
        throw IllegalStateException("This GuiObject has no onClick listener to remove")
    }

    fun onKeyTyped(onKeyTyped: (char: Char) -> Unit): GuiObject {
        if (hasListener) {
            throw IllegalStateException("This GuiObject already has a listener assigned")
        }
        listener = KeyboardHandler { char ->
            if (!hidden) {
                onKeyTyped(char)
            }
        }
        hasListener = true
        return this
    }

    fun listen(listener: InputAdapter): GuiObject {
        if (hasListener) {
            throw IllegalStateException("This GuiObject already has a listener assigned")
        }
        this.listener = listener
        hasListener = true
        return this
    }

    override fun draw(batch: SpriteBatch) {
        if (!hidden) {
            parts.forEach {
                it.draw(
                    batch,
                    position.cpy().scl(Gdx.graphics.width / 100f, Gdx.graphics.height / 100f),
                    size.cpy().scl(Gdx.graphics.width / 100f, Gdx.graphics.height / 100f))
            }
        }
    }
}

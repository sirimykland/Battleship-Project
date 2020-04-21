package com.battleship.model.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.battleship.BattleshipGame
import com.battleship.controller.input.ButtonHandler
import com.battleship.controller.input.KeyboardHandler
import com.battleship.model.GameObject
import com.battleship.model.soundeffects.SoundEffects
import java.lang.IllegalStateException

class GuiObject(
    val position: Vector2,
    val size: Vector2
) : GameObject() {
    constructor(posX: Float, posY: Float, sizeX: Float, sizeY: Float) : this(Vector2(posX, posY), Vector2(sizeX, sizeY))
    private val parts: MutableList<GuiElement> = mutableListOf()

    private val listenerMap: MutableMap<String, InputProcessor> = mutableMapOf()
    val listener: InputProcessor
        get() {
            return InputMultiplexer(*listenerMap.toList().map { it.second }.toTypedArray())
        }

    val hasListener: Boolean
        get() {
            return listenerMap.isNotEmpty()
        }

    var hidden: Boolean = false
    var sound: SoundEffects =
        SoundEffects()

    fun hasListener(key: String): Boolean {
        return listenerMap.keys.contains(key)
    }

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
        val listener = ButtonHandler(
            position.cpy().scl(Gdx.graphics.width / 100f, Gdx.graphics.height / 100f),
            size.cpy().scl(Gdx.graphics.width / 100f, Gdx.graphics.height / 100f)
        ) {
            if (!hidden) {
                if (BattleshipGame.soundOn) {
                    sound.playClick(4.0f)
                }
                onClick()
            }
        }
        listen("onClick", listener)
        return this
    }

    fun onKeyTyped(onKeyTyped: (char: Char) -> Unit): GuiObject {
        val listener = KeyboardHandler { char ->
            if (!hidden) {
                onKeyTyped(char)
            }
        }
        listen("onKeyTyped", listener)
        return this
    }

    fun listen(key: String, listener: InputAdapter): GuiObject {
        if (hasListener(key)) {
            throw IllegalStateException("This GuiObject already has a listener assigned with key '$key'")
        }
        listenerMap[key] = listener
        return this
    }

    fun removeListener(key: String): GuiObject {
        if (!hasListener(key)) {
            throw IllegalStateException("This GuiObject does not have a listener assigned with key '$key'")
        }
        listenerMap.remove(key)
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

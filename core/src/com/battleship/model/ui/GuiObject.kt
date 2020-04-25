package com.battleship.model.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.battleship.controller.input.ClickHandler
import com.battleship.controller.input.KeyboardHandler
import com.battleship.model.GameObject
import com.battleship.utility.SoundEffects

/**
 * Class for objects in the graphical user interface, inheriting from [GameObject].
 *
 * @constructor
 * @param position: Vector2 - The position of the element on the screen in percentages
 * @param size: Vector2 - The size of the element in percentages
 */
class GuiObject(
    val position: Vector2,
    val size: Vector2
) : GameObject() {

    /**
     * Secondary constructor that wraps the positional and size arguments in [Vector2] objects for you
     *
     * @constructor
     * @param posX: Float - The x position of the element on the screen in percentages
     * @param posY: Float - The y position of the element on the screen in percentages
     * @param sizeX: Float - The width of the element in percentages
     * @param sizeY: Float - The height of the element in percentages
     */
    constructor(posX: Float, posY: Float, sizeX: Float, sizeY: Float) : this(
        Vector2(posX, posY),
        Vector2(sizeX, sizeY)
    )

    private val parts: MutableList<GuiElement> = mutableListOf()

    private val listenerMap: MutableMap<String, InputProcessor> = mutableMapOf()

    /**
     * @property listener: InputProcessor - Returns all listeners registered in the [listenerMap] as
     * an InputMultiplexer
     */
    val listener: InputProcessor
        get() {
            return InputMultiplexer(*listenerMap.values.toTypedArray())
        }

    /**
     * @property hasListener: Boolean - Returns whether any listeners are registered to this object
     */
    val hasListener: Boolean
        get() {
            return listenerMap.isNotEmpty()
        }

    var hidden: Boolean = false

    /**
     * Checks if a specific listener is registered
     * @param key: String - The key of the listener that should be looked up
     * @return Boolean - True if a listener has been registered with the key
     */
    fun listenerRegistered(key: String): Boolean {
        return listenerMap.keys.contains(key)
    }

    /**
     * Changes [hidden] to true, hiding the object from view and disabling onClick and onKeyTyped listeners
     * @return GuiObject - Returns this object to enable usage in a builder pattern
     */

    fun hide(): GuiObject {
        hidden = true
        return this
    }

    /**
     * Changes [hidden] to false, showing the element and enabling onClick and onKeyTyped listeners
     * @return GuiObject - Returns this object to enable usage in a builder pattern
     */

    fun show(): GuiObject {
        hidden = false
        return this
    }

    /**
     * Registers a [GuiElement] to be a part of the object, inserting it in the list of elements
     * sorted by [GuiElement.zIndex]
     * @param element: GuiElement - The element to be added to the object
     * @return GuiObject - Returns this object to enable usage in a builder pattern
     */

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

    /**
     * Registers a [GuiElement] to be a part of the object, replacing any element of the same type
     * if it exists
     * @param element: GuiElement - The element to be added to the object
     * @return GuiObject - Returns this object to enable usage in a builder pattern
     */

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

    /**
     * Removes a [GuiElement] from the object based on the class of the parameter
     * @param element: GuiElement - An element of the same type as the element to be removed to the object
     * @return GuiObject - Returns this object to enable usage in a builder pattern
     */

    fun remove(element: GuiElement): GuiObject {
        parts.forEachIndexed { index, guiElement ->
            if (guiElement.javaClass == element.javaClass) {
                parts.removeAt(index)
                return this
            }
        }
        return this
    }

    /**
     * Registers a [ClickHandler] to the [listenerMap] which will invoke the function provided and
     * play a sound if the object is not hidden when the player clicks/touches within the boundaries
     * of the object
     * @param onClick: () -> Unit - The function that should be invoked when the object is clicked
     * @return GuiObject - returns this object to enable usage in a builder pattern
     */

    fun onClick(onClick: () -> Unit): GuiObject {
        val listener = ClickHandler(
            position.cpy().scl(Gdx.graphics.width / 100f, Gdx.graphics.height / 100f),
            size.cpy().scl(Gdx.graphics.width / 100f, Gdx.graphics.height / 100f)
        ) {
            if (!hidden) {
                SoundEffects.playClick()
                onClick()
                return@ClickHandler true
            }
            return@ClickHandler false
        }
        listen("onClick", listener)
        return this
    }

    /**
     * Registers a [KeyboardHandler] to the [listenerMap] which will invoke the function provided
     * if the object is not hidden when the player types a key on their keyboard
     * @param onKeyTyped: (char: Char) -> Unit - The function that should be invoked when a key is typed
     * @return GuiObject - returns this object to enable usage in a builder pattern
     */

    fun onKeyTyped(onKeyTyped: (char: Char) -> Unit): GuiObject {
        val listener = KeyboardHandler { char ->
            if (!hidden) {
                onKeyTyped(char)
            }
        }
        listen("onKeyTyped", listener)
        return this
    }

    /**
     * Registers a listener to the object
     * @param key: String - The key that identifies the listener
     * @param listener: InputProcessor - The listener that should be registered
     * @throws IllegalStateException If a listener has already been registered with that key
     * @return GuiObject - returns this object to enable usage in a builder pattern
     */

    fun listen(key: String, listener: InputProcessor): GuiObject {
        if (listenerRegistered(key)) {
            throw IllegalStateException("This GuiObject already has a listener assigned with key '$key'")
        }
        listenerMap[key] = listener
        return this
    }

    /**
     * Removes a listener registered to the object
     * @param key: String - The key that identifies the listener
     * @throws IllegalStateException If there is no listener registered with that key
     * @return GuiObject - returns this object to enable usage in a builder pattern
     */

    fun removeListener(key: String): GuiObject {
        if (!listenerRegistered(key)) {
            throw IllegalStateException("This GuiObject does not have a listener assigned with key '$key'")
        }
        listenerMap.remove(key)
        return this
    }

    /**
     * Draws the object if not [hidden] by drawing all elements registered to the object
     * @param batch: SpriteBatch - The SpriteBatch object used to draw the element
     */

    override fun draw(batch: SpriteBatch) {
        if (!hidden) {
            parts.forEach {
                it.draw(
                    batch,
                    position.cpy().scl(Gdx.graphics.width / 100f, Gdx.graphics.height / 100f),
                    size.cpy().scl(Gdx.graphics.width / 100f, Gdx.graphics.height / 100f)
                )
            }
        }
    }
}

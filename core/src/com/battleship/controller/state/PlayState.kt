package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.battleship.GameStateManager
import com.battleship.model.Player
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.CoordinateUtil.toCoordinate
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.gameInfoPosition
import com.battleship.utility.GdxGraphicsUtil.gameInfoSize
import com.battleship.utility.GdxGraphicsUtil.weaponsetPosition
import com.battleship.utility.GdxGraphicsUtil.weaponsetSize
import com.battleship.utility.Palette
import com.battleship.view.PlayView
import com.battleship.view.View

class PlayState : GuiState() {
    override var view: View = PlayView()
    var boardSize = 10
    var player: Player = Player(boardSize)
    var opponent: Player = Player(boardSize)
    var playerBoard: Boolean = false
    var playerTurn: Boolean = true

    private val header = GUI.header("Your turn")

    private val equipmentButtons: Array<GuiObject> =
        arrayOf(*(0 until player.equipmentSet.equipments.size).map { a: Int ->
            joinEquipmentButton(
                a,
                Gdx.graphics.weaponsetPosition(),
                Gdx.graphics.weaponsetSize()
            )
        }.toTypedArray())

    // TODO Positioning/design
    private val switchBoardButton = GUI.textButton(
        Gdx.graphics.width - Gdx.graphics.gameInfoSize().x / 3,
        Gdx.graphics.gameInfoPosition().y - 20,
        Gdx.graphics.gameInfoSize().x / 3,
        Gdx.graphics.gameInfoSize().y,
        "Switch board",
        font = Font.TINY_BLACK,
        color = Palette.GREY,
        borderColor = Palette.LIGHT_GREY,
        onClick = {
            playerBoard = !playerBoard
        }
    )

    override val guiObjects: List<GuiObject> = listOf(
        *equipmentButtons, header, switchBoardButton
    )

    override fun create() {
        super.create()
        player.board.createAndPlaceTreasurechests(4, true)
        player.board.createAndPlaceGoldcoins(2, true)
        opponent.board.createAndPlaceTreasurechests(4, false)
        opponent.board.createAndPlaceGoldcoins(2, false)
    }

    override fun render() {
        if (playerBoard) {
            this.view.render(
                *guiObjects.toTypedArray(),
                player.board
            )
        } else {
            this.view.render(
                *guiObjects.toTypedArray(),
                opponent.board
            )
        }
    }

    override fun update(dt: Float) {
        handleInput()
        updateUIElements()
        updateHealth()
    }

    private fun updateHealth() {
        player.updateHealth()
        opponent.updateHealth()
        if (player.health == 0) {
            println("Opponent won!")
            GameStateManager.set(GameOverState())
        } else if (opponent.health == 0) {
            println("You won!")
            GameStateManager.set(GameOverState())
        }
    }

    private fun handleInput() {
        if (Gdx.input.justTouched()) {
            val touchPos =
                Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat())
            val boardWidth = Gdx.graphics.boardWidth()
            val boardPos = Gdx.graphics.boardPosition()
            val boardBounds = Rectangle(boardPos.x, boardPos.y, boardWidth, boardWidth)
            if (boardBounds.contains(touchPos)) {
                val boardTouchPos = touchPos.toCoordinate(boardPos, boardWidth, boardSize)
                if (playerTurn && !playerBoard) {
                    if (player.equipmentSet.activeEquipment!!.hasMoreUses()) {
                        var valid = opponent.board.shootTiles(
                            boardTouchPos,
                            player.equipmentSet.activeEquipment!!
                        )
                        if (valid) {
                            playerTurn = !playerTurn
                            player.equipmentSet.activeEquipment!!.use()
                        }
                    } else {
                        println(player.equipmentSet.activeEquipment!!.name + " has no more uses")
                    }
                }
                // TODO remove else if
                else if (!playerTurn && playerBoard) {
                    if (opponent.equipmentSet.activeEquipment!!.hasMoreUses()) {
                        var valid = player.board.shootTiles(
                            boardTouchPos,
                            opponent.equipmentSet.activeEquipment!!
                        )
                        if (valid) {
                            playerTurn = !playerTurn
                        }
                    } else {
                        println(opponent.equipmentSet.activeEquipment!!.name + " has no more uses")
                    }
                }
            }
        }
    }

    private fun updateUIElements() {
        equipmentButtons.forEachIndexed { i, _ ->
            val button = equipmentButtons[i]
            val equipment = player.equipmentSet.equipments[i]

            // Updates border of equipement buttons
            button.set(Text(equipment.name + " " + equipment.uses, Font.TINY_BLACK))
            val active = equipment.active
            if (active) {
                button.set(Border(Palette.GREEN))
            } else {
                button.set(Border(Palette.LIGHT_GREY))
            }

            // Hides and shows equipmentbuttons and switchboardbutton' text
            if (playerBoard) {
                equipmentButtons[i].hide()
                switchBoardButton.set(Text("Opponent's board", Font.TINY_BLACK))
            } else {
                equipmentButtons[i].show()
                switchBoardButton.set(Text("Your board", Font.TINY_BLACK))
            }

            // Updates header text
            if (playerTurn) {
                header.set(Text("Your turn"))
            } else {
                header.set(Text("Opponent's turn"))
            }
        }
        //  Updates border of switchboardbutton
        if (playerTurn && playerBoard) {
            switchBoardButton.set(Border(Palette.GREEN))
        } else if (!playerTurn && !playerBoard) {
            switchBoardButton.set(Border(Palette.GREEN))
        } else if (playerTurn && !playerBoard) {
            switchBoardButton.set(Border(Palette.LIGHT_GREY))
        } else if (!playerTurn && playerBoard) {
            switchBoardButton.set(Border(Palette.LIGHT_GREY))
        }
    }

    private fun joinEquipmentButton(
        index: Int,
        position: Vector2,
        dimension: Vector2
    ): GuiObject {
        val equipment = player.equipmentSet.equipments[index]

        var borderColor = Palette.LIGHT_GREY
        if (equipment.active) {
            borderColor = Palette.GREEN
        }

        return GUI.textButton(
            position.x + dimension.x / player.equipmentSet.equipments.size * index + index * 2,
            position.y,
            dimension.x / player.equipmentSet.equipments.size,
            dimension.y,
            equipment.name + " " + equipment.uses,
            font = Font.TINY_BLACK,
            color = Palette.GREY,
            borderColor = borderColor,
            onClick = {
                player.equipmentSet.activeEquipment = equipment
            }
        )
    }
}

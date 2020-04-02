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
import com.battleship.utility.GdxGraphicsUtil.equipmentsetPosition
import com.battleship.utility.GdxGraphicsUtil.equipmentsetSize
import com.battleship.utility.Palette
import com.battleship.view.PlayView
import com.battleship.view.View
import com.battleship.model.treasures.Treasure.TreasureType

class PlayState : GuiState() {
    override var view: View = PlayView()
    private var boardSize = 10
    private var player: Player = Player(boardSize)
    private var opponent: Player = Player(boardSize)
    private var playerBoard: Boolean = false
    private var playerTurn: Boolean = true

    private val header = GUI.header("Your turn")

    private val equipmentButtons: Array<GuiObject> =
        arrayOf(*(0 until player.equipmentSet.equipments.size).map { a: Int ->
            joinEquipmentButton(
                a,
                Gdx.graphics.equipmentsetPosition(),
                Gdx.graphics.equipmentsetSize()
            )
        }.toTypedArray())

    // TODO Positioning/design
    private val switchBoardButton = GUI.imageButton(
        87f,
        90f,
        8f,
        8f,
        "icons/refresh_white.png",
        onClick = {
            playerBoard = !playerBoard
        }
    )
    private val opponentsBoardText = GUI.text(
        5f,
        5f,
        90f,
        10f,
        "Opponent's board"
    )

    override val guiObjects: List<GuiObject> = listOf(
        *equipmentButtons, header, switchBoardButton, opponentsBoardText
    )

    override fun create() {
        super.create()
        player.board.setTilesUnopened()
        player.board.createAndPlaceTreasures(1, TreasureType.TREASURECHEST, true)
        player.board.createAndPlaceTreasures(2, TreasureType.GOLDCOIN, true)
        player.board.createAndPlaceTreasures(2, TreasureType.BOOT, true)

        opponent.board.setTilesUnopened()
        opponent.board.createAndPlaceTreasures(1, TreasureType.TREASURECHEST, false)
        opponent.board.createAndPlaceTreasures(2, TreasureType.GOLDCOIN, false)
        opponent.board.createAndPlaceTreasures(2, TreasureType.BOOT, false)

        opponentsBoardText.hide()
    }

    override fun render() {
        view.render(
            *guiObjects.toTypedArray(),
            if (playerBoard) player.board else opponent.board
        )
    }

    override fun update(dt: Float) {
        handleInput()
        updateGUIObjects()
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

    // TODO: Rector complicated function
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
                        val valid = opponent.board.shootTiles(
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
                // TODO remove else if. Handles opponent's moves
                else if (!playerTurn && playerBoard) {
                    if (opponent.equipmentSet.activeEquipment!!.hasMoreUses()) {
                        val valid = player.board.shootTiles(
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

    private fun updateGUIObjects() {
        equipmentButtons.forEachIndexed { i, _ ->
            val button = equipmentButtons[i]
            val equipment = player.equipmentSet.equipments[i]

            // Updates text and border of equipementbuttons
            button.set(Text(equipment.name + " " + equipment.uses, Font.TINY_BLACK))
            if (equipment.active) {
                button.set(Border(Palette.GREEN))
            } else {
                button.set(Border(Palette.LIGHT_GREY))
            }

            // Hides and shows equipmentbuttons and opponent's board text
            if (playerBoard) {
                equipmentButtons[i].hide()
                opponentsBoardText.show()
            } else {
                equipmentButtons[i].show()
                opponentsBoardText.hide()
            }

            // Updates header text
            if (playerTurn) {
                header.set(Text("Your turn"))
            } else {
                header.set(Text("Waiting for opponent..."))
            }
        }
        // playerBoard = playerTurn

        //  Updates border of switchboardbutton
        if (playerTurn && playerBoard) {

            //switchBoardButton.set(Border(Palette.GREEN))
        } else if (!playerTurn && !playerBoard) {

            //switchBoardButton.set(Border(Palette.GREEN))
        } else if (playerTurn && !playerBoard) {
            //switchBoardButton.set(Border(Palette.LIGHT_GREY))
        } else if (!playerTurn && playerBoard) {

            //switchBoardButton.set(Border(Palette.LIGHT_GREY))
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
        // TODO Positioning/design
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

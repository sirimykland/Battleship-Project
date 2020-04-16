package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.Board.Result
import com.battleship.model.Player
import com.battleship.model.equipment.Equipment
import com.battleship.model.soundeffects.SoundEffects
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.CoordinateUtil.toCoordinate
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.equipmentSetPosition
import com.battleship.utility.GdxGraphicsUtil.equipmentSetSize
import com.battleship.utility.Palette
import com.battleship.view.PlayView
import com.battleship.view.View
import java.util.Timer
import kotlin.concurrent.schedule

class PlayState(private val controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()
    // private var boardSize = 10
    private var player: Player = GSM.activeGame!!.me
    // private var opponent: Player = Player(boardSize)
    private var playerBoard: Boolean = false
    private var playerTurn: Boolean = true
    private var newTurn: Boolean = false
    private var sound = SoundEffects()

    private val header = GUI.header("Your turn")

    private val equipmentButtons: Array<GuiObject> =
        arrayOf(*(0 until player.equipmentSet.equipments.size).map { a: Int ->
            joinEquipmentButton(
                a,
                Gdx.graphics.equipmentSetPosition(),
                Gdx.graphics.equipmentSetSize()
            )
        }.toTypedArray())

    private val switchBoardButton = GUI.imageButton(
        87f,
        90f,
        8f,
        8f,
        "icons/swap_horiz_white.png",
        onClick = {
            playerBoard = !playerBoard
        }
    )
    private val opponentsBoardText = GUI.textBox(
        5f,
        2f,
        90f,
        10f,
        "Opponent's board",
        font = Font.MEDIUM_WHITE,
        color = Palette.DARK_GREY,
        borderColor = Palette.DARK_GREY
    )

    override val guiObjects: List<GuiObject> = listOf(
        *equipmentButtons, header, switchBoardButton, opponentsBoardText
    )

    override fun create() {
        super.create()
        print("---PLAYSTATE---")
    }

    private fun headerText(): String {
        if (GSM.activeGame!!.opponent.playerId == "") return "Waiting for opponent..."
        else if (GSM.activeGame!!.isMyTurn()) return (GSM.activeGame!!.opponent.playerName + "'s Board")
        return "Waiting for opponents turn"
    }

    override fun render() {
        this.view.render(
                GUI.header(headerText()),
                *guiObjects.toTypedArray(),
                GSM.activeGame!!.opponent.board,
                GSM.activeGame!!.me.equipmentSet)
    }

    override fun update(dt: Float) {
        handleInput()
        updateGUIObjects()
        updateHealth()
    }

    private fun updateHealth() {
        player.updateHealth()
        GSM.activeGame!!.opponent.updateHealth()
        if (player.health == 0) {
            println("Opponent won!")
            GSM.set(GameOverState(controller, false))
        } else if (GSM.activeGame!!.opponent.health == 0) {
            println("You won!")
            controller.setWinner(GSM.userId, GSM.activeGame!!.gameId)
            GSM.set(GameOverState(controller, true))
        }
    }

    private fun handleInput() {
        if (GSM.activeGame!!.isMyTurn()) {
            if (Gdx.input.justTouched()) {
                val touchX = Gdx.input.x.toFloat()
                val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()
                val touchPos = Vector2(touchX, touchY)
                val boardWidth = Gdx.graphics.boardWidth()
                val boardPos = Gdx.graphics.boardPosition()
                val boardBounds = Rectangle(boardPos.x, boardPos.y, boardWidth, boardWidth)

                if (boardBounds.contains(touchPos)) {
                    val boardTouchPos = touchPos.toCoordinate(boardPos, boardWidth, 10)
                    if (player.equipmentSet.activeEquipment!!.hasMoreUses()) {
                        val result = GSM.activeGame!!.opponent.board.shootTiles(
                            boardTouchPos,
                            player.equipmentSet.activeEquipment!!
                        )
                        handleResultFromMove(result, player.equipmentSet.activeEquipment!!)
                    } else {
                        println("Has no more uses")
                    }
                }
                // TODO remove else if. Handles opponent's moves
                else if (!playerTurn && playerBoard && GSM.activeGame!!.opponent.equipmentSet.activeEquipment!!.hasMoreUses()) {
                    if (GSM.activeGame!!.opponent.equipmentSet.activeEquipment!!.hasMoreUses()) {
                        val boardTouchPos = touchPos.toCoordinate(boardPos, boardWidth, 10)
                        val result = player.board.shootTiles(
                            boardTouchPos,
                                GSM.activeGame!!.opponent.equipmentSet.activeEquipment!!
                        )
                        handleResultFromMove(result, GSM.activeGame!!.opponent.equipmentSet.activeEquipment!!)
                    } else {
                        println("Has no more uses")
                    }
                }
            }
        }
    }

    private fun handleResultFromMove(resultList: ArrayList<Result>, equipment: Equipment) {
        when {
            resultList.contains(Result.FOUND) -> {
                println("Found")
                equipment.use()
            }
            resultList.contains(Result.HIT) -> {
                println("Hit")
                equipment.use()
                sound.playHit(0.8f)
            }
            resultList.all { n -> n == Result.NOT_VALID } -> {
                println("Not valid, try again")
            }
            else -> {
                println("Missed")
                equipment.use()
                equipment.playSound(0.8f)
                playerTurn = !playerTurn
                Timer().schedule(2000) {
                    newTurn = true
                }
            }
        }
    }

    private fun updateGUIObjects() {
        equipmentButtons.forEachIndexed { i, _ ->
            val button = equipmentButtons[i]
            val equipment = player.equipmentSet.equipments[i]

            // Updates text and border of equipment buttons
            button.set(Text(equipment.name + " x " + equipment.uses, Font.SMALL_BLACK))
            if (equipment.active) {
                button.set(Border(Palette.GREEN))
            } else {
                button.set(Border(Palette.BLACK))
            }

            // Hides and shows equipment buttons and opponent's board text
            if (playerBoard) {
                button.hide()
                opponentsBoardText.show()
            } else {
                button.show()
                opponentsBoardText.hide()
            }
        }

        // Updates header text
        if (playerTurn) {
            header.set(Text("Your turn"))
        } else {
            header.set(Text("Waiting for opponent's move..."))
        }

        // Auto switching of boards
        if (playerTurn && playerBoard && newTurn) {
            playerBoard = !playerBoard
            newTurn = false
        } else if (!playerTurn && !playerBoard && newTurn) {
            playerBoard = !playerBoard
            newTurn = false
        }
    }

    private fun joinEquipmentButton(
        index: Int,
        position: Vector2,
        dimension: Vector2
    ): GuiObject {
        val equipment = player.equipmentSet.equipments[index]

        return GUI.textButton(
            position.x + dimension.x / player.equipmentSet.equipments.size * index + index * 2,
            position.y,
            dimension.x / player.equipmentSet.equipments.size,
            dimension.y,
            equipment.name + " x " + equipment.uses,
            borderColor = if (equipment.active) Palette.GREEN else Palette.DARK_GREY,
            onClick = { player.equipmentSet.activeEquipment = equipment },
            font = Font.SMALL_BLACK
        )
    }
}

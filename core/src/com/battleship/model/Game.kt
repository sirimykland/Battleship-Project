package com.battleship.model

import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import com.battleship.model.equipment.Equipment
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * Game class for managing game logic
 *
 * @constructor
 * @property gameId
 */
class Game(val gameId: String) {
    var youWon: Boolean = false
    var winner: String = ""
    var player: Player = Player()
    var opponent: Player = Player()
    var playerTurn: Boolean = false
    var gameReady = false
    var playerBoard: Boolean = false
    var newTurn: Boolean = false
    var opponentLeft: Boolean = false

    /**
     * Set player and opponent objects and
     * determines whose player1 and player2 based on registered playerId.
     *
     * @param player1: Player
     * @param player2: Player
     */
    fun setPlayers(player1: Player, player2: Player = Player()) {
        if (player1.playerId == GSM.userId) {
            this.player = player1
            this.opponent = player2
            this.playerTurn = false
            this.playerBoard = true
        } else {
            this.player = player2
            this.opponent = player1
            this.playerTurn = true
            this.playerBoard = false
        }
    }

    /**
     * For player to make move on opponent's board
     * Switches board if successful hit and updates opponents health
     *
     * @param pos: Vector2 - coordinates on board to make move on
     */
    fun makeMove(pos: Vector2) {
        if (player.equipmentSet.activeEquipment!!.hasMoreUses()) {
            val missed = opponent.board.shootTiles(pos, player.equipmentSet.activeEquipment!!)
            switchTurn(missed)
            opponent.updateHealth()
        } else {
            println(player.equipmentSet.activeEquipment!!.name + " has No more uses")
        }
    }

    /**
     * Register a opponent's move onto players board.
     * @see DesktopFirebase,
     * @see AndroidFirebase
     *
     * @param move: Map<String, Any> - map with weapon, x and y coordinates and playerId
     */
    fun registerMove(move: Map<String, Any>) {
        if (opponent.equipmentSet.activeEquipment!!.hasMoreUses()) {
            var equipment: Equipment = opponent.equipmentSet.activeEquipment!!
            for (eq in GSM.activeGame!!.opponent.equipmentSet.equipments) {
                if (eq.name == move["weapon"]) {
                    equipment = eq
                }
            }
            val pos = Vector2(
                (move["x"] as Number).toFloat(),
                (move["y"] as Number).toFloat()
            )

            val missed = player.board.shootTiles(pos, equipment)
            switchTurn(missed)
            player.updateHealth()
        } else {
            println(opponent.equipmentSet.activeEquipment!!.name + " has No more uses")
        }
    }

    /**
     * @return Boolean - is players turn and not opponent
     */
    fun isPlayersTurn(): Boolean {
        return playerTurn
    }

    /**
     * Changes the state of the game if both players are registered and
     * has a non-empty list of treasures
     */
    fun setGameReadyIfReady() {
        if (isPlayersRegistered() && isTreasuresRegistered()) {
            gameReady = true
            player.updateHealth()
            opponent.updateHealth()
        }
    }

    /**
     * Checks if both players are registered with non-empty treasures
     *
     * @return Boolean
     */
    fun isTreasuresRegistered(): Boolean {
        val ready = !player.board.isTreasureListEmpty() && !opponent.board.isTreasureListEmpty()
        println("isTreasuresRegistered: $ready")
        return ready
    }

    /**
     * Checks if both players are registered with an ID
     *
     * @return Boolean
     */
    fun isPlayersRegistered(): Boolean {
        return player.playerId != "" && opponent.playerId != ""
    }

    /**
     * Switches whose players turn it is
     *
     * @param switch: Boolean - if true playerturn is switched
     */
    private fun switchTurn(switch: Boolean) {
        if (switch) {
            playerTurn = !playerTurn
            Timer().schedule(1000) {
                newTurn = true
            }
        }
    }

    /**
     * Determines if there is a game winner, depending on
     * whether player or opponents health reaches 0
     */
    fun updateWinner() {
        if (player.health == 0) { // Opponent won!
            player.board.revealTreasures()
            opponent.board.revealTreasures()
            winner = opponent.playerName
        } else if (opponent.health == 0) { // You won!
            youWon = true
            player.board.revealTreasures()
            opponent.board.revealTreasures()
            winner = player.playerName
        }
    }

    /**
     * toString specific for Game class
     * @return String
     */
    override fun toString(): String {
        return "Game( gameReady=$gameReady, player= $player, opponent=$opponent, playersTurn='$playerTurn')"
    }
}

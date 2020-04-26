package com.battleship.model

import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import java.lang.Long.parseLong
import java.util.Locale
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
        private set
    var opponent: Player = Player()
        private set
    var playerTurn: Boolean = false
    var gameReady = false
    var playerBoard: Boolean = false
    var newTurn: Boolean = false
    var opponentLeft: Boolean = false

    private fun hexSum(hexString: String): Long {
        return hexString.split("-").map { hex ->
            parseLong(hex.toUpperCase(Locale.getDefault()), 16)
        }.sum()
    }

    private fun findDeterminsticRandomStartPlayer(
        player1Id: String,
        player2Id: String,
        gameId: String
    ): Boolean {
        var p1Turn = true
        if (player1Id != "" && player2Id != "") {
            val p1val = hexSum(player1Id)
            val p2val = hexSum(player2Id)
            val gameVal = gameId.hashCode()
            p1Turn = p1val % gameVal < p2val % gameVal
        }
        return p1Turn
    }

    /**
     * Set player and opponent objects and
     * determines whose player1 and player2 based on registered playerId.
     *
     * @param player1: Player
     * @param player2: Player
     */
    fun setPlayers(player1: Player, player2: Player = Player()) {
        val p1Turn = findDeterminsticRandomStartPlayer(player1.playerId, player2.playerId, gameId)
        if (player1.playerId == GSM.userId) {
            this.player = player1
            this.opponent = player2
            this.playerTurn = p1Turn
            this.playerBoard = !p1Turn
        } else {
            this.player = player2
            this.opponent = player1
            this.playerTurn = !p1Turn
            this.playerBoard = p1Turn
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
        for (eq in GSM.activeGame!!.opponent.equipmentSet.equipments) {
            if (eq.name == move["weapon"]) {
                opponent.equipmentSet.activeEquipment = eq
            }
        }
        if (opponent.equipmentSet.activeEquipment!!.hasMoreUses()) {
            val pos = Vector2(
                (move["x"] as Number).toFloat(),
                (move["y"] as Number).toFloat()
            )

            val missed = player.board.shootTiles(pos, opponent.equipmentSet.activeEquipment!!)
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
        return !player.board.isTreasureListEmpty() && !opponent.board.isTreasureListEmpty()
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

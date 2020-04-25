package com.battleship.model

import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import java.lang.Long.parseLong
import java.util.Locale
import java.util.Timer
import kotlin.concurrent.schedule

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

    // Returns true if player missed. For handling the switching of boards
    fun makeMove(pos: Vector2) {
        if (player.equipmentSet.activeEquipment!!.hasMoreUses()) {
            val missed = opponent.board.shootTiles(pos, player.equipmentSet.activeEquipment!!)
            switchTurn(missed)
            opponent.updateHealth()
        } else {
            println(player.equipmentSet.activeEquipment!!.name + " has No more uses")
        }
    }

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

    fun isPlayersTurn(): Boolean {
        return playerTurn
    }

    fun setTreasures(treasures: Map<String, List<Map<String, Any>>>) {
        if (player.playerId in treasures) treasures[player.playerId]?.let {
            player.board.setTreasuresList(
                it
            )
        }
        if (opponent.playerId in treasures) {
            treasures[opponent.playerId]?.let { opponent.board.setTreasuresList(it) }
        }
    }

    fun setGameReadyIfReady() {
        if (isPlayersRegistered() && isTreasuresRegistered()) {
            gameReady = true
            player.updateHealth()
            opponent.updateHealth()
        }
    }

    fun isTreasuresRegistered(): Boolean {
        return !player.board.isTreasureListEmpty() && !opponent.board.isTreasureListEmpty()
    }

    fun isPlayersRegistered(): Boolean {
        return player.playerId != "" && opponent.playerId != ""
    }

    private fun switchTurn(switch: Boolean) {
        if (switch) {
            playerTurn = !playerTurn
            Timer().schedule(1000) {
                newTurn = true
            }
        }
    }

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

    override fun toString(): String {
        return "Game( gameReady=$gameReady, player= $player, opponent=$opponent, playersTurn='$playerTurn')"
    }
}

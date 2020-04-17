package com.battleship.model

import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import com.battleship.model.equipment.Equipment
import java.util.Timer
import kotlin.concurrent.schedule

class Game(val gameId: String) {
    var winner: String = ""
    var player: Player = Player()
    var opponent: Player = Player()
    var playerTurn: Boolean = false
    var gameReady = false
    var playerBoard: Boolean = false
    var newTurn: Boolean = false

    constructor(gameId: String, player1: Player, player2: Player = Player()) : this(gameId) {
        if (player1.playerId == GSM.userId) {
            this.player = player1
            this.opponent = player2
            this.playerTurn = false
        } else {
            this.player = player2
            this.opponent = player1
            this.playerTurn = true
        }
    }

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

    // Returns true if player missed. For handling the switching of boards
    fun makeMove(pos: Vector2) {
        if (player.equipmentSet.activeEquipment!!.hasMoreUses()) {
            val missed = opponent.board.shootTiles(pos, player.equipmentSet.activeEquipment!!)
            handleResultFromMove(missed)
        } else {
            println(player.equipmentSet.activeEquipment!!.name + " has No more uses")
        }
    }

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
            handleResultFromMove(missed)
        } else {
            println(opponent.equipmentSet.activeEquipment!!.name + " has No more uses")
        }
    }

    fun switchTurn() {
        playerTurn = !playerTurn
    }

    fun isPlayersTurn(): Boolean {
        return playerTurn
    }

    fun setTreasures(treasures: Map<String, List<Map<String, Any>>>) {
        println("input param: $treasures")
        println("is not empty?" + (treasures.isNotEmpty()))
        if (player.playerId in treasures) treasures[player.playerId]?.let {
            player.board.setTreasuresList(
                it
            )
        }
        if (opponent.playerId in treasures) {
            treasures[opponent.playerId]?.let { opponent.board.setTreasuresList(it) }
        }
        println("treasures: o:" + (opponent.board.treasures) + " and m:" + (this.player.board.treasures))
    }

    fun setGameReadyifReady() {
        if (isplayersRegistered() && isTreasuresRegistered()) {
            gameReady = true
        }
    }

    fun isTreasuresRegistered(): Boolean {
        val ready = !player.board.treasures.isEmpty() && !opponent.board.treasures.isEmpty()
        println("isTreasuresRegistered: $ready")
        return ready
    }

    fun isplayersRegistered(): Boolean {
        val ready = player.playerId != "" && opponent.playerId != ""
        println("isplayersRegistered: $ready")
        return ready
    }

    private fun handleResultFromMove(nextMove: Boolean) {
        if (nextMove) {
            switchTurn()
            Timer().schedule(1000) {
                newTurn = true
            }
        }
    }

    override fun toString(): String {
        return "Game( gameReady=$gameReady, opponent=$opponent, playersTurn='$playerTurn')"
    }
}

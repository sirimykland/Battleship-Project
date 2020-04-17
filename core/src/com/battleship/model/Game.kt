package com.battleship.model

import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import com.battleship.model.equipment.Equipment

class Game(val gameId: String) {
    var winner: String = ""
    var player: Player = Player()
    var opponent: Player = Player()
    var playerTurn: Boolean = false
    var gameReady = false

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
        isGameReady()
    }

    fun setPlayers(player1: Player, player2: Player = Player()) {
        if (player1.playerId == GSM.userId) {
            this.player = player1
            this.opponent = player2
            this.playerTurn = false
        } else {
            this.player = player2
            this.opponent = player1
            this.playerTurn = true
        }
        isGameReady()
    }

    fun playersRegistered(): Boolean {
        if (player.playerId != "" && opponent.playerId != "") {
            println("players registererd : $player, $opponent")
            return true
        } else {
            println("players NOT registererd : opponent-> $opponent")
        }
        isGameReady()
        return false
    }

    // Returns true if player missed. For handling the switching of boards
    fun makeMove(pos: Vector2): Boolean {
        if (player.equipmentSet.activeEquipment!!.hasMoreUses()) {
            val missed = opponent.board.shootTiles(pos, player.equipmentSet.activeEquipment!!)
            if(missed){
                switchTurn()
            }
            return missed
        } else {
            println(player.equipmentSet.activeEquipment!!.name + " has No more uses")
            return false
        }
    }

    fun registerMove(pos: Vector2, equipment: Equipment){
        if (opponent.equipmentSet.activeEquipment!!.hasMoreUses()) {
            val missed = player.board.shootTiles(pos, opponent.equipmentSet.activeEquipment!!)
            if(missed){
                switchTurn()
            }
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
        isGameReady()
    }

    fun isGameReady() {
        println("o: " + this.opponent.board.treasures)
        if (!player.board.treasures.isNullOrEmpty() && !opponent.board.treasures.isNullOrEmpty()) {
            gameReady = true
            println("Treasures are registered")
        }
    }

    override fun toString(): String {
        return "Game( gameReady=$gameReady, opponent=$opponent, playersTurn='$playerTurn')"
    }
}

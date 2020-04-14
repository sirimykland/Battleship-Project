package com.battleship.model

import com.badlogic.gdx.math.Vector2
import com.battleship.GSM

class Game(val gameId: String ) {
    var winner: String = ""
    var me: Player = Player()
    var opponent: Player = Player()
    var playersTurn: String = ""
    var gameReady = false

    constructor( gameId: String, player1: Player, player2: Player = Player()): this(gameId){
        if (player1.playerId == GSM.userId) {
            this.me = player1
            this.opponent = player2
        } else {
            this.me = player2
            this.opponent = player1
        }
        this.playersTurn = player2.playerId
        // me.board.boardColor = Color.LIGHT_GRAY
        isGameReady()
    }
    fun setPlayers( player1: Player, player2: Player = Player()){
        if (player1.playerId == GSM.userId) {
            this.me = player1
            this.opponent = player2
        } else {
            this.me = player2
            this.opponent = player1
        }
        // this.playersTurn = player2.playerId
        // me.board.boardColor = Color.LIGHT_GRAY
        isGameReady()
    }

    fun playersRegistered(): Boolean {
        if (me.playerId != "" && opponent.playerId != "") {
            println("players registererd : ${me}, ${opponent}")
            return true
        } else {
            println("players NOT registererd : opponent-> ${opponent}")
        }
        isGameReady()
        return false
    }

    fun makeMove(pos: Vector2): Boolean {
        if (me.equipmentSet.activeEquipment!!.hasMoreUses()) {
            opponent.board.shootTiles(pos, me.equipmentSet.activeEquipment!!)
            me.equipmentSet.activeEquipment!!.use()
            return true
        } else {
            println(me.equipmentSet.activeEquipment!!.name + "Has no ammo")
            return false
        }
    }

    fun flipPlayer() {
        if (playersTurn == me.playerId) playersTurn = opponent.playerId
        else playersTurn = me.playerId
        println("playersturn: $playersTurn")
    }

    fun isMyTurn(): Boolean {
        return playersTurn == me.playerId
    }

    fun setTreasures(treasures: Map<String, List<Map<String, Any>>>) {
        println("ID: o:" + (opponent.playerId in treasures) + " and m:" + (this.me.playerId in treasures))
        println("treasures: o:" + (opponent.board.treasures) + " and m:" + (this.me.board.treasures))

        if (opponent.board.treasures.isNullOrEmpty() && opponent.playerId in treasures) {
            println("   2. Opponents registered treasures: " + treasures[opponent.playerId])
            treasures[opponent.playerId]?.let { this.opponent.board.setTreasuresList(it as ArrayList) }
            /* this.opponent.board.setTreasuresList(treasures[opponent.playerId]?: error("No treasures in list"))*/
            isGameReady()
            println("   2. Opponents registered treasures: " + opponent.board.treasures)
            println("is game ready? $gameReady")
        }
        println("treasures: o:" + (opponent.board.treasures) + " and m:" + (this.me.board.treasures))
    }

    fun isGameReady() {
        println("Are they registered:" + (me.board.treasures.isNotEmpty()) + " and " + opponent.board.treasures.isNotEmpty())
        if (!me.board.treasures.isNullOrEmpty() && !opponent.board.treasures.isNullOrEmpty()) {
            gameReady = true
            println("Treasures are registered")
        }
    }

    override fun toString(): String {
        return "Game( gameReady=$gameReady, opponent=$opponent, playersTurn='$playersTurn')"
    }
}
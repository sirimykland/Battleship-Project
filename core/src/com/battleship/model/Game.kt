package com.battleship.model

import com.badlogic.gdx.math.Vector2
import com.battleship.GSM

class Game(val gameId: String) {
    var winner: String = ""
    var me: Player = Player()
    var opponent: Player = Player()
    var playersTurn: String = ""
    var gameReady = false

    constructor(gameId: String, player1: Player, player2: Player = Player()) : this(gameId) {
        if (player1.playerId == GSM.userId) {
            this.me = player1
            this.opponent = player2
        } else {
            this.me = player2
            this.opponent = player1
        }
        this.playersTurn = player2.playerId
        isGameReady()
    }

    fun setPlayers(player1: Player, player2: Player = Player()) {
        if (player1.playerId == GSM.userId) {
            this.me = player1
            this.opponent = player2
        } else {
            this.me = player2
            this.opponent = player1
        }
        this.playersTurn = player2.playerId
        isGameReady()
    }

    fun playersRegistered(): Boolean {
        if (me.playerId != "" && opponent.playerId != "") {
            println("players registererd : $me, $opponent")
            return true
        } else {
            println("players NOT registererd : opponent-> $opponent")
        }
        isGameReady()
        return false
    }

    fun makeMove(pos: Vector2): Boolean {
        return if (me.equipmentSet.activeEquipment!!.hasMoreUses()) {
            opponent.board.shootTiles(pos, me.equipmentSet.activeEquipment!!)
            me.equipmentSet.activeEquipment!!.use()
            true
        } else {
            println(me.equipmentSet.activeEquipment!!.name + "Has no ammo")
            false
        }
    }

    fun flipPlayer() {
        playersTurn = if (playersTurn == me.playerId) opponent.playerId
        else me.playerId
    }

    fun isMyTurn(): Boolean {
        return playersTurn == me.playerId
    }

    /* private fun List<Map<String, Any>>.treasureToList() : ArrayList<Treasure> {
        val treasures = ArrayList<Treasure>()
        lateinit var newTreasure: Treasure
        var position: Vector2 = Vector2(1f,1f)
        var rotate = true
        println(this.size)
        for (treasure in this) {

            // position = Vector2((treasure["x"] as Number).toFloat(), (treasure["y"] as Number).toFloat())
            // rotate = if (treasure.containsKey("rotate")) treasure["rotate"] as Boolean else false
            print("${treasure["type"]} , ${treasure["rotate"]}, ${treasure["x"]} ${treasure["y"]}")
            when (treasure["type"]) {
              "Gold coin" ->
                    newTreasure = GoldCoin(position, rotate)
                "Treasure chest" ->
                    newTreasure = TreasureChest(position, rotate)
                "Boot" ->
                    newTreasure = Boot(position, rotate)
                "Old stinking boot" ->
                    newTreasure = Boot(position, rotate)
            }
            newTreasure = Boot(position,rotate)
            treasures.add(newTreasure)
        }
        println("- new treasure: $treasures")
        return treasures
    }*/

    fun setTreasures(treasures: Map<String, List<Map<String, Any>>>) {
        println("input param: $treasures")
        println("is not empty?" + (treasures.isNotEmpty()))
        if (me.playerId in treasures) treasures[me.playerId]?.let { me.board.setTreasuresList(it) }
        if (opponent.playerId in treasures) {
            treasures[opponent.playerId]?.let { opponent.board.setTreasuresList(it) }
        }
        println("treasures: o:" + (opponent.board.treasures) + " and m:" + (this.me.board.treasures))
        isGameReady()
    }

    fun isGameReady() {
        println("o: " + this.opponent.board.treasures)
        if (!me.board.treasures.isNullOrEmpty() && !opponent.board.treasures.isNullOrEmpty()) {
            gameReady = true
            println("Treasures are registered")
        }
    }

    override fun toString(): String {
        return "Game( gameReady=$gameReady, opponent=$opponent, playersTurn='$playersTurn')"
    }
}
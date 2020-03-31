package com.battleship.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.battleship.GSM

class Game(val gameId: String, player1: Player, player2: Player = Player()) {
    var winner: String = ""
    var me: Player
    var opponent: Player
    var playersTurn: String
    var gameReady = false

    init {
        if (player1.playerId == GSM.userId) {
            this.me = player1
            this.opponent = player2
        } else {
            this.me = player2
            this.opponent = player1
        }
        this.playersTurn = player2.playerId
        me.board.boardColor = Color.LIGHT_GRAY
        if (opponent.playerId != "") gameReady = true
    }

    fun initOpponent(player2: Player){
        opponent=player2
        gameReady=true
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
        println("playersturn: "+playersTurn)
    }

    fun isMyTurn(): Boolean {
        return playersTurn == me.playerId
    }

}
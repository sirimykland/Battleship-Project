package com.battleship.model

import com.badlogic.gdx.math.Vector2
import com.battleship.GSM

class Game(val gameId: String, player1: Player, player2: Player, activePlayer: Player = player2) {
    var winner: String = ""
    var me: Player
    var opponent: Player
    var playersTurn: Player = activePlayer


    init {
        if (player1.playerId == GSM.userId) {
            this.me = player1
            this.opponent = player2
        } else {
            this.me = player2
            this.opponent = player1
        }
    }


    fun makeMove(pos: Vector2): Boolean {
        if (me.weaponSet.weapon!!.hasAmmunition()) {
            opponent.board.shootTiles(pos, me.weaponSet.weapon!!)
            me.weaponSet.weapon!!.shoot()
            return true
        } else {
            println(me.weaponSet.weapon!!.name + "Has no ammo")
            return false
        }
    }

    fun flipPlayer() {
        if (playersTurn == me) playersTurn = opponent
        else playersTurn = me
    }

    fun isMyTurn(): Boolean {
        return playersTurn.playerId == me.playerId
    }

}
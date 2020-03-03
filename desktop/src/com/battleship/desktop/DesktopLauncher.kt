package com.battleship.desktop

import com.battleship.controller.firebase.PlayerController


object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val map = PlayerController().getPlayers()
        println(map)
        /*val config = LwjglApplicationConfiguration()
        LwjglApplication(BattleshipGame(), config)*/
    }
}

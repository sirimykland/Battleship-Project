package com.battleship.model

import com.battleship.model.equipment.EquipmentSet

/**
 * Player class for storing player entities
 *
 * @property playerId: String
 * @property playerName: String
 */
class Player(var playerId: String = "", var playerName: String = "") {
    private val boardSize: Int = 10
    var equipmentSet = EquipmentSet()
    var board: Board = Board(boardSize)
    var health: Int = board.getCombinedTreasureHealth()

    /**
     * Sets health variable to be the combined health
     * of all treasures on to the players board.
     */
    fun updateHealth() {
        health = board.getCombinedTreasureHealth()
    }

    /**
     * toString specific for Player
     * @return String
     */
    override fun toString(): String {
        return "Player(Id='$playerId', Name='$playerName', board=$board)"
    }
}

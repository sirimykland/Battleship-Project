package com.battleship.model

/**
 * open class for Game list objects of available games
 *
 * @constructor
 * @property gameId: String
 * @property playerId: String
 * @property playerName: String
 */
open class GameListObject(
    var gameId: String,
    var playerId: String,
    var playerName: String
) {
    /**
     * toString specific for GameListObject
     * @return String
     */
    override fun toString(): String {
        return "GameListObject(gameId='$gameId', playerId='$playerId', playerName='$playerName')"
    }

    /**
     * hashCode method specific for GameListObject
     * @return Int - a hash code value for the object.
     */
    override fun hashCode(): Int {
        var result = gameId.hashCode()
        result = 31 * result + playerId.hashCode()
        result = 31 * result + playerName.hashCode()
        return result
    }

    /**
     * equals method specific for GameListObject
     * @param other: Any - any object
     * @return Boolean
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameListObject) return false

        if (gameId != other.gameId) return false
        if (playerId != other.playerId) return false

        return true
    }
}

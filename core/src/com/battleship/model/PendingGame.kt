package com.battleship.model

open class PendingGame(
    var gameId: String,
    var playerId: String,
    var playerName: String
) {

    override fun toString(): String {
        return "GameListObject(gameId='$gameId', playerId='$playerId', playerName='$playerName')"
    }

    override fun hashCode(): Int {
        var result = gameId.hashCode()
        result = 31 * result + playerId.hashCode()
        result = 31 * result + playerName.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PendingGame) return false

        if (gameId != other.gameId) return false
        if (playerId != other.playerId) return false

        return true
    }
}

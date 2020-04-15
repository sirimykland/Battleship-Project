package com.battleship.model

open class GameListObject(
    var gameId: String,
    var playerId: String,
    var playerName: String
) {

    override fun toString(): String {
        return "GameListObject(gameId='$gameId', playerId='$playerId', playerName='$playerName')"
    }
}

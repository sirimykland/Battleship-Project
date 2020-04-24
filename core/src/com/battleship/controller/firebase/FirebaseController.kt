package com.battleship.controller.firebase

import com.battleship.model.Game
import com.battleship.model.PendingGame

/**
 * Interface for firebase functionality
 */
interface FirebaseController {
    /**
     * Creates a new game document in firebase
     * @param userId the id of the user setting up the game
     * @param userName the name of the user setting up the game
     * @param callback function invoked once the process has completed.
     */
    fun createGame(userId: String, userName: String, callback: (game: Game?) -> Unit)

    /**
     * Method for joining an existing game.
     * @param gameId the id of the game document
     * @param userId the id of the player that should be added
     * @param userName the name of the player that should be added
     * @param callback function invoked once the process has completed.
     */
    fun joinGame(gameId: String, userId: String, userName: String, callback: (game: Game?) -> Unit)

    /**
     * Method for leaving an existing game.
     * @param gameId the id of the game document.
     * @param playerId the id of the player that leaves.
     * @param callback function invoked once the process has completed.
     */
    fun leaveGame(gameId: String, playerId: String, callback: () -> Unit)

    /**
     * Method for registering a player's treasures
     * @param gameId the id of the game document
     * @param userId the id of the player
     * @param treasures list containing the treasures that should be added
     */
    fun registerTreasures(gameId: String, userId: String, treasures: List<Map<String, Any>>)

    /**
     * Method for registering a move in the game
     * @param gameId the id of the game document
     * @param x x coordinate of move
     * @param y y coordinate of move
     * @param playerId the id of the player making the move
     * @param equipment The name of the equipment used by the player
     */
    fun registerMove(gameId: String, x: Int, y: Int, playerId: String, equipment: String)

    /**
     * Method for setting the winner of the game
     * @param gameId the id of the game document
     * @param userId the id of the player that won
     */
    fun setWinner(userId: String, gameId: String)
    fun addPendingGamesListener(callback: (pendingGames: ArrayList<PendingGame>) -> Unit)

    /**
     * Method adding listener to all games where player2 is empty.
     * @param callback function invoked once the process has completed.
     */
    fun addPendingGamesListener(callback: (pendingGames: ArrayList<GameListObject>) -> Unit)

    /**
     * Function adding listener to a specific game. Listening to when players joins and register treasures.
     * @param gameId the id of the game document
     * @param playerId the id of the player
     */
    fun addGameListener(gameId: String, playerId: String)

    /**
     * Function adding listener to a specific game. Listening to when players make moves.
     * @param gameId the id of the game document
     */
    fun addPlayListener(gameId: String)
}

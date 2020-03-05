package com.battleship.controller.firebase

class FindPlayer : FirebaseController() {
    fun getPlayers(): Map<String, String?> {
        return mapOf(
            Pair("jonas", "a73ab"),
            Pair("bendik", "6b293"),
            Pair("ingrid", "9c99d"),
            Pair("vivian", "ab434")
        )
    }
}

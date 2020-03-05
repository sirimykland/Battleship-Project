package com.battleship.controller.firebase

abstract class FirebaseController {
    // TODO: must be read from local config file. and not in plain text, remember to add to .gitignore
    var AUTH: String = ""
    var HOST: String = ""
    // might be useful https://firebase.google.com/docs/database/android/start
    fun setup() {
        // TODO
    }

    fun fetch() {
    }

    fun send() {
    }
}

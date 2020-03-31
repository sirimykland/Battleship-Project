package com.battleship.controller.firebase

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.OutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Class for handling authentication, everything is contained in companion object
 */
class Authentication {

    // Companion object is used to enable calling the functions without making an object of the class
    companion object {

        // The Api key used for identifying the firebase project during authentication, this is found in the firebase console
        private val API_KEY = "AIzaSyCyEdKGVFy96OPy_Kgxyb6-5_WB55K24M0"
        private val baseURL = "https://identitytoolkit.googleapis.com/v1/accounts:"

        /**
         * Registers an authenticated user for the firebase project
         * @return the response from the POST request
         */
        fun registerUser(email: String, password: String): AuthResponse {
            try {
                // Set up the url and the connection parameters
                val url = URL("${baseURL}signUp?key=$API_KEY")
                val connection = url.openConnection() as HttpsURLConnection
                connection.doOutput = true
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")

                // Add the body for the POST request
                val input = "{\"email\":\"$email\",\"password\":\"$password\",\"returnSecureToken\":true}"
                val outputStream: OutputStream = connection.outputStream
                outputStream.write(input.toByteArray())
                outputStream.flush()

                // If there occurs any error, throw exception
                if (connection.responseCode != 200) {
                    throw RuntimeException("Failed : HTTP error code " + connection.responseCode + ": " + connection.responseMessage)
                }

                // Read the result of the post request
                val result = connection.inputStream.bufferedReader().use(BufferedReader::readText)

                // Make the authResponse object from the input stream
                val g = Gson()
                val authResponse = g.fromJson(result, AuthResponse::class.java)

                // Take down the connection
                connection.disconnect()

                // Return the id token of the registered user
                return authResponse
            }

            // Throw exception
            catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }

        /**
         * Sign in to get an authentication token to access firebase
         * @return the result of the POST request
         */
        fun signIn(email: String, password: String): AuthResponse {
            try {
                // Set up the url and the connection parameters
                val url = URL("${baseURL}signInWithPassword?key=$API_KEY")
                val connection = url.openConnection() as HttpsURLConnection
                connection.doOutput = true
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")

                // Add the body for the POST request
                val input = "{\"email\":\"$email\",\"password\":\"$password\",\"returnSecureToken\":true}"
                val outputStream: OutputStream = connection.outputStream
                outputStream.write(input.toByteArray())
                outputStream.flush()

                // If there occurs any error, throw exception
                if (connection.responseCode != 200) {
                    throw RuntimeException("Failed : HTTP error code " + connection.responseCode + ": " + connection.responseMessage)
                }

                // Read the result of the post request
                val result = connection.inputStream.bufferedReader().use(BufferedReader::readText)

                // Make the authResponse object from the input stream
                val g = Gson()
                val authResponse = g.fromJson(result, AuthResponse::class.java)

                // Take down the connection
                connection.disconnect()

                // Return the id token of the registered user
                return authResponse
            }

            // Throw exception
            catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}

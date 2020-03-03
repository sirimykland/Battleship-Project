package com.battleship.controller.firebase
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream

/**
 * Class used to setup the database connection
 */
abstract class FirebaseController {
    //The URL of the firebase project
    private val firebaseUrl = "https://battleshipz.firebaseio.com"
    //Protected variable used by the other controllers to access database
    protected val db: Firestore;

    //Set up database connection
    init{
        //Read the account details from file
        val serviceAccount = FileInputStream("./core/src/com/battleship/controller/firebase/adminsdk.json")
        //Get the credentials from the account details
        val credentials = GoogleCredentials.fromStream(serviceAccount)
        //Set options for connection
        val options = FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setDatabaseUrl(firebaseUrl)
                .build()
        FirebaseApp.initializeApp(options)

        //Initialize database connection
        db = FirestoreClient.getFirestore()
    }
}

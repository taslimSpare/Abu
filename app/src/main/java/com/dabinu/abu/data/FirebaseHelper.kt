package com.dabinu.abu.data


import com.dabinu.abu.models.Account
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

/*
This class holds all the Firebase logic in this project.
Two components of Firebase are used here: Firebase-Authentication and Firebase-Firestore
 */
class FirebaseHelper {

    private val mAuth = FirebaseAuth.getInstance()
    private val firebaseFireStore = FirebaseFirestore.getInstance()


    // signs the user in with email and password
    fun signInWithEmailAndPassword(email: String, password: String, onSuccessListener: OnSuccessListener<AuthResult>, onFailureListener: OnFailureListener) {

        val task = mAuth.signInWithEmailAndPassword(email, password)

        task.addOnSuccessListener (onSuccessListener)
        task.addOnFailureListener (onFailureListener)
    }


    // creates a user with email and password
    fun createUserWithEmailAndPassword(email: String, password: String, onSuccessListener: OnSuccessListener<AuthResult>, onFailureListener: OnFailureListener) {

        val task = mAuth.createUserWithEmailAndPassword(email, password)

        task.addOnSuccessListener (onSuccessListener)
        task.addOnFailureListener (onFailureListener)
    }


    // fetches the user's data from Firestore
    fun fetchUserData(onSuccessListener: OnSuccessListener<DocumentSnapshot>, onFailureListener: OnFailureListener) {

        mAuth.uid?.let {

            val task = firebaseFireStore.collection( "users").document(it).get()

            task.addOnSuccessListener (onSuccessListener)
            task.addOnFailureListener (onFailureListener)
        }
    }


    // uploads the user's data to Firestore
    fun uploadUserData(userProfile: Account, onSuccessListener: OnSuccessListener<Void>, onFailureListener: OnFailureListener) {

        mAuth.uid?.let {

            val task = firebaseFireStore.collection( "users").document(it).set(userProfile)

            task.addOnSuccessListener (onSuccessListener)
            task.addOnFailureListener (onFailureListener)
        }
    }


    // terminates the user's session
    fun signOut() {
        mAuth.signOut()
    }


    // checks whether a user is currently authenticated
    fun isAuthenticated() : Boolean = mAuth.currentUser != null


    // updates the 'hasSubscribed' field in the user object to Firebase
    fun updateSubscriptionStatus(bool: Boolean) {
        mAuth.uid?.let { firebaseFireStore.collection("users").document(it).update("hasSubscribed", bool) }
    }

}

package com.dabinu.abu.data


import com.dabinu.abu.models.Account
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseHelper {

    private val mAuth = FirebaseAuth.getInstance()
    private val firebaseFireStore = FirebaseFirestore.getInstance()


    fun signInWithEmailAndPassword(email: String, password: String, onSuccessListener: OnSuccessListener<AuthResult>, onFailureListener: OnFailureListener) {

        val task = mAuth.signInWithEmailAndPassword(email, password)

        task.addOnSuccessListener (onSuccessListener)
        task.addOnFailureListener (onFailureListener)
    }


    fun createUserWithEmailAndPassword(email: String, password: String, onSuccessListener: OnSuccessListener<AuthResult>, onFailureListener: OnFailureListener) {

        val task = mAuth.createUserWithEmailAndPassword(email, password)

        task.addOnSuccessListener (onSuccessListener)
        task.addOnFailureListener (onFailureListener)
    }


    fun fetchUserData(onSuccessListener: OnSuccessListener<DocumentSnapshot>, onFailureListener: OnFailureListener) {

        mAuth.uid?.let {

            val task = firebaseFireStore.collection( "users").document(it).get()

            task.addOnSuccessListener (onSuccessListener)
            task.addOnFailureListener (onFailureListener)
        }
    }


    fun uploadUserData(userProfile: Account, onSuccessListener: OnSuccessListener<Void>, onFailureListener: OnFailureListener) {

        mAuth.uid?.let {

            val task = firebaseFireStore.collection( "users").document(it).set(userProfile)

            task.addOnSuccessListener (onSuccessListener)
            task.addOnFailureListener (onFailureListener)
        }
    }

    fun signOut() {
        mAuth.signOut()
    }


    fun isAuthenticated() : Boolean = mAuth.currentUser == null


    fun updateSubscriptionStatus(bool: Boolean) {
        mAuth.uid?.let { firebaseFireStore.collection("users").document(it).update("hasSubscribed", bool) }
    }

}
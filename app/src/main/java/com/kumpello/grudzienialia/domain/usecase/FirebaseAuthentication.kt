package com.kumpello.grudzienialia.domain.usecase

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthentication(activity: Activity) {
    private var auth: FirebaseAuth = Firebase.auth
    private var activity: Activity = activity
    private lateinit var user: FirebaseUser

    fun checkIfUserIsLogged(): Boolean {
        return auth.currentUser != null
    }

    fun createUser(email: String, password: String, callback: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    user = auth.currentUser!!
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
/*                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)*/
                }
            }
    }

    fun getUser(): FirebaseUser {
        return user
    }
}

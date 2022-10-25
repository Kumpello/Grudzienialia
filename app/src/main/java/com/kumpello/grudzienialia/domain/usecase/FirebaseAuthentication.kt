package com.kumpello.grudzienialia.domain.usecase

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseAuthentication @Inject constructor(private var activity: Activity) {
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var user: FirebaseUser

    fun isUserLogged(): Boolean {
        return auth.currentUser != null
    }

    fun create(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    user = auth.currentUser!!
                    callback(true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    callback(false)
                }
            }
    }

    fun singIn(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    user = auth.currentUser!!
                    //Todo call some class to save user!
                    callback(true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    callback(false)
                }
            }
    }

    fun resetPassword(email: String, callback: (Boolean) -> com.kumpello.grudzienialia.domain.model.Result) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    callback(true).accept()
                }
            }
    }

    fun getUser(): FirebaseUser {
        return user
    }
}

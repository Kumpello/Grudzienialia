package com.kumpello.grudzienialia.domain.usecase

import android.R
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.kumpello.grudzienialia.data.remote.User
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class FriendsService @Inject constructor(val firebaseAuth: FirebaseAuth, val database: DatabaseReference) {

    private val userIDKey = "userID"
    private val emailKey = "email"
    private val usersKey = "users"
    private val userFriendsKey = "userFriends"
    private val contactsKey = "contacts"
    private val userID = firebaseAuth.currentUser!!.uid
    private val userEmail = firebaseAuth.currentUser!!.email

    fun addFriend(friendsEmail: String, callback: (Boolean) -> Unit) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (friendsEmail.isEmpty()) {
            callback(false)

        }
        if (!friendsEmail.matches(emailPattern)) {
            callback(false)
        }
        val email = getEmail()
        val userHash: Int = email.hashCode()
        findFriendID(friendsEmail) {
            fun onSuccess(result: String?) {
                Log.d("FriendsService", "friendsID is $result")
                if (result != null) {
                    database.child(userFriendsKey).child(java.lang.String.valueOf(userType))
                        .child(userHash).child(contactsKey).child(result).setValue(true)
                    onResult.onSuccess(result)
                }
            }

            fun onError(error: Throwable?) {
                onResult.onError(Throwable(resources.getString(R.string.friends_id_not_found)))
            }
        }
        callback(true)
    }

    fun removeFriend(friendsEmail: String, callback: (Boolean) -> Unit) {
        val email = getEmail()
        val userHash: String = getHash(email)
        findFriendID(friendsEmail, type, object : OnResult<String?>() {
            fun onSuccess(result: String?) {
                database.child(userFriendsKey).child(java.lang.String.valueOf(type)).child(userHash)
                    .child(contactsKey).child(result).setValue(false)
            }

            fun onError(error: Throwable?) {
                Log.d("Error removing friend", "$userHash $type")
            }
        })
    }

    fun changeNick(nick: String) {
        Log.d("FriendsService", "Nick set to $nick")
        database.child(usersKey).child(userID).child("nick").setValue(nick)
    }

    fun getFriendsList(callback: (Boolean) -> Unit) {
        val userHash: Int = userEmail.hashCode()
        database.child(userFriendsKey).child(userHash.toString()).child(contactsKey).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.value != null) {
                        val tempMap = task.result.value as HashMap<String, Boolean>
                        for ((key) in tempMap.entries) {
                            Log.d("Getting friend list, friend key", key)
                            getUser(key, object : OnResult<User?>() {
                                fun onSuccess(result: User) {
                                    Log.d("Adding friend ", result.email)
                                    handler.onNext(result)
                                }

                                fun onError(error: Throwable?) {
                                    //ToDO
                                }
                            })
                        }
                    }
                } else {
                    Log.e("firebase", "Error getting data", task.getException())
                }
            }
    }

    private fun findFriendID(friendsEmail: String, callback: (String) -> Unit) {
        val friendsHash: Int = friendsEmail.hashCode()
        database.child(userFriendsKey).child(friendsHash.toString())
            .child(userIDKey).get().addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("firebase", "Error getting data", task.exception)
                    callback(null.toString())
                } else {
                    Log.d("FriendsService", "Success finding friends ID")
                    callback(task.result.value.toString())
                }
            }
    }

    fun getUserIDbyEmail(email: String,  callback: (String) -> Unit) {
        val emailHash: Int = email.hashCode()
        database.child(userFriendsKey).child(emailHash.toString())
            .child(userIDKey).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Adding friend, hash:", emailHash.toString())
                    handler.onSuccess(task.result.value as String)
                } else {
                    handler.onError(task.exception)
                }
            }
    }

    fun getSize(callback: (Int) -> Unit) {
        val userHash: Int = userEmail.hashCode()
        database.child(userFriendsKey).child(userHash.toString()).child(contactsKey).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val tempMap =
                        task.result.value as HashMap<String, Boolean>
                    if (tempMap != null) {
                        callback(tempMap.size)
                    } else {
                        callback(-1)
                    }
                }
            }
    }

    fun getUser(userID: String, callback: (Result<User>) -> Unit) {
        Log.d("getUser ", userID)
        database.child(usersKey).child(userID).get().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("firebase", "Error getting data", task.exception)
                callback(null)
            } else {
                val tempMap: Map<String, String> = task.result.value as Map<String, String>
                var user: User? = null
                if (tempMap != null) {
                    user = User(
                        tempMap[userIDKey],
                        tempMap[emailKey],
                    )
                } else {
                    //handler.onError(Throwable("User has no friends"))
                    callback(false)
                }
                if (user != null) {
                    Log.d("User added ", user.email + " " + user.userId)
                    callback(Result.success(user))
                    //Todo rest of results like this!!! Old errors from github needed
                } else {
                    //handler.onError(Throwable("Error getting user!"))
                }
            }
        }
    }

    private fun addUserToFriendsDataBase(user: User) {
        val userHash: String = user.email
        Log.d("FriendsService", "userHash: $userHash")
        database.child(userFriendsKey).child(userHash).child(emailKey)
            .setValue(user.email)
        database.child(userFriendsKey).child(userHash).child(userIDKey)
            .setValue(user.userId)
    }

}
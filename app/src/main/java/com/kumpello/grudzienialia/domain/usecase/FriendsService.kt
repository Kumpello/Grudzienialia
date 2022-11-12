package com.kumpello.grudzienialia.domain.usecase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.kumpello.grudzienialia.data.remote.User
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class FriendsService @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val database: DatabaseReference
) {

    private val userIDKey = "userID"
    private val emailKey = "email"
    private val usersKey = "users"
    private val userFriendsKey = "userFriends"
    private val contactsKey = "contacts"
    private val userID = firebaseAuth.currentUser!!.uid
    private val userEmail = firebaseAuth.currentUser!!.email
    private val userHash = userEmail.hashCode()

    fun addFriend(friendsEmail: String?, callback: (Result<Boolean>) -> Unit) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        if (friendsEmail.isNullOrEmpty()) {
            callback(Result.failure(Exception("Friends email is empty")))
            return
        }
        if (!friendsEmail.matches(emailPattern)) {
            callback(Result.failure(Exception("Friends email doesn't match patter ")))
            return
        }
        val userHash: Int = userEmail.hashCode()
        findFriendID(friendsEmail) {
            result ->
            if (result.isSuccess) {
                database.child(userFriendsKey).child(userHash.toString()).child(contactsKey).child(result.getOrThrow()).setValue(true)
                callback(Result.success(true))
            } else {
                callback(Result.failure(Exception("Couldn't find friends ID")))
            }
        }
    }

    fun removeFriend(friendsEmail: String, callback: (Result<Boolean>) -> Unit) {
        findFriendID(friendsEmail) {
            result ->
                if (result.isSuccess) {
                    database.child(userFriendsKey).child(userHash.toString()).child(contactsKey).child(result.getOrThrow()).setValue(false)
                    callback.invoke(Result.success(true))
                } else {
                    Log.d("Error removing friend", userHash.toString())
                    callback.invoke(Result.failure(result.exceptionOrNull()!!))
                }
        }
    }

    fun changeNick(nick: String?) {
        Log.d("FriendsService", "Nick set to $nick")
        database.child(usersKey).child(userID).child("nick").setValue(nick)
    }

    fun getFriendsList(callback: (Result<MutableList<User>>) -> Unit) {
        val friendsList: MutableList<User> = ArrayList()
        database.child(userFriendsKey).child(userHash.toString()).child(contactsKey).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.value != null) {
                        val tempMap = task.result.value as HashMap<String, Boolean>
                        for ((key) in tempMap.entries) {
                            Log.d("Getting friend list, friend key", key)
                            getUser(key) {
                                result ->
                                if (result.isSuccess) {
                                    friendsList.add(result.getOrThrow())
                                }
                            }
                        }
                        callback.invoke(Result.success(friendsList))
                    }
            } else {
            Log.e("firebase", "Error getting data", task.exception)
            callback.invoke(Result.failure(task.exception!!))
        }
    }
}

private fun findFriendID(friendsEmail: String, callback: (Result<String>) -> Unit) {
    val friendsHash: Int = friendsEmail.hashCode()
    database.child(userFriendsKey).child(friendsHash.toString())
        .child(userIDKey).get().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("firebase", "Error getting data", task.exception)
                callback(Result.failure(task.exception!!))
            } else {
                Log.d("FriendsService", "Success finding friends ID")
                callback(Result.success(task.result.value.toString()))
            }
        }
}

fun getUserIDbyEmail(email: String, callback: (Result<String>) -> Unit) {
    val emailHash: Int = email.hashCode()
    database.child(userFriendsKey).child(emailHash.toString())
        .child(userIDKey).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Adding friend, hash:", emailHash.toString())
                callback(Result.success(task.result.value.toString()))
            } else {
                callback(Result.failure(task.exception!!))
            }
        }
}

fun getSize(callback: (Result<Int>) -> Unit) {
    database.child(userFriendsKey).child(userHash.toString()).child(contactsKey).get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val tempMap =
                    task.result.value as HashMap<String, Boolean>
                if (tempMap != null) {
                    callback(Result.success(tempMap.size))
                } else {
                    callback(Result.failure(Throwable("User has no friends")))
                }
            }
        }
}

fun getUser(userID: String, callback: (Result<User>) -> Unit) {
    Log.d("getUser ", userID)
    database.child(usersKey).child(userID).get().addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.e("firebase", "Error getting data", task.exception)
            callback(Result.failure(task.exception!!.cause!!))
        } else {
            val tempMap: Map<String, String> = task.result.value as Map<String, String>
            var user: User? = null
            if (tempMap != null) {
                user = User(
                    tempMap[emailKey]!!,
                    tempMap[userIDKey]!!
                )
            } else {
                callback(Result.failure(Throwable("User has no friends")))
            }
            if (user != null) {
                Log.d("User added ", user.email + " " + user.userId)
                callback(Result.success(user))
            } else {
                callback(Result.failure(Throwable("Error getting user")))
            }
        }
    }
}

fun addUserToFriendsDataBase(user: User) {
    val userHash: String = user.email
    Log.d("FriendsService", "userHash: $userHash")
    database.child(userFriendsKey).child(userHash).child(emailKey)
        .setValue(user.email)
    database.child(userFriendsKey).child(userHash).child(userIDKey)
        .setValue(user.userId)
}

}
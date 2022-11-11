package com.kumpello.grudzienialia.ui.screens.application.friendsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kumpello.grudzienialia.R
import com.kumpello.grudzienialia.data.remote.User
import com.kumpello.grudzienialia.domain.model.NetworkModule
import com.kumpello.grudzienialia.domain.usecase.FriendsService
import com.kumpello.grudzienialia.ui.theme.GrudzienialiaTheme

@Composable
fun Friends(friendsService: FriendsService) {
    var userList: List<User>
    Refresh(friendsService) {
        result ->
        run {
            if (result.isSuccess) {
                userList = result.getOrThrow()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ) {
        Row() {
            AddFriendButton(friendsService)
            ChangeNickButton(friendsService)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.teal_700))
                .wrapContentSize(Alignment.Center)
        ) {

        }
    }

}

@Composable
fun AddFriendButton(friendsService: FriendsService) {
    var showDialog by remember { mutableStateOf(false) }
    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
        Button(
            onClick = {
                showDialog = true;
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Add Friend")
        }
    }
    if (showDialog) {
        makeAlertDialog(text = "Please type in friends email") { result ->
            run {
                if (result.isSuccess) {
                    friendsService.addFriend(result.getOrNull()) { addFriendResult ->
                        run {
                            if (addFriendResult.isSuccess) {
                                //Todo refresh/toast
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChangeNickButton(friendsService: FriendsService) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
        Button(
            onClick = {
                showDialog = true
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Add Friend")
        }
    }
    if (showDialog) {
        makeAlertDialog(text = "Please type in your new nick") { result ->
            run {
                if (result.isSuccess) {
                    friendsService.changeNick(result.getOrNull())
                }
            }
        }
    }
}

@Composable
fun removeFriend(friendsService: FriendsService, user: User) {
    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
        Button(
            onClick = {

            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Add Friend")
        }
    }
}

@Composable
fun makeAlertDialog(text: String, callback: (Result<String>) -> Unit) {
    GrudzienialiaTheme() {
        Column {
            val openDialog = remember { mutableStateOf(false) }
            val textState = remember { mutableStateOf(TextFieldValue()) }
            Button(onClick = {
                openDialog.value = true
            }) {
                Text("Click me")
            }

            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onCloseRequest.
                        openDialog.value = false
                    },
                    title = {
                        Text(text = text)
                    },
                    text = {
                        TextField(
                            value = textState.value,
                            onValueChange = { textState.value = it }
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                callback(Result.success(textState.value.text))
                                openDialog.value = false
                            }) {
                            Text("This is the Confirm Button")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("This is the dismiss Button")
                        }
                    }
                )
            }
        }

    }
}

@Composable
fun UserView(user: User) {

}

fun Refresh(friendsService: FriendsService, callback: (Result<List<User>>) -> Unit) {
    friendsService.getFriendsList {
        result ->
        run {
            if (result.isSuccess) {
                callback.invoke(Result.success(result.getOrThrow()))
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun FriendsPreview() {
    Friends(NetworkModule.provideFriendsService())
}

package com.kumpello.grudzienialia.ui.screens.application.addPostScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.kizitonwose.calendar.core.CalendarDay
import com.kumpello.grudzienialia.R
import com.kumpello.grudzienialia.data.remote.User
import com.kumpello.grudzienialia.domain.usecase.Database
import com.kumpello.grudzienialia.domain.usecase.FriendsService

@Composable
fun AddPost(day: CalendarDay, friendsService: FriendsService, database: Database) {
    var receiver by remember {
        mutableStateOf(ReceiverType.USER)
    }
    var showFriendsDropdown by remember {
        mutableStateOf(false)
    }
    var choosenFriend: User?
    var friends: List<User>? = null

    friendsService.getFriendsList { result ->
        run {
            if (result.isSuccess) {
                friends = result.getOrThrow()
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
            ChooseReceiver() { result ->
                when (result) {
                    ReceiverType.FRIEND -> {
                        receiver = ReceiverType.FRIEND
                        showFriendsDropdown = true
                    }
                    ReceiverType.USER -> receiver = ReceiverType.USER
                    ReceiverType.FRIENDS -> receiver = ReceiverType.FRIENDS
                }
            }
            if (showFriendsDropdown) {
                ChooseFriend(friends)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChooseReceiver(callback: (ReceiverType) -> Unit) {
    val contextForToast = LocalContext.current.applicationContext
    val listItems = ReceiverType.values()
    var selectedItem by remember {
        mutableStateOf(listItems[0].text)
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    // the box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        // text field
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listItems.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    selectedItem = selectedOption.text
                    callback.invoke(selectedOption)
                    Toast.makeText(contextForToast, selectedOption.text, Toast.LENGTH_SHORT).show()
                    expanded = false
                }) {
                    Text(text = selectedOption.text)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChooseFriend(list: List<User>?) {
    var selectedItem by remember {
        mutableStateOf("")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        TextField(
            value = selectedItem,
            onValueChange = { selectedItem = it },
            label = { Text(text = "Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // filter options based on text field value
        val filteringOptions =
            list?.filter { it.email.contains(selectedItem, ignoreCase = true) || it.nick.contains(selectedItem, ignoreCase = true) }

        if (filteringOptions != null) {
            if (filteringOptions.isNotEmpty()) {

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    filteringOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedItem = selectionOption.nick
                                expanded = false
                            }
                        ) {
                            Text(text = selectionOption.nick)
                        }
                    }
                }
            }
        }
    }
}

enum class ReceiverType(val text: String) {
    FRIEND("Friend"),
    FRIENDS("All Friends"),
    USER("You")
}
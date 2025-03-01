package com.example.notestakingapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Note(var selected: Boolean, val title: String = "New note", var content: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        val note = Note(selected = true, content = "")
        val listOfNotes = remember {
            mutableStateListOf<Note>().apply { add(note) }
        }

        val openNote = remember {
            mutableStateOf<Note>(note)
        }
        val textFieldValue = remember { mutableStateOf(TextFieldValue(openNote.value.content)) }

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(100.dp)
                    .fillMaxHeight()
            ) {
                stickyHeader {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Notes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                    Spacer(Modifier.height(8.dp))

                }
                itemsIndexed(listOfNotes) { index, item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(if (item.selected) Color.LightGray else Color.White)
                            .clickable(onClick = {
                                listOfNotes.forEach { item -> item.selected = false }
                                openNote.value = listOfNotes[index].apply { selected = true }
                                textFieldValue.value = TextFieldValue(openNote.value.content)
                                val indexOf = listOfNotes.indexOf(openNote.value)
                                listOfNotes.removeAt(indexOf)
                                listOfNotes.add(indexOf, openNote.value)
                            })
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            text = "new note",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            VerticalDivider(thickness = 1.dp, color = Color.Gray)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {

                OutlinedTextField(
                    modifier = Modifier.fillMaxSize(),
                    value = textFieldValue.value,
                    /*colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        focusedBorderColor = Color.Blue, // Custom focused border color
                        unfocusedBorderColor = Color.Gray // Custom unfocused border color
                    ),*/
                    label = { Text("What's on your mind..") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    onValueChange = { newText ->
                        textFieldValue.value = newText
                        listOfNotes.indexOf(openNote.value).let {
                            println("Inside onValueChanged: ${newText.text}")
                            println("Index of open note: $it, Note: ${listOfNotes[it]}")
                            listOfNotes[it].content = newText.text
                        }
                    },
                    placeholder = { Text(text = "Type here..", color = Color.Red) },
                    enabled = true,
                )
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .wrapContentHeight()
                .wrapContentWidth(),
            onClick = {
                listOfNotes.forEach { item -> item.selected = false }
                listOfNotes.add(Note(selected = true, content = ""))
                openNote.value = listOfNotes.last()
                textFieldValue.value = TextFieldValue(openNote.value.content)
            }
        ) {
            Text("Create Note")
        }
    }
}

@Composable
@Preview
fun PreviewNotes() {
    MainScreen()
}
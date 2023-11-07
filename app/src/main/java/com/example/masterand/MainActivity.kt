package com.example.masterand

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.masterand.R
import com.example.masterand.ui.theme.MasterAndTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
// A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreenInitial()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenInitial() {
    val profileImageUri: MutableState<Uri?> = rememberSaveable { mutableStateOf(null) }
    val name = rememberSaveable { mutableStateOf("") }
    val nameValidation = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val emailValidation = rememberSaveable { mutableStateOf("")}
    val colors = rememberSaveable { mutableStateOf("") }
    val colorsValidation = rememberSaveable {mutableStateOf("")}
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { selectedUri: Uri? ->
            if (selectedUri != null) {
                profileImageUri.value = selectedUri
            } else {
                profileImageUri.value = null
            }
        }
    )
    fun validateName () {
        if (name.value.isEmpty()) {
            nameValidation.value = "Name can't be empty"
        } else if (!name.value.matches(Regex("^[a-zA-Z ,.'-]+\$"))) {
            nameValidation.value = "Name is not valid"
        }
        else{
            nameValidation.value = ""
        }
    }

    fun validateEmail () {
        if (email.value.isEmpty()) {
            emailValidation.value = "Email can't be empty"
        } else if (!email.value.matches(Regex("^[a-zA-Z ,.'-]+\$"))) {
            emailValidation.value = "Email is not valid"
        }
        else{
            emailValidation.value = ""
        }
    }

    fun validateColors () {
        if (colors.value.isEmpty()) {
            colorsValidation.value = "Colors can't be empty"
        } else if (!colors.value.matches(Regex("^[a-zA-Z ,.'-]+\$"))) {
            colorsValidation.value = "Colors are not valid"
        }
        else{
            colorsValidation.value = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MasterAnd",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(bottom = 48.dp)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .clickable {
                    imagePicker.launch("image/*")
                }
        ) {
            if (profileImageUri.value != null) {
                Image(
                    painter = rememberImagePainter(
                        data = profileImageUri.value,
                        builder = {
                            crossfade(true)
                        },
                    ),
                    contentDescription = "Profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),

                    )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.baseline_question_mark_24),
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name.value,
            onValueChange = { name.value = it; validateName() },
            label = { Text(" Enter name") },
            singleLine = true,
            isError = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            supportingText = { Text(nameValidation.value) }
        )

    Spacer(modifier = Modifier.height(16.dp))
    if (email.value.isEmpty()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(" Enter email") },
            singleLine = true,
            isError = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            supportingText = { Text("Email can't be empty") }
        )
    } else {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(" Enter email") },
            singleLine = true,
            isError = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    if (colors.value.isEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = colors.value,
            onValueChange = { name.value = it },
            label = { Text(" Enter number of colors") },
            singleLine = true,
            isError = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            supportingText = { Text("Colors can't be empty") }
        )
    } else {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = colors.value,
            onValueChange = { name.value = it },
            label = { Text(" Enter number of colors") },
            singleLine = true,
            isError = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
    Button(onClick = { /*TODO*/ }) {
        Text("Next", modifier = Modifier.padding(start = 150.dp, end = 150.dp))

    }
}
}
@Preview
@Composable
fun ProfileScreenInitialPreview() {
    MasterAndTheme {
        ProfileScreenInitial()
    }
}
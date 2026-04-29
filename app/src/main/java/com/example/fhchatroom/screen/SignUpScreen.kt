package com.example.fhchatroom.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.fhchatroom.data.Result
import com.example.fhchatroom.data.semesterOptionsForStudyPath
import com.example.fhchatroom.data.studyPathOptions
import com.example.fhchatroom.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var studyPath by remember { mutableStateOf("") }
    var semesterInput by remember { mutableStateOf("") }
    var isStudyPathExpanded by remember { mutableStateOf(false) }
    var isSemesterExpanded by remember { mutableStateOf(false) }
    val result by authViewModel.authResult.observeAsState()
    val context = LocalContext.current
    val availableSemesterOptions = remember(studyPath) {
        semesterOptionsForStudyPath(studyPath)
    }

    LaunchedEffect(Unit) {
        authViewModel.clearAuthResult()
    }

    LaunchedEffect(studyPath, availableSemesterOptions) {
        val selectedSemester = semesterInput.toLongOrNull()
        if (selectedSemester != null && selectedSemester !in availableSemesterOptions) {
            semesterInput = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        ExposedDropdownMenuBox(
            expanded = isStudyPathExpanded,
            onExpandedChange = { isStudyPathExpanded = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = studyPath,
                onValueChange = {},
                readOnly = true,
                label = { Text("Study Path") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStudyPathExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isStudyPathExpanded,
                onDismissRequest = { isStudyPathExpanded = false }
            ) {
                studyPathOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            studyPath = option
                            semesterInput = ""
                            isStudyPathExpanded = false
                        }
                    )
                }
            }
        }
        ExposedDropdownMenuBox(
            expanded = isSemesterExpanded,
            onExpandedChange = {
                if (availableSemesterOptions.isNotEmpty()) {
                    isSemesterExpanded = it
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = semesterInput.toLongOrNull()?.let { "Semester $it" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Semester") },
                placeholder = {
                    Text(if (studyPath.isBlank()) "Choose study path first" else "Choose semester")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSemesterExpanded)
                },
                enabled = availableSemesterOptions.isNotEmpty(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                singleLine = true
            )
            ExposedDropdownMenu(
                expanded = isSemesterExpanded,
                onDismissRequest = { isSemesterExpanded = false }
            ) {
                availableSemesterOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text("Semester $option") },
                        onClick = {
                            semesterInput = option.toString()
                            isSemesterExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                val semester = semesterInput.toLongOrNull()

                // Use ignoreCase true in case the user inputs uppercase letters.
                if (!email.trim().endsWith("@stud.hcw.ac.at", ignoreCase = true)) {
                    // **Updated**: Show validation errors with Toast (disappear after a few seconds)
                    Toast.makeText(context, "Please use your institutional email (@stud.hcw.ac.at)", Toast.LENGTH_LONG).show()
                } else if (password.length < 8 || password.all { it.isLetterOrDigit() }) {
                    Toast.makeText(context, "Password must be at least 8 characters long and include at least one special character", Toast.LENGTH_LONG).show()
                } else if (studyPath.isBlank()) {
                    Toast.makeText(context, "Please choose your study path", Toast.LENGTH_LONG).show()
                } else if (semester == null || semester !in availableSemesterOptions) {
                    Toast.makeText(context, "Please choose an available semester for $studyPath", Toast.LENGTH_LONG).show()
                } else {
                    authViewModel.signUp(
                        email = email,
                        password = password,
                        firstName = firstName,
                        lastName = lastName,
                        studyPath = studyPath,
                        semester = semester
                    )
                    // Clear the input fields after attempting sign up.
                    email = ""
                    password = ""
                    firstName = ""
                    lastName = ""
                    studyPath = ""
                    semesterInput = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Already have an account? Sign in.",
            modifier = Modifier.clickable { onNavigateToLogin() }
        )
    }

    // Show Toast messages for sign-up success or failure
    LaunchedEffect(result) {
        when (val r = result) {
            is Result.Success -> {
                Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                authViewModel.clearAuthResult()
                onSignUpSuccess()
            }
            is Result.Error -> {
                r.exception?.message?.let { msg ->
                    val toastText = when {
                        msg.contains("address is already in use", ignoreCase = true) -> "Email is already registered."
                        msg.contains("badly formatted", ignoreCase = true) -> "Invalid email address format."
                        else -> "Sign up failed: $msg"
                    }
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                } ?: Toast.makeText(context, "Sign up failed: Unknown error", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }
}

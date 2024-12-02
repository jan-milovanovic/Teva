package com.blankcat.teva.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.blankcat.teva.R
import com.blankcat.teva.ui.navigation.Login

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    val termsAndPolicy: AnnotatedString = buildAnnotatedString {
        pushStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.onBackground,
            )
        )
        append("By signing up you agree to our ")
        pushStringAnnotation(tag = "terms&policy", annotation = "https://example.com")
        pushStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.tertiary,
                textDecoration = TextDecoration.Underline,
            )
        )
        append("Terms & Privacy Policy")
        pop()
    }


    Scaffold(
        // modifier = Modifier
        // .fillMaxSize()
        // .navigationBarsPadding()
        // .systemBarsPadding()
        // .captionBarPadding()
        // .displayCutoutPadding()
    ) { innerPadding ->

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            // .consumeWindowInsets(innerPadding)

        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(32.dp))
                Text("Teva", style = MaterialTheme.typography.displayLarge)
                Spacer(modifier = Modifier.height(32.dp))
                Image(
                    painter = painterResource(id = R.drawable.welcome),
                    contentDescription = "Welcome",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }


            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .verticalScroll(scrollState)
                    .consumeWindowInsets(innerPadding)
                    .imePadding()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f))
                    .fillMaxWidth()
                    .padding(32.dp)

            ) {

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
                    label = { Text("Email") },
                    // colors = OutlinedTextFieldDefaults.colors(
                    //     focusedTextColor = secondary,
                    //     focusedLabelColor = secondary,
                    //     unfocusedLabelColor = secondary,
                    //     unfocusedBorderColor = Color.Gray,
                    // )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    label = { Text("Password") },
                    // colors = OutlinedTextFieldDefaults.colors(
                    //     focusedTextColor = secondary,
                    //     focusedLabelColor = secondary,
                    //     unfocusedLabelColor = secondary,
                    // )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = repeatPassword,
                    onValueChange = { repeatPassword = it },
                    singleLine = true,
                    label = { Text("Repeat password") },
                    // colors = OutlinedTextFieldDefaults.colors(
                    //     focusedTextColor = secondary,
                    //     focusedLabelColor = secondary,
                    //     unfocusedLabelColor = secondary,
                    // )
                )

                Spacer(modifier = Modifier.height(64.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(192.dp)
                ) {
                    Button(
                        onClick = {
                            // TODO(jan): ensure UI does not allow register when password is not equal
                            viewModel.register(email, password, onSuccess = {
                                navController.navigate(Login)
                            })

                        },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 64.dp),
                    ) {
                        Text("Register")
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    val uriHandler = LocalUriHandler.current
                    ClickableText(
                        text = termsAndPolicy,
                        style = TextStyle(textAlign = TextAlign.Center),
                        onClick = { offset ->
                            termsAndPolicy.getStringAnnotations(
                                tag = "terms&policy",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let { annotation ->
                                uriHandler.openUri(annotation.item)
                            }
                        }

                    )
                }

            }
        }
    }
}
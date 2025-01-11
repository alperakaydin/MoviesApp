package com.alperakaydin.moviesapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

import com.alperakaydin.moviesapp.R
import com.alperakaydin.moviesapp.ui.theme.CustomBackground
import com.alperakaydin.moviesapp.ui.theme.CustomButtonColor
import com.alperakaydin.moviesapp.ui.theme.CustomTextColor
import com.alperakaydin.moviesapp.ui.theme.zentokyo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLogin: (String, String) -> Unit,padding: PaddingValues) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameFocused by remember { mutableStateOf(false) }
    var passwordFocused by remember { mutableStateOf(false) }

    // Lottie animation setup
//    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loginanimation))
//    val progress by animateLottieCompositionAsState(composition)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding) // Scaffold'dan gelen padding uygulanıyor
            .background(CustomBackground)
    )
    {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                    //
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "MoviesApp",
                    fontSize = 44.sp,
                    fontFamily = zentokyo,
                    fontWeight = FontWeight.Bold,
                    color = CustomTextColor
                )
                Spacer(modifier = Modifier.height(48.dp))


                val animationSpec = LottieCompositionSpec.RawRes(R.raw.loginblue)
                val composition by rememberLottieComposition(spec = animationSpec)
                val progress by animateLottieCompositionAsState(composition)

                // Animasyon
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(250.dp)
                )



                Spacer(modifier = Modifier.height(36.dp))

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username", color = Color.LightGray) },
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            usernameFocused = focusState.isFocused
                        }.padding(horizontal = 16.dp).fillMaxWidth(),

//                        .background(
//                            if (usernameFocused) Color.LightGray
//                            else CustomButtonColor
//                        )

                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = if (usernameFocused) Color.LightGray
                        else CustomButtonColor

                    )
                )

                // Spacer for padding
                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                // Password Field
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.LightGray) },
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            passwordFocused = focusState.isFocused
                        }
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = if (passwordFocused) Color.LightGray
                        else CustomButtonColor
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )

                // Spacer for padding
                Spacer(modifier = Modifier.height(86.dp))

                Button(
                    onClick = { onLogin(username, password)  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomButtonColor
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Login",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
        }

}
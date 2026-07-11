package com.jetnsync.parkingapp.feature_auth.presentation.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetnsync.parkingapp.core.presentation.NormalTextField
import com.jetnsync.parkingapp.core.presentation.PasswordTextField
import com.jetnsync.parkingapp.core.presentation.VerticalSpace
import com.jetnsync.parkingapp.feature_auth.domain.AuthEvent
import com.jetnsync.parkingapp.feature_auth.presentation.signup.SignUpPage
import com.jetnsync.parkingapp.feature_auth.presentation.signup.SignUpState
import com.jetnsync.parkingapp.feature_auth.presentation.signup.SignUpViewModel
import com.jetnsync.parkingapp.ui.theme.ParkingAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LogInRoot(
    modifier: Modifier = Modifier,
    navToHome : () -> Unit
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel.events

    LaunchedEffect(Unit) {
        events.collect {event->
            when(event){
                AuthEvent.Success ->{
                    navToHome()
                }
            }
        }
    }

    LoginPage(
        modifier,
        state = state,
        onAction = {
            viewModel.onAction(it)
        }
    )
}


@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        VerticalSpace(8.dp)
        Text(
            text = "Log in to your account",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        VerticalSpace(32.dp)

        NormalTextField(
            value = state.email,
            onValueChange = { onAction(LoginAction.OnEmailChange(it)) },
            placeholder = "example@email.com",
            titleText = "Email Address",
            modifier = Modifier.fillMaxWidth()
        )

        VerticalSpace(16.dp)

        PasswordTextField(
            value = state.password,
            onValueChange = { onAction(LoginAction.OnPasswordChange(it)) },
            placeholder = "••••••••",
            titleText = "Password",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text
        )

        VerticalSpace(32.dp)

        AnimatedContent(state.isLoading) {loading->
            if(loading){
                CircularProgressIndicator()

            }else{
                Button(
                    onClick = { onAction(LoginAction.OnLoginClick) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Login",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
//        if (state.isLoading) {
//            CircularProgressIndicator()
//        } else {
//            Button(
//                onClick = { onAction(LoginAction.OnLoginClick) },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = "Login",
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//            }
//        }

        state.error?.let {
            VerticalSpace(16.dp)
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    ParkingAppTheme {
        Box(Modifier.fillMaxSize()) {
            LoginPage(
                state = LoginState(),
                onAction = {}
            )
        }
    }
}
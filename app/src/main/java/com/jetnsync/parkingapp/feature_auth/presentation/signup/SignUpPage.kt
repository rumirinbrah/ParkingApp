package com.jetnsync.parkingapp.feature_auth.presentation.signup

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetnsync.parkingapp.core.presentation.NormalTextField
import com.jetnsync.parkingapp.core.presentation.PasswordTextField
import com.jetnsync.parkingapp.core.presentation.VerticalSpace
import com.jetnsync.parkingapp.feature_auth.domain.AuthEvent
import com.jetnsync.parkingapp.ui.theme.ParkingAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpRoot(
    modifier: Modifier = Modifier ,
    navToHome: () -> Unit
) {
    val viewModel = koinViewModel<SignUpViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel.events

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                AuthEvent.Success -> {
                    navToHome()
                }
            }
        }
    }
    SignUpPage(
        state = state ,
        onAction = {
            viewModel.onAction(it)
        }
    )
}

@Composable
fun SignUpPage(
    state: SignUpState ,
    onAction: (SignUpAction) -> Unit ,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize() ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account" ,
            style = MaterialTheme.typography.headlineMedium ,
            fontWeight = FontWeight.Bold ,
            color = MaterialTheme.colorScheme.primary
        )
        VerticalSpace(8.dp)
        Text(
            text = "Join us to start parking" ,
            style = MaterialTheme.typography.bodyLarge ,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        VerticalSpace(32.dp)

        NormalTextField(
            value = state.email ,
            onValueChange = { onAction(SignUpAction.OnEmailChange(it)) } ,
            placeholder = "example@email.com" ,
            titleText = "Email Address" ,
            modifier = Modifier.fillMaxWidth()
        )

        VerticalSpace(16.dp)

        PasswordTextField(
            value = state.password ,
            onValueChange = { onAction(SignUpAction.OnPasswordChange(it)) } ,
            placeholder = "••••••••" ,
            titleText = "Create Password" ,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text
        )

        VerticalSpace(16.dp)

        PasswordTextField(
            value = state.confirmPassword ,
            onValueChange = { onAction(SignUpAction.OnConfirmPasswordChange(it)) } ,
            placeholder = "••••••••" ,
            keyboardType = KeyboardType.Text,
            titleText = "Confirm Password" ,
            modifier = Modifier.fillMaxWidth()
        )

        VerticalSpace(32.dp)

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onAction(SignUpAction.OnSignUpClick) } ,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Register" ,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        state.error?.let {
            VerticalSpace(16.dp)
            Text(
                text = it ,
                style = MaterialTheme.typography.bodyMedium ,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpPreview() {
    ParkingAppTheme {
        Box(Modifier.fillMaxSize()) {
            SignUpPage(
                state = SignUpState() ,
                onAction = {}
            )
        }
    }
}

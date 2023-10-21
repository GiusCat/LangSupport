package org.progmob.langsupport.ui.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.progmob.langsupport.R
import org.progmob.langsupport.model.DataViewModel

@Composable
fun LoginScreen(
    viewModel: DataViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isValidEmail by remember { mutableStateOf(true) }
    val errorMsg by viewModel.errorMsg.observeAsState()

    LaunchedEffect(errorMsg) {
        if(!errorMsg.isNullOrEmpty())
            Toast.makeText(viewModel.getApplication(), errorMsg, Toast.LENGTH_SHORT).show()
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    isValidEmail = isValidEmail || Patterns.EMAIL_ADDRESS.matcher(email).matches()
                },
                textStyle =  MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                ),
                label = { Text(text = stringResource(id = R.string.e_mail_address)) },
                isError = !isValidEmail,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Unspecified,
                    focusedContainerColor = Color.Unspecified,
                    errorContainerColor = Color.Unspecified
                ),
                modifier = Modifier.fillMaxWidth(0.55f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                textStyle =  MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                ),
                label = { Text(text = stringResource(id = R.string.password)) },
                isError = !isValidEmail,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Unspecified,
                    focusedContainerColor = Color.Unspecified,
                    errorContainerColor = Color.Unspecified
                ),
                modifier = Modifier.fillMaxWidth(0.45f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    if(isValidEmail) viewModel.signInUser(email, password)
                },
                enabled = isValidEmail
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in_btn).uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            val primary = MaterialTheme.colorScheme.secondary
            val surface = MaterialTheme.colorScheme.surface
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 8.dp)
                    .drawBehind {
                        drawLine(
                            color = primary,
                            Offset(0f, size.height / 2),
                            Offset(size.width, size.height / 2),
                            strokeWidth = 4f,
                            cap = StrokeCap.Round
                        )
                    }
            ) {
                Text(
                    text = stringResource(R.string.or).lowercase(),
                    color = primary,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = 24.sp
                    ),
                    modifier = Modifier
                        .drawBehind {
                            drawRoundRect(
                                surface,
                                Offset(-24f, 0f),
                                Size(size.width + 48f, size.height),
                                CornerRadius(32f)
                            )
                        }
                )
            }

            Button(
                onClick = {
                    isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    if(isValidEmail) viewModel.signUpUser(email, password)
                },
                enabled = isValidEmail
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up_btn).uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
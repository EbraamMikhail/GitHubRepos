package com.example.githubrepos.ui.screens.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.githubrepos.R

@Composable
@Preview(showBackground = true)
fun CommonTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    imeActions: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    isSingleLine: Boolean = true,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit = {}
) {
    val isPasswordField = keyboardType == KeyboardType.Password

    var passwordVisibility: Boolean by remember {
        mutableStateOf(isPasswordField)
    }
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = placeholder) },
            isError = isError,
            maxLines = 1,
            singleLine = isSingleLine,
            leadingIcon = leadingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeActions),
            keyboardActions = onAction,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.secondary
            ),
            trailingIcon =
            if (isPasswordField && value.isNotEmpty()) {
                {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        val painter = if (passwordVisibility) {
                            painterResource(R.drawable.ic_eye)
                        } else {
                            painterResource(R.drawable.ic_no_eye)
                        }
                        Icon(
                            painter = painter,
                            contentDescription = "Password Icon"
                        )
                    }
                }
            } else {
                trailingIcon
            },
            shape = RoundedCornerShape(15.dp),
            modifier = modifier,
            visualTransformation = if (passwordVisibility.not()) VisualTransformation.None else PasswordVisualTransformation()
        )
        ErrorText(
            text = if (isError && errorMessage != null)
                errorMessage
            else "",
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun ErrorText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.error,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Text(
        text = text,
        color = textColor,
        style = textStyle,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}


@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    text: String = "button",
    icon: Painter? = null,
    iconOnEnd: Boolean = true,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    cornerRadius: Dp = 10.dp
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        if (iconOnEnd.not() && icon != null) {
            Icon(
                painter = icon,
                contentDescription = "button icon",
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        Text(text = text)

        if (iconOnEnd && icon != null)
            Icon(
                painter = icon,
                contentDescription = "button icon",
                modifier = Modifier.padding(start = 16.dp)
            )

    }
}
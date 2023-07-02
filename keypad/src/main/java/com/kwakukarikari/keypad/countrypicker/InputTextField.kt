package com.kwakukarikari.keypad.countrypicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nimdi.keypad.ui.theme.AppTheme

/**
 * Created by Kwaku Karikari on 01/07/2023.
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onTextChanged: (String) -> Unit,
    placeholder: String = "",
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    focusedIndicatorColor: Color = Color.LightGray,
    unfocusedIndicatorColor: Color = Color.White,
    containerColor: Color = Color.White
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onTextChanged,
            textStyle = TextStyle(
                fontWeight = FontWeight.Normal
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                )
            },
            modifier = Modifier
                .background(color = Color.Transparent)
                .defaultMinSize(minHeight = 30.dp)
                .fillMaxWidth()
                .border(
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(0.dp)
                ),
            maxLines = 1,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, autoCorrect = true),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = focusedIndicatorColor,
                unfocusedIndicatorColor = unfocusedIndicatorColor,
                containerColor = containerColor
                // containerColor = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.background
            ),
            leadingIcon = leadingIcon
        )
    }
}


@Composable
@Preview
fun InputTextFieldPreview() {
    AppTheme() {
        InputTextField(value = "",
            onTextChanged = {

            })
    }
}
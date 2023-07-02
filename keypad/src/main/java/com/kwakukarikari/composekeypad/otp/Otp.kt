package com.kwakukarikari.composekeypad.otp

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nimdi.keypad.ui.theme.AppTheme

/**
 * Created by Kwaku Karikari on 04/06/2023.
 */


@Composable
fun Otp(
    modifier: Modifier = Modifier,
    numberOfBoxes: Int = 4,
    initialValue: String = "",
    fontSize: TextUnit = 18.sp,
    textColor: Color = Color.Black,
    boxSize: Dp = 40.dp,
    activeColor: Color = Color.Black,
    boxBorderColor: Color = Color.Gray,
    onCompleted: (String) -> Unit,
    isError: MutableState<Boolean> = remember { mutableStateOf(false) }
) {

    var otpValue by remember { mutableStateOf("") }
    if (initialValue.isNotEmpty()) {
        otpValue = initialValue
    }

    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = otpValue,
            onValueChange = {
                if (it.length <= numberOfBoxes) otpValue = it

                if (otpValue.length == numberOfBoxes) {
                    onCompleted(otpValue)
                }
            },
            modifier = Modifier
                .testTag("Input"),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            cursorBrush = SolidColor(Color.Gray),
            decorationBox = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(numberOfBoxes) { index ->
                        val char = when {
                            index >= otpValue.length -> ""
                            else -> otpValue[index].toString()
                        }
                        val isFocused = otpValue.length == index

                        var borderColor = if (isFocused) activeColor else boxBorderColor

                        if (isError.value && otpValue.length == numberOfBoxes){
                            borderColor = Color.Red
                        }else{
                            isError.value = false
                        }

                        Box(
                            modifier = Modifier
                                .size(boxSize)
                                .border(
                                    width = if (isFocused) 2.dp else 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                textAlign = TextAlign.Center,
                                fontSize = fontSize,
                                color = if (isError.value) Color.Red else textColor
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))

                    }
                }
            }
        )
    }

}


@Composable
@Preview
fun OtpPreview() {
    AppTheme {
        Otp(
            initialValue = "12",
            onCompleted = {

            },
            isError = remember { mutableStateOf(true) }
        )
    }
}
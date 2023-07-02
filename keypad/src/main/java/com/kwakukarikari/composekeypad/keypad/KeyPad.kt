package com.kwakukarikari.composekeypad.keypad

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwakukarikari.composekeypad.join
import com.kwakukarikari.composekeypad.pop
import com.kwakukarikari.composekeypad.push
import com.nimdi.composekeypad.R
import com.nimdi.keypad.ui.theme.AppTheme
import kotlinx.coroutines.delay


/**
 * Created by Kwaku Karikari on 14/05/2023.
 */

@Composable
fun KeyPad(
    modifier: Modifier = Modifier,
    numberOfIndicators: Int = 4,
    backgroundColor: Color = Color.Transparent,
    keyBackgroundColor: Color = Color.Gray,
    keyTextColor: Color = Color.Black,
    indicatorColor: Color = Color.Transparent,
    activeIndicatorColor: Color = Color.Gray,
    onKeyPressed: (String) -> Unit = {},
    errorMessage: String = "",
    isError: MutableState<Boolean> = mutableStateOf(false),
    clear: MutableState<Boolean> = mutableStateOf(false)
) {
    val stack = remember {
        mutableStateListOf<String>()
    }

    onKeyPressed(stack.join())

    /**
     * This Clears the stack when clear is set to true
     */
    LaunchedEffect(clear.value){
        if (clear.value){
            delay(500)
            stack.clear()
            clear.value = false
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "#", "0", "back")

        Indicators(
            number = numberOfIndicators,
            indicated = stack.size,
            isError = isError.value,
            color = indicatorColor,
            activeIndicatorColor = activeIndicatorColor
        )

        Text(
            text = errorMessage,
            color = if (isError.value) Color.Red else Color.Transparent
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .background(color = backgroundColor),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            userScrollEnabled = false,
            contentPadding = PaddingValues(
                start = 45.dp,
                end = 45.dp,
                top = 10.dp,
                bottom = 10.dp
            )
        ) {
            items(numbers) { value ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    when (value) {
                        "back" -> {
                            KeyIcon(
                                number = "back",
                                iconColor = keyBackgroundColor,
                                onKeyPress = {
                                    if (stack.isNotEmpty()) {
                                        stack.pop()
                                    }
                                    if (stack.isNotEmpty() && isError.value) {
                                        stack.clear()
                                        isError.value = false
                                    }
                                }
                            )
                        }
                        "#" -> {
                            KeyIcon(
                                number = "clear",
                                iconColor = keyBackgroundColor,
                                onKeyPress = {
                                    if (stack.isNotEmpty()) {
                                        stack.clear()
                                    }
                                },
                                icon = ImageVector.vectorResource(id = R.drawable.ic_clear)
                            )
                        }
                        else -> {
                            Key(
                                number = value,
                                backgroundColor = keyBackgroundColor,
                                textColor = keyTextColor,
                                onKeyPress = {
                                    if (isError.value) {
                                        stack.clear()
                                        isError.value = false
                                    }

                                    if (stack.size < numberOfIndicators) {
                                        stack.push(it)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Indicators(
    number: Int,
    indicated: Int = 0,
    isError: Boolean = false,
    color: Color = Color.Transparent,
    activeIndicatorColor: Color = Color.Gray
) {
    Row {
        for (i in 1..number) {
            var indicatorColor: Color = color
            if (i <= indicated && !isError) {
                indicatorColor = activeIndicatorColor
            }
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(
                            1.dp,
                            color = if (isError) Color.Red else activeIndicatorColor
                        ),
                        shape = CircleShape
                    )
                    .background(color = indicatorColor)
                    .size(20.dp),
                content = {}
            )
        }
    }
}


@Composable
fun Key(
    modifier: Modifier = Modifier,
    number: String,
    onKeyPress: (String) -> Unit = {},
    textColor: Color = Color.Black,
    backgroundColor: Color = Color.Gray,
) {
    Box(
        modifier = modifier
            .padding(10.dp)
            .size(70.dp)
            .clip(CircleShape)
            .background(color = backgroundColor)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = rememberRipple(
                    bounded = true,
                    radius = 70.dp,
                    color = Color.White
                ),
                onClick = {
                    onKeyPress(number)
                },
                role = Role.Button
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            fontSize = 30.sp,
            color = textColor,
        )
    }
}

@Composable
fun KeyIcon(
    modifier: Modifier = Modifier,
    number: String,
    onKeyPress: (String) -> Unit = {},
    iconColor: Color = Color.Black,
    icon: ImageVector  = ImageVector.vectorResource(id = R.drawable.backspace)
) {
    Box(
        modifier = modifier
            .padding(10.dp)
            .size(70.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            modifier = Modifier
                .size(40.dp)
                .clickable(role = Role.Button, onClick = {
                    onKeyPress(number)
                }),
            contentDescription = null,
            tint = iconColor
        )
    }
}


@Composable
@Preview(showBackground = true)
fun IndicatorPreview() {
    AppTheme {
        Indicators(number = 4)
    }
}

@Composable
@Preview(showBackground = true)
fun KeyPreview() {
    AppTheme {
        Row() {
            Key(
                number = "8",
                onKeyPress = {
                }
            )

            KeyIcon(number = "&")
        }

    }
}

@Composable
@Preview(showBackground = true)
fun KeyPadPreview() {
    AppTheme {
        KeyPad()
    }
}
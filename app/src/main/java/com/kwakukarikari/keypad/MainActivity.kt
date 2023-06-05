package com.kwakukarikari.keypad

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwakukarikari.composekeypad.KeyPad
import com.kwakukarikari.composekeypad.Otp
import com.kwakukarikari.keypad.MainActivity.Constants.Grey_Light
import com.kwakukarikari.keypad.MainActivity.Constants.PIN
import com.kwakukarikari.keypad.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {

    object Constants {
        const val TAG: String = "MainActivity"
        const val PIN = "1234"

        val Grey_Light = Color(0xFFE1E5F2)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {

                val hasError = remember {
                    mutableStateOf(false)
                }

                Column(
                ) {
                    KeyPad(
                        modifier = Modifier.background(color = Color.Transparent),
                        onKeyPressed = { value ->
                            if (value.length == 4 && value != PIN) {
                                hasError.value = true
                            }
                        },
                        keyBackgroundColor = Grey_Light,
                    )

                    Otp(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally),
                        boxSize = 60.dp,
                        fontSize = 25.sp,
                        onCompleted = {
                            if (it != PIN){
                                hasError.value = true
                            }
                        },
                        isError = hasError
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyAppTheme() {
        Greeting("Android")
    }
}
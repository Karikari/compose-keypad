package com.nimdi.composekeypad

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import com.google.common.truth.Truth.assertThat
import com.kwakukarikari.keypad.otp.Otp
import com.nimdi.keypad.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

/**
 * Created by Kwaku Karikari on 15/05/2023.
 */

class OtpTest {

    @get:Rule
    val rule = createComposeRule()


    @Test
    fun test_that_otp_entered_is_correct() {
        var otpValue = ""
        rule.setContent {
            AppTheme {
                Otp(
                    numberOfBoxes = 4,
                    onCompleted = {
                        otpValue = it
                })
            }
        }
        rule.onNodeWithTag("Input").performTextInput("1234")

        rule.runOnIdle {
            assertThat(otpValue).isEqualTo("1234")
        }
    }

    @Test
    fun test_that_otp_enter_is_not_valid(){
        var errorValue = false
        rule.setContent {
            val error = remember { mutableStateOf(false) }
            errorValue = error.value
            AppTheme {
                Otp(
                    numberOfBoxes = 4,
                    onCompleted = {
                        if (it != "1234"){
                            error.value = true
                        }
                    },
                    isError = error
                )
            }
        }
        rule.onNodeWithTag("Input").performTextInput("1239")

        rule.runOnIdle {
            assertThat(errorValue).isEqualTo(true)
        }
    }

}
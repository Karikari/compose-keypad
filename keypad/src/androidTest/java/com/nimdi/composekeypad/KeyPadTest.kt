package com.nimdi.composekeypad

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import com.kwakukarikari.keypad.keypad.KeyPad
import com.nimdi.keypad.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

/**
 * Created by Kwaku Karikari on 15/05/2023.
 */

class KeyPadTest {

    @get:Rule
    val rule = createComposeRule()


    @Test
    fun test_that_keys_entered_is_correct() {
        var keyspressed = ""
        rule.setContent {
            AppTheme {
                KeyPad(
                    onKeyPressed = {
                        keyspressed = it
                    }
                )
            }
        }
        rule.onNodeWithText("2").performClick()
        rule.onNodeWithText("5").performClick()
        rule.onNodeWithText("8").performClick()
        rule.onNodeWithText("0").performClick()

        rule.runOnIdle {
            assertThat(keyspressed).isEqualTo("2580")
        }

    }

    @Test
    fun test_that_incorrect_pin_will_display_error_message() {

        val errorMessage = "Incorrect PIN, try again.."
        rule.setContent {
            val testNumber = "1234"
            val error: MutableState<Boolean> = remember {
                mutableStateOf(false)
            }

            KeyPad(
                onKeyPressed = {
                    if (it.length == 4 && it != testNumber) {
                        error.value = true
                    }

                },
                errorMessage = errorMessage,
                isError = error
            )
        }
        rule.onNodeWithText("2").performClick()
        rule.onNodeWithText("5").performClick()
        rule.onNodeWithText("8").performClick()
        rule.onNodeWithText("0").performClick()

        rule.onNode(hasTextExactly(errorMessage)).assertIsDisplayed()


    }
}
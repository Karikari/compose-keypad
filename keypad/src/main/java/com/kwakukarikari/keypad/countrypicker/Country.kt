package com.kwakukarikari.keypad.countrypicker

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Created by Kwaku Karikari on 14/02/2023.
 */
@Keep
data class Country(
    val name: String = "",
    @SerializedName("dial_code")
    val dialCode: String = "",
    val code: String = ""
    )

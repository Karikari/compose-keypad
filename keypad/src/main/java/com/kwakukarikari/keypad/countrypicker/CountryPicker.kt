package com.kwakukarikari.keypad.countrypicker

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kwakukarikari.keypad.FillAvailableSpace
import com.kwakukarikari.keypad.HorizontalSpacer
import com.kwakukarikari.keypad.R
import com.nimdi.keypad.ui.theme.AppTheme
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Created by Kwaku Karikari on 14/02/2023.
 */

@Composable
fun CountryPicker(
    modifier: Modifier = Modifier,
    defaultCountry: String = "GH",
    showFlag: Boolean = true,
    showCountryCode: Boolean = true,
    showDialCode: Boolean = true,
    masterCountries: List<String> = emptyList(),
    onCountrySelected: (Country) -> Unit = {}
) {
    val localContext = LocalContext.current

    val dialogState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    var countries by remember {
        mutableStateOf(getCountries(localContext))
    }
    var selectedCountry by remember {
        mutableStateOf(
            countries.find {
                it.code == defaultCountry
            }
        )
    }

    LaunchedEffect(true) {
        onCountrySelected(selectedCountry!!)
    }

    val onlyCountries by remember {
        mutableStateOf(
            countries.filter {
                masterCountries.contains(it.code)
            }
        )
    }

    if (onlyCountries.isNotEmpty()) {
        countries = onlyCountries
    }

    val flagName = "flag ".plus(selectedCountry?.name)

    val flag = flagName.split(" ").joinToString("_").lowercase()

    Row(
        modifier = modifier
            .padding(10.dp)
            .clickable {
                dialogState.value = true
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showFlag) {
            getDrawableId(localContext, flag)?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "Country",
                    modifier = Modifier
                        .size(25.dp)
                )
            }
            HorizontalSpacer(space = 5.dp)
        }
        if (showCountryCode) {
            Text(text = selectedCountry!!.code)
            HorizontalSpacer(space = 5.dp)
        }

        if (showDialCode) {
            Text(text = selectedCountry!!.dialCode)
        }
    }
    CountryPickerDialog(
        countries = countries,
        onCountrySelected = {
            selectedCountry = it
            onCountrySelected(it)
        },
        dialogState = dialogState
    )
}

@Composable
private fun CountryPickerDialog(
    countries: List<Country> = emptyList(),
    onCountrySelected: (Country) -> Unit = {},
    dialogState: MutableState<Boolean> = mutableStateOf(false)
) {
    val query = remember { mutableStateOf("") }
    val searchedCountries = remember { mutableStateOf(emptyList<Country>()) }

    if (query.value.isNotEmpty()) {
        searchedCountries.value = countries.filter {
            it.name.lowercase().contains(query.value.lowercase())
        }
    } else {
        searchedCountries.value = emptyList()
    }

    if (dialogState.value) {
        Dialog(
            onDismissRequest = {
                dialogState.value = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Column {
                InputTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onTextChanged = {
                        query.value = it
                    },
                    value = query.value,
                    placeholder = stringResource(id = R.string.search_here),
                    leadingIcon = {
                        Icon(
                            painter =
                            painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search"
                        )
                    }
                )
                LazyColumn {
                    val values = searchedCountries.value.ifEmpty { countries }
                    items(values) { country ->
                        CountryRow(
                            modifier = Modifier
                                .clickable {
                                    onCountrySelected(country)
                                    dialogState.value = false
                                },
                            country = country
                        )
                        HorizontalSpacer(space = 2.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CountryRow(
    modifier: Modifier = Modifier,
    country: Country
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .background(color = Color.White)
            .padding(start = 15.dp, end = 15.dp, bottom = 8.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val flagName = "flag ".plus(country.name)
        val flag = flagName.split(" ").joinToString("_").lowercase()

        getDrawableId(context, flag)?.let { painterResource(id = it) }?.let {
            Image(
                painter = it,
                contentDescription = "Country",
                modifier = Modifier
                    .size(25.dp)
            )
        }
        HorizontalSpacer(space = 5.dp)
        Text(
            text = country.name.plus(" (${country.code})"),
            fontSize = 15.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        FillAvailableSpace()
        Text(
            text = country.dialCode,
            fontSize = 15.sp,
            maxLines = 1
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun CountryPickerPreview() {
    AppTheme {
        CountryPicker()
    }
}

@Preview(showBackground = true)
@Composable
private fun CountryRowPreview() {
    AppTheme {
        CountryRow(country = Country("Ghana", "+233", "GH"))
    }
}

private fun getDrawableId(context: Context?, name: String): Int? {
    return context?.resources?.getIdentifier(name, "drawable", context.packageName)
}

private fun getCountries(context: Context?): List<Country> {
    return Gson().fromJson(loadJsonFromAsset(context), object : TypeToken<List<Country>>() {}.type)
}

private fun loadJsonFromAsset(context: Context?, filename: String = "country_codes.json"): String {
   return try {
        val inputStream = context!!.assets.open(filename)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, StandardCharsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        ""
    }
}
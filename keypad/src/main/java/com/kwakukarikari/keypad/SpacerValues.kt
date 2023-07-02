package com.kwakukarikari.keypad

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp


@Composable
fun VerticalSpacer(space: Dp) {
    Spacer(modifier = Modifier.height(space))
}

@Composable
fun HorizontalSpacer(space: Dp) {
    Spacer(modifier = Modifier.width(space))
}

@Composable
fun ColumnScope.FillAvailableSpace() {
    Spacer(modifier = Modifier.weight(1.0f))
}

@Composable
fun RowScope.FillAvailableSpace() {
    Spacer(modifier = Modifier.weight(1.0f))
}
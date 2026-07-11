package com.jetnsync.parkingapp.core.presentation


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacer util
 * @author zyzz
 */
@Composable
fun HorizontalSpace(
    width : Dp = 20.dp
) {
    Spacer(Modifier.width(width))
}

/**
 * Spacer util
 * @author zyzz
 */
@Composable
fun VerticalSpace(
    height : Dp = 20.dp
) {
    Spacer(Modifier.height(height))
}
package com.blankcat.teva.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.borderSide(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(16.dp),
    borderColor: Color,
    contentColor: Color,
): Modifier {
    return this then Modifier
        .background(borderColor, shape = shape)
        .padding(start = start, top = top, end = end, bottom = bottom)
        .background(contentColor, shape = shape)
}

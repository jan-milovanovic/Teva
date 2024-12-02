package com.blankcat.teva.ui.cart.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.blankcat.teva.utils.borderSide


@Composable
fun ShoppingList(listValue: String, onListUpdate: (String) -> Unit) {
    Text("Cart notes", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    BasicTextField(
        value = listValue,
        onValueChange = onListUpdate,
        minLines = 4,
        textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .fillMaxWidth()
            .borderSide(
                top = 2.dp,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                borderColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.background,
            )
            .padding(12.dp)
    )
}
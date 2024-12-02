package com.blankcat.teva.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateItemDialog(
    name: String,
    isDialogVisible: (Boolean) -> Unit,
    deleteItem: () -> Unit,
    updateItem: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { isDialogVisible(false) },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text("How would you like to modify $name?")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextButton(onClick = { isDialogVisible(false) }) {
                    Text("Cancel")
                }
               Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    updateItem()
                    isDialogVisible(false)
                }) {
                    Text("Update")
                }
                TextButton(onClick = {
                    deleteItem()
                    isDialogVisible(false)
                }) {
                    Text("Delete")
                }

            }
        }
    }
}

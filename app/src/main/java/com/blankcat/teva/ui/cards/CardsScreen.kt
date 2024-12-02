package com.blankcat.teva.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blankcat.teva.ui.cards.components.CardComposable
import com.blankcat.teva.components.AddItemModalSheet
import com.blankcat.teva.components.BarcodeNameSheetContent
import com.blankcat.teva.models.TevaBarcode
import com.blankcat.teva.models.TevaCollection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(viewModel: CardsScreenViewModel = hiltViewModel(), navigateToCamera: () -> Unit) {
    var showModalBottomSheet by remember { mutableStateOf(false) }
    var showRenameProductSheet by remember { mutableStateOf(false) }

    var barcode by remember { mutableStateOf<TevaBarcode?>(null) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val searchResults = viewModel.searchResults.collectAsStateWithLifecycle()
    val query = viewModel.searchQuery.collectAsStateWithLifecycle()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            OutlinedTextField(
                query.value,
                onValueChange = { viewModel.updateQuery(it) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, "search") },
                placeholder = { Text("Search ...") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(searchResults.value) {
                    CardComposable(
                        it,
                        deleteCard = { viewModel.deleteCard(it) },
                        updateCard = {
                            barcode = it
                            showRenameProductSheet = true
                        },
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { showModalBottomSheet = true },
            shape = CircleShape,
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }


    if (showModalBottomSheet) {
        AddItemModalSheet(
            isSheetVisible = { showModalBottomSheet = it },
            addBarcode = { viewModel.addCard(it) },
            navigateToCamera = { navigateToCamera.invoke() },
            barcodeType = TevaCollection.Card,
        )
    }

    if (showRenameProductSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRenameProductSheet = false },
            sheetState = bottomSheetState
        ) {
            BarcodeNameSheetContent(
                tevaBarcode = barcode,
                updateBarcode = {
                    viewModel.updateCard(it)
                    showRenameProductSheet = false
                },
                onClose = { showRenameProductSheet = false },
                barcodeType = TevaCollection.Card,
            )
        }
    }
}
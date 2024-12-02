package com.blankcat.teva.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.blankcat.teva.ui.navigation.NavigationItem
import com.blankcat.teva.utils.borderSide

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        modifier = Modifier.borderSide(
            top = 1.dp,
            shape = RectangleShape,
            borderColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

        val items = listOf(
            NavigationItem.Cart,
            NavigationItem.Cards,
            NavigationItem.Coupons,
        )

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(
                            if (index == selectedItemIndex) {
                                item.activeIcon
                            } else item.disabledIcon
                        ),
                        item.title
                    )
                },
                label = { Text(item.title) },
                alwaysShowLabel = true,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.route)
                    // {
                    //     popUpTo(navController.graph.findStartDestination().id) {
                    //         saveState = true
                    //     }
                    //     launchSingleTop = true
                    //     restoreState = true
                    // }
                },
                selected = currentRoute == item.route,
            )
        }
    }
}
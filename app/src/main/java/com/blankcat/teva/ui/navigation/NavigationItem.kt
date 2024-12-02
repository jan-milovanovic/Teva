package com.blankcat.teva.ui.navigation

import com.blankcat.teva.R
import kotlinx.serialization.Serializable

@Serializable
object AuthNav
@Serializable
object Register
@Serializable
object Login


@Serializable
object MainPage

@Serializable
object GroupList

@Serializable
object TevaCamera


sealed class NavigationItem(
    var route: String,
    var disabledIcon: Int,
    var activeIcon: Int,
    var title: String,
) {
    data object Cart : NavigationItem(
        route = "cart",
        disabledIcon = R.drawable.shopping_cart,
        activeIcon = R.drawable.shopping_cart_filled,
        title = "Cart",
    )

    data object Cards : NavigationItem(
        route = "cards",
        disabledIcon = R.drawable.cards,
        activeIcon = R.drawable.cards_filled,
        title = "Cards",
    )

    data object Coupons : NavigationItem(
        route = "coupons",
        disabledIcon = R.drawable.coupons,
        activeIcon = R.drawable.coupons_filled,
        title = "Coupons",
    )
}
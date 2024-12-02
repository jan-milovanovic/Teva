package com.blankcat.teva

import AppBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.blankcat.teva.components.BottomNavigationBar
import com.blankcat.teva.models.TevaCollection
import com.blankcat.teva.ui.camera.CameraScreen
import com.blankcat.teva.ui.auth.AuthViewModel
import com.blankcat.teva.ui.auth.LoginScreen
import com.blankcat.teva.ui.auth.RegisterScreen
import com.blankcat.teva.ui.cards.CardsScreen
import com.blankcat.teva.ui.cart.CartScreen
import com.blankcat.teva.ui.coupons.CouponsScreen
import com.blankcat.teva.ui.navigation.AuthNav
import com.blankcat.teva.ui.navigation.Login
import com.blankcat.teva.ui.navigation.MainPage
import com.blankcat.teva.ui.navigation.NavigationItem
import com.blankcat.teva.ui.navigation.Register
import com.blankcat.teva.ui.navigation.TevaCamera
import com.blankcat.teva.ui.theme.TevaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appViewModel.initializeAppData()

        enableEdgeToEdge()

        setContent {
            val appData by appViewModel.appData.collectAsState()

            TevaTheme(darkTheme = appData?.isDarkTheme ?: isSystemInDarkTheme()) {
                MyApp()
            }
        }
    }

    @Composable
    fun MyApp(viewModel: AuthViewModel = hiltViewModel()) {

        val isAuthenticated by viewModel.isAuthenticated().collectAsStateWithLifecycle()
        val navController = rememberNavController()

        var barcodeType by remember { mutableStateOf<TevaCollection>(TevaCollection.Card) }

        NavHost(
            navController = navController,
            startDestination = if (isAuthenticated) MainPage else AuthNav,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
        ) {
            navigation<AuthNav>(startDestination = Login) {
                composable<Login> {
                    LoginScreen(navigateToRegister = { navController.navigate(Register) })
                }
                composable<Register> {
                    RegisterScreen(navController)
                }
            }
            composable<MainPage> {
                MainNav(
                    mainNavigator = navController,
                    barcodeType = { barcodeType = it },
                    authViewModel = viewModel,
                )
            }
            composable<TevaCamera> {
                CameraScreen(
                    onClose = { navController.popBackStack() },
                    barcodeType = barcodeType
                )
            }
        }
    }
}

@Composable
fun MainNav(
    authViewModel: AuthViewModel = hiltViewModel(),
    appViewModel: AppViewModel = hiltViewModel(),
    mainNavigator: NavHostController,
    barcodeType: (TevaCollection) -> Unit,
) {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val appData by appViewModel.appData.collectAsState()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Set dark theme")
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = appData?.isDarkTheme ?: false,
                            onCheckedChange = { isChecked ->
                                appViewModel.toggleDarkTheme(isChecked)
                            })
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {
                        authViewModel.logout()
                        // mainNavigator.navigate(Login)
                    }) { Text("Log out") }
                }
            }
        }) {
        Scaffold(
            topBar = {
                AppBar(
                    navController,
                    openDrawer = { scope.launch { drawerState.open() } })
            },
            bottomBar = { BottomNavigationBar(navController) },

            contentWindowInsets = WindowInsets(0.dp),
        ) { padding ->
            Box(
                modifier = Modifier.padding(
                    PaddingValues(
                        start = padding.calculateStartPadding(LayoutDirection.Ltr),
                        top = padding.calculateTopPadding() - WindowInsets.systemBars.asPaddingValues()
                            .calculateTopPadding(),
                        end = padding.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = padding.calculateBottomPadding()
                    )
                )
            ) {
                NavHost(
                    navController = navController, startDestination = NavigationItem.Cart.route,
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                ) {
                    composable(NavigationItem.Cart.route) {
                        CartScreen()
                    }
                    composable(NavigationItem.Cards.route) {
                        CardsScreen(navigateToCamera = {
                            barcodeType(TevaCollection.Card)
                            mainNavigator.navigate(TevaCamera)
                        })
                    }
                    composable(NavigationItem.Coupons.route) {
                        CouponsScreen(navigateToCamera = {
                            barcodeType(TevaCollection.Coupon)
                            mainNavigator.navigate(TevaCamera)
                        })
                    }
                }
            }
        }

    }

}




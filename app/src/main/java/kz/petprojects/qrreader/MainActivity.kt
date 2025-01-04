package kz.petprojects.qrreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kz.petprojects.qrreader.ui.qr.HomeScreen
import kz.petprojects.qrreader.ui.qr.QRCodeScanner
import kz.petprojects.qrreader.ui.qr.TicketDetailsScreen
import kz.petprojects.qrreader.ui.theme.QrReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QrReaderTheme {
                QRReaderNavGraph()
            }
        }
    }
}

@Composable
fun QRReaderNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "home_screen"
    ) {
        composable("home_screen") {
            HomeScreen(navController)
        }
        composable("details_screen") {
            TicketDetailsScreen()
        }
    }
}
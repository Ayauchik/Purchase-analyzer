package kz.petprojects.qrreader.ui.qr

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: TicketViewModel = get()
) {
    var qrCodeContent by remember { mutableStateOf("") }
    var hasCameraPermission by remember { mutableStateOf(false) }

    if (!hasCameraPermission) {
        RequestPermission(
            permission = Manifest.permission.CAMERA,
            onPermissionGranted = { hasCameraPermission = true }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Fiscal Receipt Scanner") }) }
    ) { paddingValues ->
        if (!hasCameraPermission) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Camera permission is required to scan QR codes.")
            }
        } else {
            if (qrCodeContent.isEmpty()) {
                QRCodeScanner(onQRCodeScanned = { content ->
                    qrCodeContent = content
//                    val registrationNumber = "Extract from content"
//                    val ticketNumber = "Extract from content"
//                    val ticketDate = "Extract from content"
//
//                    viewModel.fetchData(registrationNumber, ticketNumber, ticketDate)
//                    navController.navigate("details_screen")

                    val extractedData = extractDataFromUrl(content)
                    Log.e("Home Screen", extractedData?.third.toString())
                    if (extractedData != null) {
                        val (registrationNumber, ticketNumber, ticketDate) = extractedData
                        viewModel.fetchData(registrationNumber, ticketNumber, ticketDate)
                        navController.navigate("details_screen")
                    } else {
//                        Toast.makeText(
//                            LocalContext.current,
//                            "Invalid QR Code format",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                })
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Scan a QR code to view details.")
                }
            }
        }
    }
}


fun extractDataFromUrl(url: String): Triple<String, String, String>? {
    try {
        val uri = android.net.Uri.parse(url)
        val registrationNumber = uri.getQueryParameter("f") ?: return null
        val ticketNumber = uri.getQueryParameter("i") ?: return null
        val ticketDate = uri.getQueryParameter("t")?.let { formatTicketDate(it) } ?: return null
        return Triple(registrationNumber, ticketNumber, ticketDate)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}


fun formatTicketDate(rawDate: String): String {
    return try {
        val inputFormat = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
        val outputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val date = inputFormat.parse(rawDate)
        outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

@Composable
fun RequestPermission(permission: String, onPermissionGranted: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) onPermissionGranted()
    }

    SideEffect {
        launcher.launch(permission)
    }
}

@Composable
fun CopyToClipboardButton(textToCopy: String) {
    val context = LocalContext.current
    val clipboardManager = remember { context.getSystemService(ClipboardManager::class.java) }
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            val clip = ClipData.newPlainText("Copied Text", textToCopy)
            clipboardManager.setPrimaryClip(clip)
            coroutineScope.launch {
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Text("Copy to Clipboard")
    }
}

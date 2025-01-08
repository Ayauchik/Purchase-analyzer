package kz.petprojects.qrreader.ui.qr

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.ClickableText
import org.koin.androidx.compose.get

@Composable
fun TicketDetailsScreen(
    viewModel: TicketViewModel = get()
) {
    val ticketState = viewModel.ticketState.collectAsState().value
    val context = LocalContext.current
    // Remember scroll state
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)  // Enable vertical scrolling
    ) {
        Text(text = "Company: ${ticketState.where ?: "N/A"}")
        Text(text = "Time: ${ticketState.`when` ?: "N/A"}")

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Products:")
        ticketState.products?.forEach { product ->
            Text(text = "Name: ${product.name}\nPrice: ${product.price}₸ \n")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Total Price: ${ticketState.totalPrice ?: "N/A"}₸")

        Spacer(modifier = Modifier.height(16.dp))

        // Display the label "Ticket URL:" and then the clickable URL
        val ticketUrl = ticketState.ticketUrl
        if (!ticketUrl.isNullOrEmpty()) {
            Row {
                // Regular label text "Ticket URL:"
                Text(text = "Ticket URL: ")

                // Clickable blue text for the URL
                val annotatedString = buildAnnotatedString {
                    append(ticketUrl)
                    addStringAnnotation(
                        tag = "URL",
                        annotation = ticketUrl,
                        start = 0,
                        end = ticketUrl.length
                    )
                }

                // Display clickable URL as blue text
                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations("URL", offset, offset)
                            .firstOrNull()?.let { annotation ->
                                openWebPage(context, annotation.item)
                            }
                    },
                    style = androidx.compose.ui.text.TextStyle(color = Color.Blue)
                )
            }
        } else {
            // If there's no URL, show N/A
            Text(text = "Ticket URL: N/A")
        }
    }
}

/**
 * Function to open URL in a web browser
 */
private fun openWebPage(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Error opening URL", Toast.LENGTH_SHORT).show()
    }
}

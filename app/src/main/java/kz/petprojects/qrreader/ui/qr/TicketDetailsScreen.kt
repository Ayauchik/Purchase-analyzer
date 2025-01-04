package kz.petprojects.qrreader.ui.qr

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.get

@Composable
fun TicketDetailsScreen(
    viewModel: TicketViewModel = get()
) {
    val ticketState = viewModel.ticketState.collectAsState().value

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Company: ${ticketState.where ?: "N/A"}")
        Text(text = "Time: ${ticketState.`when` ?: "N/A"}")

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Products:")
        ticketState.products?.forEach { product ->
            Text(text = "Name ${product.name}, Price: ${product.price}₸ \n")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Total Price: ${ticketState.totalPrice ?: "N/A"}₸")
        Text(text = "Ticket URL: ${ticketState.ticketUrl ?: "N/A"}")
    }
}

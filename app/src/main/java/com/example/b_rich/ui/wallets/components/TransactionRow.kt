

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.TransactionSolana
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

@SuppressLint("DefaultLocale")
@Composable
fun TransactionRow(transaction: TransactionSolana) {
    // Determine if transaction is incoming or outgoing based on type and addresses
    val isIncoming = transaction.toAddress == transaction.walletPublicKey
    val backgroundColor = when {
        isIncoming -> Color(0xFFE8F5E9) // Green background for incoming
        else -> Color(0xFFFFEBEE) // Red background for outgoing
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row with Amount and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Amount with SOL symbol
                Text(
                    text = "${if (isIncoming) "+" else "-"}${String.format("%.6f", abs(transaction.amount))} SOL",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isIncoming) Color(0xFF00C853) else Color(0xFFFF1744)
                )

                // Status Chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (transaction.status.lowercase()) {
                        "success" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                        "pending" -> Color(0xFFFFC107).copy(alpha = 0.2f)
                        else -> Color(0xFFFF5252).copy(alpha = 0.2f)
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = transaction.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = when (transaction.status.lowercase()) {
                            "success" -> Color(0xFF1B5E20)
                            "pending" -> Color(0xFF795548)
                            else -> Color(0xFFB71C1C)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Transaction Details
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Transaction Type and Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = transaction.type.capitalize(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF3D5AFE)
                    )
                    Text(
                        text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                            .format(transaction.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                // Transaction Fee
                if (transaction.fee > 0) {
                    Text(
                        text = "Fee: ${String.format("%.5f", transaction.fee)} SOL",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                // Show abbreviated addresses
                Text(
                    text = buildString {
                        append("From: ${transaction.fromAddress?.take(6)}...${transaction.fromAddress?.takeLast(4)}")
                        append("\nTo: ${transaction.toAddress?.take(6)}...${transaction.toAddress?.takeLast(4)}")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// Extension function to capitalize first letter
private fun String.capitalize() = this.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}
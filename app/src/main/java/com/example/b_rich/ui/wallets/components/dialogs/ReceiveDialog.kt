package com.example.b_rich.ui.wallets.components.dialogs

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import android.graphics.Color as AndroidColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiveDialog(
    selectedWallet: Wallet,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Generate the QR code based on the wallet's public key
    LaunchedEffect(selectedWallet.publicKey) {
        qrCodeBitmap = generateQRCode(selectedWallet.publicKey, 300, 300)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "QR Code Icon",
                    tint = Color(0xFF3D5AFE)
                )
                Text(
                    text = "Receive Funds",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF3D5AFE)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Scan this QR code to send funds to your wallet.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )

                // Display the QR Code or a loading indicator
                qrCodeBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Wallet Receive QR Code",
                        modifier = Modifier
                            .size(250.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, Color(0xFF3D5AFE), RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(8.dp)
                    )
                } ?: CircularProgressIndicator(color = Color(0xFF3D5AFE))

                // Display the wallet address
                Text(
                    text = "Wallet Address: ${selectedWallet.publicKey}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
            ) {
                Text("Done", color = Color.White)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
// Fonction de génération de QR Code (à implémenter selon vos besoins)

fun generateQRCode(data: String, width: Int, height: Int): Bitmap? {
    return try {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix: BitMatrix = multiFormatWriter.encode(
            data,
            BarcodeFormat.QR_CODE,
            width,
            height
        )

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            style = Paint.Style.FILL
        }

        for (x in 0 until width) {
            for (y in 0 until height) {
                paint.color = if (bitMatrix.get(x, y)) AndroidColor.BLACK else AndroidColor.WHITE
                canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
            }
        }

        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
package com.example.b_rich.ui.AddAccount.componenets

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.util.Size
import android.view.Surface
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.util.concurrent.Executors

@Composable
fun RibScannerDialog(
    onRibDetected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var isProcessing by remember { mutableStateOf(false) }
    var overlayText by remember { mutableStateOf("Placez votre RIB dans le cadre") }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // Configuration améliorée de la capture d'image
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setTargetRotation(Surface.ROTATION_0)
            .setTargetResolution(Size(2400, 1800)) // Résolution plus élevée
            .setJpegQuality(100) // Qualité maximale
            .build()
    }



    val textRecognizer = remember {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    // Permission de la caméra
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Log.d("RibScanner", "Camera permission denied")
            onDismiss()
        } else {
            Log.d("RibScanner", "Camera permission granted")
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Prévisualisation de la caméra
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                        }.also { previewView ->
                            val preview = Preview.Builder()
                                .setTargetResolution(Size(1920, 1080))
                                .build()
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            try {
                                val cameraProvider = cameraProviderFuture.get()
                                cameraProvider.unbindAll()

                                val camera = cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageCapture
                                )

                                // Configuration de l'autofocus
                                camera.cameraControl.enableTorch(false)
                                camera.cameraControl.setLinearZoom(0f)

                                preview.setSurfaceProvider(previewView.surfaceProvider)

                            } catch (e: Exception) {
                                Log.e("RibScanner", "Camera initialization error", e)
                                e.printStackTrace()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay pour guider l'utilisateur
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Guide visuel amélioré
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(180.dp)
                            .align(Alignment.Center)
                            .border(
                                width = 2.dp,
                                color = if (isProcessing)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )

                    // Instructions plus claires
                    Text(
                        text = "Alignez votre RIB dans le cadre",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 32.dp)
                    )

                    // Bouton de fermeture
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer",
                            tint = Color.White
                        )
                    }

                    // Instructions et état
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 160.dp)
                            .fillMaxWidth(0.9f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = overlayText,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            if (isProcessing) {
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .fillMaxWidth(0.8f)
                                )
                            }
                        }
                    }

                    // Bouton de capture amélioré
                    Button(
                        onClick = {
                            isProcessing = true
                            overlayText = "Analyse en cours..."
                            Log.d("RibScanner", "Starting image capture")

                            val outputDirectory = context.getExternalFilesDir(null)
                            val photoFile = File(
                                outputDirectory,
                                "RIB_${System.currentTimeMillis()}.jpg"
                            )

                            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                                .setMetadata(
                                    ImageCapture.Metadata().apply {
                                        isReversedHorizontal = false
                                    }
                                )
                                .build()

                            imageCapture.takePicture(
                                outputFileOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                        Log.d("RibScanner", "Image saved successfully")
                                        try {
                                            val image = InputImage.fromFilePath(context, Uri.fromFile(photoFile))

                                            textRecognizer.process(image)
                                                .addOnSuccessListener { visionText ->
                                                    Log.d("RibScanner", "Full text detected: ${visionText.text}")

                                                    // Extraction améliorée du RIB
                                                    val text = visionText.text.replace(" ", "")
                                                    val ribPattern = Regex("\\d{20}")
                                                    val matches = ribPattern.findAll(text)
                                                    var ribFound = false

                                                    matches.forEach { match ->
                                                        val potentialRib = match.value
                                                        Log.d("RibScanner", "Found potential RIB: $potentialRib")
                                                        if (potentialRib.length == 20) {
                                                            ribFound = true
                                                            onRibDetected(potentialRib)
                                                        }
                                                    }

                                                    if (!ribFound) {
                                                        // Si aucun RIB n'est trouvé, chercher des séquences de chiffres
                                                        val numbers = text.replace("[^0-9]".toRegex(), "")
                                                        if (numbers.length >= 20) {
                                                            val extractedRib = numbers.substring(0, 20)
                                                            Log.d("RibScanner", "Extracted RIB from numbers: $extractedRib")
                                                            onRibDetected(extractedRib)
                                                        } else {
                                                            Log.d("RibScanner", "No valid RIB found in text")
                                                            overlayText = "Aucun RIB trouvé. Réessayez."
                                                        }
                                                    }
                                                    isProcessing = false
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("RibScanner", "Text recognition failed", e)
                                                    overlayText = "Erreur de lecture: ${e.localizedMessage}"
                                                    isProcessing = false
                                                }
                                        } catch (e: Exception) {
                                            Log.e("RibScanner", "Error processing image", e)
                                            overlayText = "Erreur de traitement: ${e.localizedMessage}"
                                            isProcessing = false
                                        } finally {
                                            photoFile.delete()
                                        }
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        Log.e("RibScanner", "Image capture error", exception)
                                        overlayText = "Erreur de capture: ${exception.message}"
                                        isProcessing = false
                                    }
                                }
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 80.dp)
                            .fillMaxWidth(0.8f),
                        enabled = !isProcessing
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isProcessing) "Analyse en cours..." else "Capturer",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
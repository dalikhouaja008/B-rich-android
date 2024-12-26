package com.example.b_rich.ui.AddAccount.componenets

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
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

// Définition du composant RibScannerDialog
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
    val imageCapture = remember { ImageCapture.Builder().build() }
    val textRecognizer = remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }

    // Permission de la caméra
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            onDismiss()
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
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                        }.also { previewView ->
                            val preview = Preview.Builder().build()
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

                                preview.setSurfaceProvider(previewView.surfaceProvider)

                            } catch (e: Exception) {
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
                    // Guide visuel
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

                    // Bouton de capture
                    Button(
                        onClick = {
                            isProcessing = true
                            overlayText = "Analyse en cours..."

                            val outputDirectory = context.getExternalFilesDir(null)
                            val photoFile = File(
                                outputDirectory,
                                "RIB_${System.currentTimeMillis()}.jpg"
                            )

                            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                            imageCapture.takePicture(
                                outputFileOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                        val image = InputImage.fromFilePath(context, Uri.fromFile(photoFile))
                                        textRecognizer.process(image)
                                            .addOnSuccessListener { visionText ->
                                                val ribPattern = Regex("\\d{20}")
                                                val matches = ribPattern.findAll(visionText.text)
                                                var ribFound = false

                                                matches.forEach { match ->
                                                    val potentialRib = match.value
                                                    if (potentialRib.length == 20) {
                                                        ribFound = true
                                                        onRibDetected(potentialRib)
                                                    }
                                                }

                                                if (!ribFound) {
                                                    overlayText = "Aucun RIB trouvé. Réessayez."
                                                }
                                                isProcessing = false
                                            }
                                            .addOnFailureListener { e ->
                                                overlayText = "Erreur de lecture: ${e.localizedMessage}"
                                                isProcessing = false
                                            }
                                        photoFile.delete() // Nettoyage du fichier temporaire
                                    }

                                    override fun onError(exception: ImageCaptureException) {
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

@OptIn(ExperimentalGetImage::class)
@Composable
private fun CameraPreview(
    onRibDetected: (String) -> Unit,
    onProcessing: (Boolean) -> Unit,
    onOverlayTextChange: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val textRecognizer = remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }
    var isAnalyzing by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            onOverlayTextChange("Permission de caméra requise")
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { previewView ->
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            var lastAnalysisTimestamp = 0L
            val analysisCooldown = 1000L // Augmenté à 1 seconde

            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(context)
            ) { imageProxy ->
                val currentTimestamp = System.currentTimeMillis()
                if (currentTimestamp - lastAnalysisTimestamp >= analysisCooldown && !isAnalyzing) {
                    isAnalyzing = true
                    lastAnalysisTimestamp = currentTimestamp

                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        onProcessing(true)
                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        textRecognizer.process(image)
                            .addOnSuccessListener { visionText ->
                                val text = visionText.text
                                // Recherche de séquences de 20 chiffres
                                val ribPattern = Regex("\\d{20}")
                                val matches = ribPattern.findAll(text)
                                var ribFound = false

                                matches.forEach { match ->
                                    val potentialRib = match.value
                                    if (potentialRib.length == 20) {
                                        ribFound = true
                                        onOverlayTextChange("RIB détecté !")
                                        onRibDetected(potentialRib)
                                    }
                                }

                                if (!ribFound && text.isNotEmpty()) {
                                    onOverlayTextChange("Placez votre RIB dans le cadre")
                                }
                            }
                            .addOnFailureListener { e ->
                                onOverlayTextChange("Erreur de lecture: ${e.localizedMessage}")
                            }
                            .addOnCompleteListener {
                                onProcessing(false)
                                isAnalyzing = false
                                imageProxy.close()
                            }
                    } else {
                        imageProxy.close()
                        isAnalyzing = false
                    }
                } else {
                    imageProxy.close()
                }
            }

            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                imageAnalysis,
                preview
            )

            preview.setSurfaceProvider(previewView.surfaceProvider)

        } catch (e: Exception) {
            onOverlayTextChange("Erreur: ${e.localizedMessage}")
        }
    }
}
package com.example.b_rich.ui.components.Charts

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LineChartComponent(
    currencyConverterViewModel: CurrencyConverterViewModel,
    toCurrency: String
) {
    // Collecter les prédictions en tant que state
    val predictions by currencyConverterViewModel.predictions.collectAsState(initial = emptyMap())
    // Effect to load predictions when currency changes
    LaunchedEffect(predictions) {
        Log.d("LineChartComponent", "Predictions for $toCurrency: $predictions")
    }
    // State to track prediction loading
    val isLoadingPredictions by currencyConverterViewModel.isLoadingPredictions.collectAsState()
    LaunchedEffect(toCurrency) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        currencyConverterViewModel.loadPredictions(currentDate, listOf(toCurrency))
    }

    // Get prediction data for current currency
    val currencyData = predictions[toCurrency] ?: emptyList()
    // Conteneur pour le graphique
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            // Priorité au chargement - montrer le spinner même si les données sont vides
            isLoadingPredictions -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            // Si pas de données après le chargement
            currencyData.isEmpty() && !isLoadingPredictions -> {
                Text(
                    text = "No predictions available for $toCurrency",
                    color = MaterialTheme.colorScheme.error
                )
            }
            // Afficher le graphique si des données existent
            else -> {
                // Préparer les données pour le graphique
                val pointsData = currencyData.mapIndexed { index, prediction ->
                    Point(index.toFloat(), prediction.value.toFloat())
                }

                // Calculer les étapes dynamiquement
                val steps = minOf(5, pointsData.size - 1)

                // Axe X
                val xAxisData = AxisData.Builder()
                    .axisStepSize(100.dp)
                    .backgroundColor(Color.Transparent)
                    .steps(pointsData.size - 1)
                    .labelData { i ->
                        currencyData.getOrNull(i)?.date ?: ""
                    }
                    .labelAndAxisLinePadding(15.dp)
                    .axisLineColor(MaterialTheme.colorScheme.tertiary)
                    .axisLabelColor(MaterialTheme.colorScheme.tertiary)
                    .build()

                // Axe Y avec échelle dynamique
                val yAxisData = AxisData.Builder()
                    .steps(steps)
                    .backgroundColor(Color.Transparent)
                    .labelAndAxisLinePadding(20.dp)
                    .labelData { i ->
                        val minValue = pointsData.minOfOrNull { it.y } ?: 0f
                        val maxValue = pointsData.maxOfOrNull { it.y } ?: 0f
                        val yScale = (maxValue - minValue) / steps
                        "%.4f".format(minValue + i * yScale)
                    }
                    .axisLineColor(MaterialTheme.colorScheme.tertiary)
                    .axisLabelColor(MaterialTheme.colorScheme.tertiary)
                    .build()

                // Configuration du graphique
                val lineChartData = LineChartData(
                    linePlotData = LinePlotData(
                        lines = listOf(
                            Line(
                                dataPoints = pointsData,
                                LineStyle(
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                IntersectionPoint(
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                                SelectionHighlightPoint(),
                                ShadowUnderLine(),
                                SelectionHighlightPopUp()
                            )
                        ),
                    ),
                    xAxisData = xAxisData,
                    yAxisData = yAxisData,
                    gridLines = GridLines(),
                    backgroundColor = Color.White
                )

                // Affichage du graphique
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    lineChartData = lineChartData
                )
            }
        }
    }
}
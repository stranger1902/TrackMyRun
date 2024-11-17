package com.example.trackmyrun.run_statistics.presentation.component

import androidx.compose.runtime.saveable.rememberSaveable
import com.example.trackmyrun.core.theme.TrackMyRunTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.geometry.Offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.Canvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.drawText
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

enum class OrderBy {
    START, END
}

data class LineGraphConfig(
    val minHorizontalPaddingLabelsX: Dp,
    val labelsAxisXTextStyle: TextStyle,
    val labelsAxisYTextStyle: TextStyle,
    val minVerticalPaddingLabelsY: Dp,
    val contentPadding: Dp,
    val borderColor: Color,
    val pointColor: Color,
    val pathColor: Color,
    val lineColor: Color,
    val orderBy: OrderBy
)

private fun getIndex(index: Int, size: Int, orderBy: OrderBy): Int {
    return when(orderBy) {
        OrderBy.END -> size - 1 - index
        OrderBy.START -> index
    }
}

private fun getShift(value: Int, size: Int, orderBy: OrderBy): Int {
    return when(orderBy) {
        OrderBy.END -> -(size - value)
        OrderBy.START -> size - value
    }
}

@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    labelsAxisX: List<String>,
    isDebug: Boolean = false,
    data: List<Float>,
    lineGraphConfig: LineGraphConfig = LineGraphConfig(
        borderColor = MaterialTheme.colorScheme.inverseOnSurface,
        pathColor = MaterialTheme.colorScheme.onSurfaceVariant,
        lineColor = MaterialTheme.colorScheme.onSurface,
        pointColor = MaterialTheme.colorScheme.primary,
        minHorizontalPaddingLabelsX = 16.dp,
        minVerticalPaddingLabelsY = 24.dp,
        contentPadding = 8.dp,
        orderBy = OrderBy.END,
        labelsAxisXTextStyle = TextStyle(
            background = if (isDebug) MaterialTheme.colorScheme.onSurfaceVariant else Color.Unspecified,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        ),
        labelsAxisYTextStyle = TextStyle(
            background = if (isDebug) MaterialTheme.colorScheme.onSurfaceVariant else Color.Unspecified,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    )
) {

    if (data.isEmpty())
        Box(
            modifier = modifier
        ).also { return }

    if (labelsAxisX.size != data.size) throw RuntimeException("axisX must contain the same number of data items list")

    if (data.size < 2) throw RuntimeException("Graph must contains at least 2 items")

    var minValueLabelAxisY by rememberSaveable {
        mutableFloatStateOf(0f)
    }

    val maxValue = rememberSaveable(data) {
        data.max()
    }

    val minValue = rememberSaveable(data) {
        data.min()
    }

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
    ) {

        drawRect(
            color = lineGraphConfig.borderColor,
            topLeft = Offset(0f, 0f),
            style = Stroke(8f),
            size = Size(
                height = size.height,
                width = size.width
            )
        )

        inset(
            inset = lineGraphConfig.contentPadding.toPx()
        ) {

            val textMeasureResultLabelsAxisX = labelsAxisX.map {
                textMeasurer.measure(
                    style = lineGraphConfig.labelsAxisXTextStyle,
                    text = it
                )
            }

            val maxValueTextMeasureResultLabelsAxisY = textMeasurer.measure(
                text = "%.2f".format(maxValue),
                style = lineGraphConfig.labelsAxisYTextStyle,
            )

            val maxHeightLabelAxisX = textMeasureResultLabelsAxisX.maxOf { it.size.height }

            val maxHeightLabelAxisY = maxValueTextMeasureResultLabelsAxisY.size.height
            val maxWidthLabelAxisY = maxValueTextMeasureResultLabelsAxisY.size.width

            val heightLabelsAxisY = maxHeightLabelAxisY.toFloat()
            val widthLabelsAxisY = maxWidthLabelAxisY.toFloat()

            val viewPortHeightLabelsAxisY = size.height - maxHeightLabelAxisX
            val viewPortWidthLabelsAxisX = size.width - maxWidthLabelAxisY

            if (isDebug)
                drawRect(
                    topLeft = Offset(0f, 0f),
                    style = Stroke(4f),
                    color = Color.Red,
                    size = Size(
                        height = viewPortHeightLabelsAxisY,
                        width = widthLabelsAxisY
                    )
                )

            var spacePx = lineGraphConfig.minVerticalPaddingLabelsY.toPx() / 2f

            var containerHeightLabelY = heightLabelsAxisY + (spacePx * 2f)

            val counterLabelsY = ((viewPortHeightLabelsAxisY - spacePx) / containerHeightLabelY).roundToInt()

            val step = ((maxValue - minValue) / (counterLabelsY - 1))

            val spaceFilledByLabelsY = containerHeightLabelY * counterLabelsY.toFloat()
            val spaceRemained = viewPortHeightLabelsAxisY - spaceFilledByLabelsY

            minValueLabelAxisY = maxValue - (step * (counterLabelsY - 1))

            if (isDebug)
                println("DEBUG -> viewPortHeightLabelsAxisY: $viewPortHeightLabelsAxisY - spaceFilledByLabelsY: $spaceFilledByLabelsY - spaceRemained: ($spaceRemained) - containerHeightLabelY: $containerHeightLabelY - counter: $counterLabelsY - maxValue: ${maxValue} - minValue: ${minValue} - step: $step - minValueLabelAxisY: $minValueLabelAxisY")

            spacePx += spaceRemained / (counterLabelsY * 2f)
            containerHeightLabelY = heightLabelsAxisY + (spacePx * 2f)

            (0..< counterLabelsY).forEach { i ->

                val index = getIndex(i, counterLabelsY, lineGraphConfig.orderBy)

                val measureResult = textMeasurer.measure(
                    text = "%.2f".format(maxValue - (step * index)),
                    style = lineGraphConfig.labelsAxisYTextStyle
                )

                if (isDebug) {

                    drawRect(
                        style = Stroke(4f),
                        color = Color.Yellow,
                        topLeft = Offset(
                            y = spacePx + (index * containerHeightLabelY),
                            x = 0f,
                        ),
                        size = Size(
                            height = heightLabelsAxisY,
                            width = widthLabelsAxisY
                        )
                    )

                    drawLine(
                        color = lineGraphConfig.lineColor,
                        start = Offset(
                            y = spacePx + (index * containerHeightLabelY) + (measureResult.size.height / 2f),
                            x = widthLabelsAxisY
                        ),
                        end = Offset(
                            y = spacePx + (index * containerHeightLabelY) + (heightLabelsAxisY / 2f),
                            x = viewPortWidthLabelsAxisX + widthLabelsAxisY
                        )
                    )
                }

                drawText(
                    textLayoutResult = measureResult,
                    topLeft = Offset(
                        x = (widthLabelsAxisY - measureResult.size.width) / 2f,
                        y = spacePx + (index * containerHeightLabelY),
                    )
                )
            }

            val maxWidthLabelAxisX = textMeasureResultLabelsAxisX.maxOf { it.size.width }

            val heightLabelsAxisX = maxHeightLabelAxisX.toFloat()
            val widthLabelsAxisX = maxWidthLabelAxisX.toFloat()

            if (isDebug)
                drawRect(
                    style = Stroke(4f),
                    color = Color.Red,
                    size = Size(
                        width = viewPortWidthLabelsAxisX,
                        height = heightLabelsAxisX
                    ),
                    topLeft = Offset(
                        x = maxWidthLabelAxisY.toFloat(),
                        y = viewPortHeightLabelsAxisY
                    )
                )

            var spacePx2 = lineGraphConfig.minHorizontalPaddingLabelsX.toPx() / 2f

            var containerWidthLabelX = widthLabelsAxisX + (spacePx2 * 2f)

            val counterLabelsX = ((viewPortWidthLabelsAxisX - spacePx2) / containerWidthLabelX).roundToInt().coerceAtMost(data.size)

            val spaceFilledByLabelsX = (containerWidthLabelX * counterLabelsX)
            val spaceRemained2 = viewPortWidthLabelsAxisX - spaceFilledByLabelsX

            if (isDebug)
                println("DEBUG -> viewPortWidthLabelsAxisX: $viewPortWidthLabelsAxisX - spaceFilledByLabelsX: $spaceFilledByLabelsX - spaceRemained2: $spaceRemained2 - containerWidthLabelX: $containerWidthLabelX - counter: $counterLabelsX")

            spacePx2 += spaceRemained2 / (counterLabelsX * 2f)
            containerWidthLabelX = widthLabelsAxisX + (spacePx2 * 2f)

            val drawPointX = mutableListOf<Float>()

            (0..< counterLabelsX).forEach { index ->

                val accessIndex = getIndex(index, textMeasureResultLabelsAxisX.size, lineGraphConfig.orderBy) + getShift(counterLabelsX, textMeasureResultLabelsAxisX.size, lineGraphConfig.orderBy)

                val measureResult = textMeasureResultLabelsAxisX[accessIndex]

                if (isDebug)
                    drawRect(
                        color = if (index.mod(2) == 0) Color.DarkGray else Color.Yellow,
                        style = Stroke(4f),
                        topLeft = Offset(
                            x = widthLabelsAxisY + (index * containerWidthLabelX),
                            y = viewPortHeightLabelsAxisY,
                        ),
                        size = Size(
                            width = containerWidthLabelX,
                            height = heightLabelsAxisX
                        )
                    )

                val x = widthLabelsAxisY + spacePx2 + (index * containerWidthLabelX) + ((widthLabelsAxisX - measureResult.size.width) / 2f) + measureResult.size.width / 2f

                drawPointX.add(x)

                if (isDebug)
                    drawLine(
                        color = lineGraphConfig.lineColor,
                        start = Offset(
                            y = 0f,
                            x = x
                        ),
                        end = Offset(
                            y = viewPortHeightLabelsAxisY,
                            x = x
                        )
                    )

                drawText(
                    textLayoutResult = measureResult,
                    topLeft = Offset(
                        x = widthLabelsAxisY + spacePx2 + (index * containerWidthLabelX) + (widthLabelsAxisX - measureResult.size.width) / 2f,
                        y = viewPortHeightLabelsAxisY,
                    )
                )
            }

            val drawPoints = mutableListOf<Offset>()

            (0..< counterLabelsX).forEach { index ->

                val accessIndex = getIndex(index, textMeasureResultLabelsAxisX.size, lineGraphConfig.orderBy) + getShift(counterLabelsX, textMeasureResultLabelsAxisX.size, lineGraphConfig.orderBy)

                // [minValueLabelAxisY : maxYValue] -> [0 : 1]
                val ratio = 1 - ((data[accessIndex] - minValueLabelAxisY) / (maxValue - minValueLabelAxisY))

                if (isDebug) println("DEBUG -> value: ${data[accessIndex]} - ratio: $ratio")

                drawPoints.add(
                    Offset(
                        y = spacePx + (heightLabelsAxisY / 2f) + (ratio * (viewPortHeightLabelsAxisY - spacePx * 2f - heightLabelsAxisY)),
                        x = drawPointX[index]
                    )
                )

                drawCircle(
                    color = lineGraphConfig.pointColor,
                    radius = 8f,
                    center = Offset(
                        y = spacePx + (heightLabelsAxisY / 2f) + (ratio * (viewPortHeightLabelsAxisY - spacePx * 2f - heightLabelsAxisY)),
                        x = drawPointX[index]
                    )
                )
            }

            Path().apply {

                if(drawPointX.isNotEmpty()) {

                    moveTo(drawPoints.first().x, drawPoints.first().y)

                    for(index in 1 until drawPointX.size) {

                        val controlPointX = (drawPoints[index].x + drawPoints[index - 1].x) / 2f

                        val controlPoint1 = Offset(
                            y = drawPoints[index - 1].y,
                            x = controlPointX,
                        )

                        val controlPoint2 = Offset(
                            y = drawPoints[index].y,
                            x = controlPointX,
                        )

                        cubicTo(
                            x1 = controlPoint1.x,
                            y1 = controlPoint1.y,
                            x2 = controlPoint2.x,
                            y2 = controlPoint2.y,
                            x3 = drawPoints[index].x,
                            y3 = drawPoints[index].y
                        )
                    }

                    lineTo(
                        y = viewPortHeightLabelsAxisY - spacePx,
                        x = drawPoints.last().x
                    )

                    lineTo(
                        y = viewPortHeightLabelsAxisY - spacePx,
                        x = drawPoints.first().x
                    )

                    close()
                }
            }.also {
                drawPath(
                    brush = Brush.verticalGradient(
                        listOf(
                            lineGraphConfig.pathColor.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    path = it
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LineGraphPreview() {
    TrackMyRunTheme {
        LineGraphExample()
    }
}

@Composable
fun LineGraphExample(
    modifier: Modifier = Modifier,
    isDebug: Boolean = true
) {

    val data = remember {
        // listOf(0.0068334336f, 0.0f, 0.032974254f, 2.6313756f, 1.2419505f, 0.16757672f, 1.2419505f)
        // listOf(34f, 24f, 77f, 125f, 48f, 4f, 22f, 84f, 26f, 62f)
        listOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f)
        // listOf(0.16714206f, 15.898576f, 6.098382f)
        // listOf(1f, 2f, 3f, 4f)
    }

    LineGraph(
        labelsAxisX = data.map { "%.2f".format(it) },
        isDebug = isDebug,
        data = data,
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
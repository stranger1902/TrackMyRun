package com.example.trackmyrun.run_statistics.presentation.component

import com.example.trackmyrun.core.theme.TrackMyRunTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.geometry.Offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.drawText
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    labelsAxisX: List<String>,
    isDebug: Boolean = false,
    data: List<Float>
) {

    if (labelsAxisX.size != data.size)
        throw RuntimeException("axisX must contain the same number of data items list")

    if (data.isEmpty())
        Box(
            modifier = modifier
        ).also { return }

    val MAX_VISIBLE_DATA_SIZE = 10

    val MIN_HORIZONTAL_PADDING_LABELS_X = 16.dp
    val MIN_VERTICAL_PADDING_LABELS_Y = 24.dp
    val CONTENT_PADDING = 16.dp

    val PATH_COLOR = MaterialTheme.colorScheme.onSurfaceVariant
    val LINE_COLOR = MaterialTheme.colorScheme.onSurface

    val textMeauser = rememberTextMeasurer()

    val labelsAxisXTextStyle = TextStyle(
        background = if (isDebug) Color.Gray else Color.Unspecified,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 12.sp
    )

    val labelsAxisYTextStyle = TextStyle(
        background = if (isDebug) Color.Gray else Color.Unspecified,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 12.sp
    )

    val visibleData by remember {
        mutableStateOf(
            data.take(MAX_VISIBLE_DATA_SIZE)
        )
    }

    val maxValue = remember {
        visibleData.max()
    }

    val minValue = remember {
        visibleData.min()
    }

    Canvas(
        modifier = modifier
    ) {

        inset(
            inset = CONTENT_PADDING.toPx()
        ) {

            if (isDebug)
                drawRect(
                    topLeft = Offset(0f, 0f),
                    style = Stroke(8f),
                    color = Color.Green,
                    size = Size(
                        height = size.height,
                        width = size.width
                    )
                )

            val textMeasureResultLabelsAxisX = labelsAxisX.map {
                textMeauser.measure(
                    style = labelsAxisXTextStyle,
                    text = it
                )
            }

            val maxValueTextMeasureResultLabelsAxisY = textMeauser.measure(
                style = labelsAxisYTextStyle,
                text = maxValue.toString()
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

            var spacePx = MIN_VERTICAL_PADDING_LABELS_Y.toPx() / 2f

            var containerHeightLabelY = heightLabelsAxisY + (spacePx * 2f)

            val counterLabelsY = ((viewPortHeightLabelsAxisY - spacePx) / containerHeightLabelY).roundToInt()

            val spaceFilledByLabelsY = containerHeightLabelY * counterLabelsY.toFloat()
            val spaceRemained = viewPortHeightLabelsAxisY - spaceFilledByLabelsY

            val step = ((maxValue - minValue) / (counterLabelsY - 1)).roundToInt()

            if (isDebug)
                println("viewPortHeightLabelsAxisY: $viewPortHeightLabelsAxisY - spaceFilledByLabelsY: $spaceFilledByLabelsY - spaceRemained: ($spaceRemained) - containerHeightLabelY: $containerHeightLabelY - counter: $counterLabelsY - maxValue: ${visibleData.max()} - minValue: ${visibleData.min()} - step: $step")

            spacePx += spaceRemained / (counterLabelsY * 2f)
            containerHeightLabelY = heightLabelsAxisY + (spacePx * 2f)

            (0..< counterLabelsY).forEach { index ->

                val measureResult = textMeauser.measure(
                    text = (maxValue - (step * index)).toString(),
                    style = labelsAxisYTextStyle
                )

                if (isDebug)
                    drawLine(
                        color = LINE_COLOR,
                        start = Offset(
                            y = spacePx + (index * containerHeightLabelY) + (measureResult.size.height / 2f),
                            x = widthLabelsAxisY
                        ),
                        end = Offset(
                            y = spacePx + (index * containerHeightLabelY) + (heightLabelsAxisY / 2f),
                            x = viewPortWidthLabelsAxisX + widthLabelsAxisY
                        )
                    )

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

            var spacePx2 = MIN_HORIZONTAL_PADDING_LABELS_X.toPx() / 2f

            var containerWidthLabelX = widthLabelsAxisX + (spacePx2 * 2f)

            val counterLabelsX = ((viewPortWidthLabelsAxisX - spacePx2) / containerWidthLabelX).roundToInt().coerceAtMost(visibleData.size)

            val spaceFilledByLabelsX = (containerWidthLabelX * counterLabelsX)
            val spaceRemained2 = viewPortWidthLabelsAxisX - spaceFilledByLabelsX

            if (isDebug)
                println("viewPortWidthLabelsAxisX: $viewPortWidthLabelsAxisX - spaceFilledByLabelsX: $spaceFilledByLabelsX - spaceRemained2: $spaceRemained2 - containerWidthLabelX: $containerWidthLabelX - counter: $counterLabelsX")

            spacePx2 += spaceRemained2 / (counterLabelsX * 2f)
            containerWidthLabelX = widthLabelsAxisX + (spacePx2 * 2f)

            val drawPointX = mutableListOf<Float>()

            (0..< counterLabelsX).forEach { index ->

                val measureResult = textMeasureResultLabelsAxisX[index]

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
                        color = LINE_COLOR,
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

                // [minYValue ; maxYValue] -> [0 ; 1]
                val ratio = 1 - ((visibleData[index] - minValue) / (maxValue - minValue))

                if (isDebug) println("value: ${visibleData[index]} - ratio: $ratio")

                drawPoints.add(
                    Offset(
                        y = spacePx + (heightLabelsAxisY / 2f) + (ratio * (viewPortHeightLabelsAxisY - spacePx * 2f - heightLabelsAxisY)),
                        x = drawPointX[index]
                    )
                )

                drawCircle(
                    color = Color.Red,
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
                }
            }.also {
                drawPath(
                    color = PATH_COLOR,
                    path = it,
                    style = Stroke(
                        cap = StrokeCap.Round,
                        width = 5f
                    )
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
        listOf(34f, 24f, 77f, 125f, 48f, 4f, 22f, 84f, 26f, 62f)
    }

    LineGraph(
        labelsAxisX = data.map { it.toString() },
        isDebug = isDebug,
        data = data,
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
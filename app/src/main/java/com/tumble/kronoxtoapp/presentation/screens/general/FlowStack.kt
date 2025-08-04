package com.tumble.kronoxtoapp.presentation.screens.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class FlowStackOptions(
    var horizontalPadding: Dp = 5.dp,
    var verticalPadding: Dp = 5.dp,
    var minHeight: Dp = 200.dp,
    var minWidth: Dp = 200.dp
)

@Composable
fun <T: Any> FlowStack(
    items: List<T>,
    options: FlowStackOptions = FlowStackOptions(),
    viewGenerator: @Composable (T) -> Unit
){
    BoxWithConstraints{
        Layout(
            content = {
                items.forEach{ item ->
                    Box(modifier = Modifier
                        .background(Color.Unspecified, RoundedCornerShape(20.dp))
                        .padding(
                            horizontal = options.horizontalPadding,
                            vertical = options.verticalPadding
                        )
                        .layoutId(item)

                    ){
                        viewGenerator(item)
                    }
                }
            }
        ){ measurables, constraints ->
            var width = 0
            var height = 0
            var rowWidth = 0
            var rowHeight = 0

            val placeables = measurables.map { measurable ->
                val placeable = measurable.measure(constraints)
                if (rowWidth + placeable.width > constraints.maxWidth){
                    width = maxOf(width, rowWidth)
                    height += rowHeight
                    rowWidth = 0
                    rowHeight = 0
                }
                rowWidth += placeable.width
                rowHeight = maxOf(rowHeight, placeable.height)
                placeable
            }
            width = maxOf(width, rowWidth)
            height += rowHeight

            layout(width,height){
                var xPos = 0
                var yPos = 0
                placeables.forEach{ placeable ->
                    if (xPos + placeable.width > width){
                        xPos = 0
                        yPos += rowHeight
                    }
                    placeable.placeRelative(xPos, yPos)
                    xPos += placeable.width
                }
            }
        }
    }
}
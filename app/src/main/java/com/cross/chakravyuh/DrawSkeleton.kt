package com.cross.chakravyuh

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun drawTracks(index: Int, screenCenterX: Int, screenCenterY: Int, level: Level) {
    val trackDiameterInPx = with(LocalDensity.current) { level.trackDiameter[index].dp.toPx() }
    Surface(
        modifier = Modifier
            .size(level.trackDiameter[index].dp)
            .graphicsLayer {
                translationX = screenCenterX.toFloat() - (trackDiameterInPx / 2)
                translationY = screenCenterY.toFloat() - (trackDiameterInPx / 2)
            },
        color = Color.Transparent,
        shape = CircleShape,
        border = BorderStroke(width = level.trackWidthInDp, color = Color.Gray)
    ) {}
}

@Composable
fun drawRevolvingBalls(
    index: Int,
    angle: Animatable<Float, AnimationVector1D>,
    screenCenterX: Int,
    screenCenterY: Int,
    ballSizeInPx: Float,
    level: Level
) {
    val radius = with(LocalDensity.current) { level.revolvingBallsRadiusArray[index].dp.toPx() }
    Box(modifier = Modifier
        .size(level.ballSizeInDp)
        .graphicsLayer {
            translationX = screenCenterX + getXCoOrdFromAngle(
                angle.value,
                radius
            ).toFloat() - (ballSizeInPx / 2)
            translationY = screenCenterY + getYCoOrdFromAngle(
                angle.value,
                radius
            ).toFloat() - (ballSizeInPx / 2)
            generateIntersectTimestampList(
                index,
                angle.value,
                level.ballAnimationDuration[index]
            )
        }
        .background(
            color = level.colorArray[index],
            shape = CircleShape
        )
    )
}

@Composable
fun drawSourceRing(
    x: Int,
    y: Int,
    ringSizeInPx: Float,
    srcDestRingSize: Dp,
    srcDestRingStroke: Dp
) {
    Surface(
        modifier = Modifier
            .size(srcDestRingSize)
            .graphicsLayer {
                translationX = x.toFloat() - (ringSizeInPx / 2)
                translationY = y.toFloat() - (ringSizeInPx / 2)
            },
        color = Color.Transparent,
        shape = CircleShape,
        border = BorderStroke(width = srcDestRingStroke, color = Color.Green)
    ) {}
}

@Composable
fun drawDestinationRing(
    x: Int,
    y: Int,
    ringSizeInPx: Float,
    srcDestRingSize: Dp,
    srcDestRingStroke: Dp
) {
    Surface(
        modifier = Modifier
            .size(srcDestRingSize)
            .graphicsLayer {
                translationX = x.toFloat() - (ringSizeInPx / 2)
                translationY = y.toFloat() - (ringSizeInPx / 2)
            },
        color = Color.Transparent,
        shape = CircleShape,
        border = BorderStroke(width = srcDestRingStroke, color = Color.Red)
    ) {}
}

@Composable
fun drawRoller(
    rollerSourceX: Float,
    rollerSourceY: Float,
    destX: Float,
    destY: Float,
    rollerStarted: Boolean,
    rollerStopped: MutableState<Boolean>,
    gameState: MutableState<State>,
    angles: List<Animatable<Float, AnimationVector1D>>
) {

    var previousX = 0f
    var previousY = 0f

    val xValue = animateFloatAsState(
        targetValue = if (rollerStarted) destX else rollerSourceX,
        animationSpec = tween(rollerAnimTime, 0, LinearEasing)
    )

    val yValue = animateFloatAsState(
        targetValue = if (rollerStarted) destY else rollerSourceY,
        animationSpec = tween(rollerAnimTime, 0, LinearEasing)
    )

    Box(
        modifier = Modifier
            .size(rollerSize)
            .graphicsLayer {
                if (!rollerStopped.value) {
                    previousX = xValue.value
                    previousY = yValue.value
                }
                translationX = previousX
                translationY = previousY
                if (previousX == destX && previousY == destY) {
                    // You Win, as roller has reached destination without collision
                    stopAllAnimations(angles, rollerStopped, gameState, State.Win)
                }
            }
            .background(
                color = Color.White,
                shape = CircleShape
            )
    )
}
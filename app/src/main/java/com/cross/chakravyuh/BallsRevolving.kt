package com.cross.chakravyuh

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import com.cross.chakravyuh.ui.theme.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "Balls Revolving"
val srcDestRingStroke = 5.dp
val srcDestRingSize = 35.dp
val trackWidth = 10.dp
val ballSize = 20.dp
val rollerSize = 25.dp
const val rollerAnimTime = 2000

/** xIntersectList stores all x coordinates of intersection between roller and every track */
lateinit var xIntersectList: ArrayList<Float>

/** yIntersectList stores all y coordinates of intersection between roller and every track */
lateinit var yIntersectList: ArrayList<Float>

/** timeRequiredList stores time in millisecond, which is the time required by roller to touch every track */
lateinit var timeRequiredList: ArrayList<Long>

/** intersectTimestampList stores time in timestamp, which is the exact time in future when roller will touch every track */
lateinit var intersectTimestampList: ArrayList<Long>

fun initiateData(trackCount: Int) {
    // a condition to protect from reinitialization of variable
    if (!::xIntersectList.isInitialized) {
        xIntersectList = ArrayList(trackCount)
        yIntersectList = ArrayList(trackCount)
        timeRequiredList = ArrayList(trackCount)
        intersectTimestampList = ArrayList(trackCount)
    }
}

fun clearData() {
    xIntersectList.clear()
    yIntersectList.clear()
    timeRequiredList.clear()
    intersectTimestampList.clear()
}

@Composable
fun BallsRevolving(
    level: Level,
    gameState: MutableState<State>
) {
    Log.d(TAG, "Level : ${level.level}")
    // initiate all data
    initiateData(level.trackCount)

    // mobile device density, used to convert dp to pixel
    val density = LocalDensity.current

    val configuration = LocalConfiguration.current;
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.roundToPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.roundToPx() }
    // center co-ordinates of screen
    val screenCenterX = screenWidthPx / 2
    val screenCenterY = screenHeightPx / 2

    // Todo set sourceX, sourceY from level json
    val sourceX = screenCenterX
    val sourceY = 110
    // Todo set destX, destY from level json
    val destX = screenCenterX
    val destY = screenCenterY

    val ringSizeInPx = with(density) { srcDestRingSize.toPx() }
    val ballSizeInPx = with(density) { ballSize.toPx() }
    // Calculating roller source and destination
    val rollerSourceX = (sourceX - (ringSizeInPx / 2)) + with(density) { srcDestRingStroke.toPx() }
    val rollerSourceY = (sourceY - (ringSizeInPx / 2)) + with(density) { srcDestRingStroke.toPx() }
    val rollerDestX =
        (destX - (ringSizeInPx / 2)) + with(density) { srcDestRingStroke.toPx() }
    val rollerDestY =
        (destY - (ringSizeInPx / 2)) + with(density) { srcDestRingStroke.toPx() }

    var rollerStarted by remember { mutableStateOf(false) }
    var rollerStopped = remember { mutableStateOf(false) }

    // Get balls animation values
    val angles = getAnimationValue(level)
    angles.forEachIndexed { index, angle ->
        LaunchedEffect(key1 = angle) {
            angle.animateTo(
                targetValue = level.ballTargetAngle[index],
                animationSpec = infiniteRepeatable(
                    animation = tween(level.ballAnimationDuration[index], 0, LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    // draw game
    Box(modifier = Modifier.fillMaxSize())
    {
        angles.forEachIndexed { index, angle ->
            // Tracks
            drawTracks(index = index, screenCenterX, screenCenterY)
            // Revolving balls
            drawRevolvingBalls(index, angle, screenCenterX, screenCenterY, ballSizeInPx, level)
        }

        // Draw destination ring to desired co-ordinates
        drawSourceRing(sourceX, sourceY, ringSizeInPx, srcDestRingSize, srcDestRingStroke)

        // Draw destination ring to desired co-ordinates
        drawDestinationRing(destX, destY, ringSizeInPx, srcDestRingSize, srcDestRingStroke)

        // Draw roller
        drawRoller(
            rollerSourceX,
            rollerSourceY,
            rollerDestX,
            rollerDestY,
            rollerStarted,
            rollerStopped,
            gameState,
            angles
        )

        var buttonEnabled by remember { mutableStateOf(false) }
        Button(modifier = Modifier
            .align(Alignment.BottomCenter),
            enabled = buttonEnabled,
            onClick = {
                // To calculate if roller touches the revolving balls, if true, stop roller and balls
                fireTheRoller(level.trackCount, angles, rollerStopped, gameState)
                // To start the roller from source to dest
                rollerStarted = true
            })
        {
            Text(text = "Start")
        }

        // Enable start button after one revolution of ball (ball which has longest revolution time)
        Handler(Looper.getMainLooper()).postDelayed({
            buttonEnabled = true
        }, (level.ballAnimationDuration.maxOrNull() ?: 0).toLong())
    }

    // Get co-ordinates, where roller path and tracks intersect
    // todo : angle should not be hard coded, need to calculate angle and save it in a list
    calculateIntersectionPointsOfRollerAndTracks(
        level.trackCount,
        180f,
        revolvingBallsRadiusArray,
        screenCenterX,
        screenCenterY,
        ballSizeInPx
    )

    // calculate velocity of roller
    val rollerVelocity =
        getVelocity(rollerSourceX, rollerSourceY, rollerDestX, rollerDestY, rollerAnimTime)

    // calculate time taken by roller to touch each track
    calculateTimeRequiredByRollerToTouchEachTrack(
        level.trackCount,
        rollerSourceX,
        rollerSourceY,
        rollerVelocity
    )
}

fun fireTheRoller(
    trackCount: Int,
    angles: List<Animatable<Float, AnimationVector1D>>,
    rollerStopped: MutableState<Boolean>,
    gameState: MutableState<State>
) {
    // When Roller is triggered calculate the live timestamp when it will hit resp tracks
    // Then finally verify if ball is in or around(~120 ms) that timestamp
    val currentTime = System.currentTimeMillis()
    var count = trackCount - 1
    while (count != -1) {
        val timeRequiredToTouchTrack = currentTime + timeRequiredList[count]
        val intersectTime = intersectTimestampList[count]
//        Log.d(TAG, "timeRequiredToTouchTrackt : $timeRequiredToTouchTrack")
//        Log.d(TAG, "intersectTime : $intersectTime")
        if (timeRequiredToTouchTrack in intersectTime - 120..intersectTime + 120) {
            Log.d(TAG, "Boom Boom : count : $count")
            Handler(Looper.getMainLooper()).postDelayed({
                // Stop all balls and roller as there is a collision
                stopAllAnimations(angles, rollerStopped, gameState, State.Loss)
            }, timeRequiredList[count])
            break
        }
        count--
    }

}

/** Stop all balls and roller as there is a collision*/
fun stopAllAnimations(
    angles: List<Animatable<Float, AnimationVector1D>>,
    rollerStopped: MutableState<Boolean>,
    gameState: MutableState<State>,
    state: State
) {
    GlobalScope.launch() {
        for (angle in angles)
            angle.stop()
        rollerStopped.value = true
        gameState.value = state
    }
}

/**
 * It generates list of timestamps, when resp ball will reach desired angle in next cycle
 * */
fun generateIntersectTimestampList(index: Int, angle: Float, animationDuration: Int) {
    // Identify time, when ball will reach intersect points of track
    // If intersect points are updated then maintain a list of timestamp
    // (where timestamp is the time, when ball will reach the intersect point in next cycle)
    if (xIntersectList.isNotEmpty()) {
        // todo : angle 180f should not be hard coded, need to fetch angle from the calculated angle list
        if (angle in 180f - 1f..180f + 1f)
            if (intersectTimestampList.size > index)
                intersectTimestampList[index] = System.currentTimeMillis() + animationDuration
            else
                intersectTimestampList.add(System.currentTimeMillis() + animationDuration)
    }
}

/**
 * It calculates the time required by roller to touch each track
 * */
fun calculateTimeRequiredByRollerToTouchEachTrack(
    trackCount: Int,
    rollerSourceX: Float,
    rollerSourceY: Float,
    rollerVelocity: Double
) {
    var count = 0
    while (count != trackCount) {
        // gets the distance between roller start point and point of intersection with track
        val distance = getDistance(
            rollerSourceX, xIntersectList[count],
            rollerSourceY, yIntersectList[count]
        )
        val time = distance / rollerVelocity
        timeRequiredList.add(time.toLong())
        count++
    }
}

/**
 * It calculates the intersection points of roller and tracks
 * */
@Composable
fun calculateIntersectionPointsOfRollerAndTracks(
    trackCount: Int,
    angle: Float,
    revolvingBallsRadiusArray: List<Int>,
    screenCenterX: Int,
    screenCenterY: Int,
    ballSizeInPx: Float
) {
    var count = 0
    while (count != trackCount) {
        val radius = with(LocalDensity.current) { revolvingBallsRadiusArray[count].dp.toPx() }
        xIntersectList.add(
            screenCenterX + getXCoOrdFromAngle(
                angle,
                radius
            ).toFloat() - (ballSizeInPx / 2)
        )
        yIntersectList.add(
            screenCenterY + getYCoOrdFromAngle(
                angle,
                radius
            ).toFloat() - (ballSizeInPx / 2)
        )
        count++
    }
}

@Composable
fun getAnimationValue(level: Level): List<Animatable<Float, AnimationVector1D>> {
    var count = 0
    val list = ArrayList<Animatable<Float, AnimationVector1D>>()
    while (count != level.trackCount) {
        list.add(remember { Animatable(initialValue = level.ballInitialAngle[count]) })
        count++
    }
    return list
}
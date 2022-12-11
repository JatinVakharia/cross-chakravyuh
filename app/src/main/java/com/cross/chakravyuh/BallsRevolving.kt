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

const val TAG = "Balls Revolving"
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

@Composable
fun BallsRevolving(
    ringsCount: Int,
    level: Int
) {
    xIntersectList = ArrayList(ringsCount)
    yIntersectList = ArrayList(ringsCount)
    timeRequiredList = ArrayList(ringsCount)
    intersectTimestampList = ArrayList(ringsCount)

    // mobile device density, used to convert dp to pixel
    val density = LocalDensity.current

    val configuration = LocalConfiguration.current;
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.roundToPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.roundToPx() }
    // center co-ordinates of screen
    val screenCenterX = screenWidthPx / 2
    val screenCenterY = screenHeightPx / 2

    // Todo set sourceX, sourceY from function arg
    val sourceX = screenCenterX
    val sourceY = 110
    // Todo set destX, destY from function arg
    val destX = screenCenterX
    val destY = screenCenterY

    val ringSizeInPx = with(density) { srcDestRingSize.toPx() }
    val ballSizeInPx = with(density) { ballSize.toPx() }
    val rollerSourceX = (sourceX - (ringSizeInPx / 2)) + with(density) { srcDestRingStroke.toPx() }
    val rollerSourceY = (sourceY - (ringSizeInPx / 2)) + with(density) { srcDestRingStroke.toPx() }
    val rollerDestX =
        (destX - (ringSizeInPx / 2)) + with(density) { srcDestRingStroke.toPx() }
    val rollerDestY =
        (destY - (ringSizeInPx / 2)) + with(density) { srcDestRingStroke.toPx() }
    var rollerStarted by remember { mutableStateOf(false) }
    var rollerStopped = remember { mutableStateOf(false) }

    // Get balls animation values
    val angles = getAnimationValue(ringsCount)
    angles.forEachIndexed { index, angle ->
        LaunchedEffect(key1 = angle) {
            angle.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(animationDuration[index], 0, LinearEasing),
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
            drawRevolvingBalls(index, angle, screenCenterX, screenCenterY, ballSizeInPx)
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
            rollerStopped
        )

        Button(modifier = Modifier
            .align(Alignment.BottomCenter),
            onClick = {
//                Log.d(TAG, "Clicked : ")
                fireTheRoller(ringsCount, angles, rollerStopped)
                rollerStarted = !rollerStarted
            })
        {
            Text(text = "Start")
        }

        /*Button(modifier = Modifier
            .align(Alignment.BottomEnd),
            onClick = {
                Log.d(TAG, "CurrentTime : " + System.currentTimeMillis())
                var count = 0
                while (count != ringsCount) {
                    Log.d(TAG, "count : " + count)
                    if (xIntersectList?.isNotEmpty())
                        Log.d(TAG, "xPoint : " + xIntersectList[count])
                    if (yIntersectList?.isNotEmpty())
                        Log.d(TAG, "yPoint : " + yIntersectList[count])
                    if (timeRequiredList?.isNotEmpty())
                        Log.d(TAG, "time required : " + timeRequiredList[count])
                    if (intersectTimestampList?.isNotEmpty())
                        Log.d(TAG, "intersect timestamp : " + intersectTimestampList[count])
                    count++
                }
            })
        {
            Text(text = "Print Lists")
        }*/
    }

    // Get co-ordinates, where roller path and tracks intersect
    // todo : angle should not be hard coded, need to calculate angle and save it in a list
    calculateIntersectionPointsOfRollerAndTracks(
        ringsCount,
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
        ringsCount,
        rollerSourceX,
        rollerSourceY,
        rollerVelocity
    )
}

fun fireTheRoller(
    ringsCount: Int,
    angles: List<Animatable<Float, AnimationVector1D>>,
    rollerStopped: MutableState<Boolean>
) {
    // When Roller is triggered calculate the live timestamp when it will hit resp tracks
    // Then finally verify if ball is in or around(~120 ms) that timestamp
    val currentTime = System.currentTimeMillis()
    var count = ringsCount - 1
    while (count != -1) {
        val timeRequiredToTouchTrack = currentTime + timeRequiredList[count]
        val intersectTime = intersectTimestampList[count]
//        Log.d(TAG, "timeRequiredToTouchTrackt : $timeRequiredToTouchTrack")
//        Log.d(TAG, "intersectTime : $intersectTime")
        if (timeRequiredToTouchTrack in intersectTime - 120..intersectTime + 120) {
            Log.d(TAG, "Boom Boom : count : $count")
            Handler(Looper.getMainLooper()).postDelayed({
                stopAllAnimation(angles, rollerStopped)
            }, timeRequiredList[count])
            break
        }
        count--
    }

}
/** Stop all balls and roller as there is a collision*/
private fun stopAllAnimation(
    angles: List<Animatable<Float, AnimationVector1D>>,
    rollerStopped: MutableState<Boolean>
) {
    GlobalScope.launch() {
        for (angle in angles)
            angle.stop()
        rollerStopped.value = true
    }
}

fun generateIntersectTimestampList(index: Int, angle: Float, animationDuration: Int) {
    // Identify time, when ball will reach intersect points of track
    // If intersect points are updated then maintain a list of timestamp
    // (where timestamp is the time, when ball will reach the intersect point in next cycle)
    if (xIntersectList.isNotEmpty()) {
        // todo : angle 180f should not be hard coded, need to fetch angle from the calculated angle list
        if (angle in 180f - 2f..180f + 2f)
            if (intersectTimestampList.size > index)
                intersectTimestampList[index] = System.currentTimeMillis() + animationDuration
            else
                intersectTimestampList.add(System.currentTimeMillis() + animationDuration)
    }
}

fun calculateTimeRequiredByRollerToTouchEachTrack(
    ringsCount: Int,
    rollerSourceX: Float,
    rollerSourceY: Float,
    rollerVelocity: Double
) {
    var count = 0
    while (count != ringsCount) {
        val distance = getDistance(
            rollerSourceX, xIntersectList[count],
            rollerSourceY, yIntersectList[count]
        )
        val time = distance / rollerVelocity
        timeRequiredList.add(time.toLong())
        count++
    }
}

@Composable
fun calculateIntersectionPointsOfRollerAndTracks(
    ringsCount: Int,
    angle: Float,
    revolvingBallsRadiusArray: List<Int>,
    screenCenterX: Int,
    screenCenterY: Int,
    ballSizeInPx: Float
) {
    var count = 0
    while (count != ringsCount) {
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
fun getAnimationValue(ringsCount: Int): List<Animatable<Float, AnimationVector1D>> {
    var count = 0
    val list = ArrayList<Animatable<Float, AnimationVector1D>>()
    while (count != ringsCount) {
        list.add(remember { Animatable(initialValue = 0f) })
        count++
    }
    return list
}
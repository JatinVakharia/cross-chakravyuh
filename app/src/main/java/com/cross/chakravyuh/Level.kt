package com.cross.chakravyuh

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cross.chakravyuh.ui.theme.*

data class Level(
    val level: Int,
    val trackCount: Int,
    val ballInitialAngle: List<Float>,
    val ballTargetAngle: List<Float>,
    val ballAnimationDuration: List<Int>,
    val sourceRingX: Int,
    val sourceRingY: Int,
    val destRingX: Int,
    val destRingY: Int,
    val trackWidthInDp: Dp,
    val ballSizeInDp: Dp,
    val revolvingBallsRadiusArray: List<Int>,
    val trackDiameter: List<Int>,
    val colorArray: List<Color>
)

fun getLevelObjects(): List<Level> {
    val levelList = ArrayList<Level>()

    levelList.add(
        Level(
            1, 2,
            listOf(0f, 0f), listOf(360f, 360f),
            listOf(2400, 3000),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            2, 2,
            listOf(0f, 360f), listOf(360f, 0f),
            listOf(2400, 3000),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            3, 2,
            listOf(0f, 360f), listOf(360f, 0f),
            listOf(1800, 2400),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            4, 2,
            listOf(0f, 360f), listOf(360f, 0f),
            listOf(1800, 2100),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            5, 3,
            listOf(0f, 0f, 0f), listOf(360f, 360f, 360f),
            listOf(2400, 3000, 3600),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            6, 3,
            listOf(0f, 360f, 0f), listOf(360f, 0f, 360f),
            listOf(2400, 3000, 3600),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            7, 3,
            listOf(0f, 360f, 0f), listOf(360f, 0f, 360f),
            listOf(1800, 2400, 3000),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            8, 3,
            listOf(0f, 360f, 0f), listOf(360f, 0f, 360f),
            listOf(1800, 2100, 2400),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            9, 4,
            listOf(0f, 0f, 0f, 0f), listOf(360f, 360f, 360f, 360f),
            listOf(2400, 3000, 3600, 4200),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            10, 4,
            listOf(0f, 360f, 0f, 360f), listOf(360f, 0f, 360f, 0f),
            listOf(2400, 3000, 3600, 4200),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            11, 4,
            listOf(0f, 360f, 0f, 360f), listOf(360f, 0f, 360f, 0f),
            listOf(1800, 2400, 3000, 3600),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            12, 4,
            listOf(0f, 360f, 0f, 360f), listOf(360f, 0f, 360f, 0f),
            listOf(1800, 2100, 2400, 2700),
            0, 0, 0, 0,
            10.dp, 20.dp,
            revolvingBallsRadiusArrayFor4Tracks, trackDiameterFor4Tracks,
            colorArrayFor4Tracks
        )
    )

    levelList.add(
        Level(
            13, 5,
            listOf(0f, 0f, 0f, 0f, 0f), listOf(360f, 360f, 360f, 360f, 360f),
            listOf(2100, 2400, 2700, 3000, 3300),
            0, 0, 0, 0,
            3.dp, 12.dp,
            revolvingBallsRadiusArrayFor8Tracks, trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            14, 5,
            listOf(0f, 360f, 0f, 360f, 0f), listOf(360f, 0f, 360f, 0f, 360f),
            listOf(2100, 2400, 2700, 3000, 3300),
            0, 0, 0, 0,
            3.dp, 12.dp,
            revolvingBallsRadiusArrayFor8Tracks, trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            15, 5,
            listOf(0f, 360f, 0f, 360f, 0f), listOf(360f, 0f, 360f, 0f, 360f),
            listOf(1800, 2100, 2400, 2700, 3000),
            0, 0, 0, 0,
            3.dp, 12.dp,
            revolvingBallsRadiusArrayFor8Tracks, trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            16, 6,
            listOf(0f, 0f, 0f, 0f, 0f, 0f), listOf(360f, 360f, 360f, 360f, 360f, 360f),
            listOf(2100, 2400, 2700, 3000, 3300, 3600),
            0, 0, 0, 0,
            3.dp, 12.dp,
            revolvingBallsRadiusArrayFor8Tracks, trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            17, 6,
            listOf(0f, 360f, 0f, 360f, 0f, 360f), listOf(360f, 0f, 360f, 0f, 360f, 0f),
            listOf(2100, 2400, 2700, 3000, 3300, 3600),
            0, 0, 0, 0,
            3.dp, 12.dp,
            revolvingBallsRadiusArrayFor8Tracks, trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            18, 6,
            listOf(0f, 360f, 0f, 360f, 0f, 360f), listOf(360f, 0f, 360f, 0f, 360f, 0f),
            listOf(1800, 2100, 2400, 2700, 3000, 3300),
            0, 0, 0, 0,
            3.dp, 12.dp,
            revolvingBallsRadiusArrayFor8Tracks, trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            19, 7,
            listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f), listOf(360f, 360f, 360f, 360f, 360f, 360f, 360f),
            listOf(2100, 2400, 2700, 3000, 3300, 3600, 3900),
            0, 0, 0, 0,
            3.dp, 12.dp,
            revolvingBallsRadiusArrayFor8Tracks, trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            20, 7,
            listOf(0f, 360f, 0f, 360f, 0f, 360f, 0f), listOf(360f, 0f, 360f, 0f, 360f, 0f, 360f),
            listOf(2100, 2400, 2700, 3000, 3300, 3600, 3900),
            0, 0, 0, 0,
            3.dp, 12.dp,
            revolvingBallsRadiusArrayFor8Tracks, trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            21, 7,
            listOf(0f, 360f, 0f, 360f, 0f, 360f, 0f), listOf(360f, 0f, 360f, 0f, 360f, 0f, 360f),
            listOf(1800, 2100, 2400, 2700, 3000, 3300, 3600),
            0, 0, 0, 0,
            3.dp, 12.dp,
            revolvingBallsRadiusArrayFor8Tracks, trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            22,
            8,
            listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f),
            listOf(360f, 360f, 360f, 360f, 360f, 360f, 360f, 360f),
            listOf(2100, 2400, 2700, 3000, 3300, 3600, 3900, 4200),
            0,
            0,
            0,
            0,
            3.dp,
            12.dp,
            revolvingBallsRadiusArrayFor8Tracks,
            trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            23,
            8,
            listOf(0f, 360f, 0f, 360f, 0f, 360f, 0f, 360f),
            listOf(360f, 0f, 360f, 0f, 360f, 0f, 360f, 0f),
            listOf(2100, 2400, 2700, 3000, 3300, 3600, 3900, 4200),
            0,
            0,
            0,
            0,
            3.dp,
            12.dp,
            revolvingBallsRadiusArrayFor8Tracks,
            trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    levelList.add(
        Level(
            24,
            8,
            listOf(0f, 360f, 0f, 360f, 0f, 360f, 0f, 360f),
            listOf(360f, 0f, 360f, 0f, 360f, 0f, 360f, 0f),
            listOf(1800, 2100, 2400, 2700, 3000, 3200, 3400, 3600),
            0,
            0,
            0,
            0,
            3.dp,
            12.dp,
            revolvingBallsRadiusArrayFor8Tracks,
            trackDiameterFor8Tracks,
            colorArrayFor8Tracks
        )
    )

    return levelList
}

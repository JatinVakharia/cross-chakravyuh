package com.cross.chakravyuh

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
)

fun getLevelObjects(): List<Level> {
    val levelList = ArrayList<Level>()

    levelList.add(
        Level(
            1, 2,
            listOf(0f, 0f), listOf(360f, 360f),
            listOf(2400, 3000),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            2, 3,
            listOf(0f, 0f, 0f), listOf(360f, 360f, 360f),
            listOf(2400, 3000, 3600),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            3, 4,
            listOf(0f, 0f, 0f, 0f), listOf(360f, 360f, 360f, 360f),
            listOf(2400, 3000, 3600, 4200),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            4, 2,
            listOf(0f, 360f), listOf(360f, 0f),
            listOf(2400, 3000),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            5, 3,
            listOf(0f, 360f, 0f), listOf(360f, 0f, 360f),
            listOf(2400, 3000, 3600),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            6, 4,
            listOf(0f, 360f, 0f, 360f), listOf(360f, 0f, 360f, 0f),
            listOf(2400, 3000, 3600, 4200),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            7, 2,
            listOf(0f, 360f), listOf(360f, 0f),
            listOf(1800, 2400),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            8, 3,
            listOf(0f, 360f, 0f), listOf(360f, 0f, 360f),
            listOf(1800, 2400, 3000),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            9, 4,
            listOf(0f, 360f, 0f, 360f), listOf(360f, 0f, 360f, 0f),
            listOf(1800, 2400, 3000, 3600),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            10, 2,
            listOf(0f, 360f), listOf(360f, 0f),
            listOf(1800, 2100),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            11, 3,
            listOf(0f, 360f, 0f), listOf(360f, 0f, 360f),
            listOf(1800, 2100, 2400),
            0, 0, 0, 0
        )
    )

    levelList.add(
        Level(
            12, 4,
            listOf(0f, 360f, 0f, 360f), listOf(360f, 0f, 360f, 0f),
            listOf(1800, 2100, 2400, 2700),
            0, 0, 0, 0
        )
    )

    return levelList
}

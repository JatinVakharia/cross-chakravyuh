package com.cross.chakravyuh

data class Level(
    val level: Int,
    val trackCount:Int,
    val ballInitialAngle: List<Float>,
    val ballTargetAngle: List<Float>,
    val ballAnimationDuration: List<Int>,
    val sourceRingX: Int,
    val sourceRingY: Int,
    val destRingX: Int,
    val destRingY: Int,
    )

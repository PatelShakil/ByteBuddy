package com.shakilpatel.notesapp.common

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

val MainColor = getRandomDarkColor()
val LightColor = getRandomLightColor()
val HorizontalBrush = Brush.horizontalGradient(
    colors = listOf(
        getRandomLightColor(),
        getRandomLightColor(),
        getRandomLightColor(),
        getRandomLightColor()
    )
)
val VerticalBrush = Brush.verticalGradient(
    colors = listOf(
        getRandomLightColor(),
        getRandomLightColor(),
        getRandomLightColor()
    )
)
val SecondaryColor = Color(0xFF5B74FF)
val TernaryColor = Color(0xFF5B74FF)
val WhiteColor = Color(0xFFFFFFFF)
val NotificationUnReadColor = Color(0xFFDDDCDC)
val BlackColor = Color(0xFF000000)
val AppBarTextColor = Color(0xFFFFFFFF)
val BorderColor = Color(0xFF664FA1)
val TextColor = Color(0xffffffff)

fun getRandomLightColor(): Color =
    Color(Random.nextInt(200, 256), Random.nextInt(200, 256), Random.nextInt(200, 256))

fun getRandomDarkColor(): Color = Color(
    Random.nextInt(0, 101),
    Random.nextInt(0, 101),
    Random.nextInt(0, 101)
)

fun getVerticalGradient(): Brush = Brush.verticalGradient(
//    colors = listOf(getRandomLightColor(), getRandomLightColor())
    colors = listOf(MainColor, MainColor)
)

fun getHorizontalGradient(): Brush = Brush.horizontalGradient(
//    colors = listOf(getRandomLightColor(), getRandomLightColor())
    colors = listOf(MainColor, MainColor)
)
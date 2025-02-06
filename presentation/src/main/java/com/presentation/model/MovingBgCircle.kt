package com.presentation.model

import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

data class MovingBgCircle(
    val x: Float,
    val y: Float,
    val angle: Float, // Угол движения в градусах
    val speed: Float, // Скорость в пикселях в секунду
    val size: Int,
    val starsCounter: Int = 0,
    val color: Color
) {
    fun updatePosition(
        screenWidth: Float,
        screenHeight: Float,
        deltaTime: Float,
        bounced: Boolean = true
    ): MovingBgCircle? {
        // Конвертируем угол в радианы
        val angleInRadians = Math.toRadians(angle.toDouble())

        // Вычисляем смещение по x и y
        val dx = (speed * deltaTime * cos(angleInRadians)).toFloat()
        val dy = (speed * deltaTime * sin(angleInRadians)).toFloat()

        // Обновляем координаты
        var newX = x + dx
        var newY = y + dy

        var newAngle = angle

        // Обработка столкновений с границами экрана (отражение)
        if (newX <= 0 || newX >= screenWidth) {
            if (bounced) {
                newX = newX.coerceIn(0f, screenWidth)
                newAngle = 180 - newAngle
            } else return null
        }
        if (newY <= 0 || newY >= screenHeight) {
            if (bounced) {
                newY = newY.coerceIn(0f, screenHeight)
                newAngle = -newAngle
            } else return null
        }

        // Возвращаем новый объект с обновленными значениями
        return copy(x = newX, y = newY, angle = (newAngle + 360) % 360)
    }
}
package com.presentation.animation.stars

data class Star(
    val x: Float,
    val y: Float,
    val angle: Float, // Угол движения в градусах
    val speed: Float, // Скорость в пикселях в секунду
    val size: Int,
    val radius: Float, // Внешний радиус,
    val starsCounter: Int = 0,
    val alpha: Float = 1f
) {
    fun updatePosition(
        screenWidth: Float,
        screenHeight: Float,
        deltaTime: Float,
        bounced: Boolean = true
    ): Star? {
        // Конвертируем угол в радианы
        val angleInRadians = Math.toRadians(angle.toDouble())

        // Вычисляем смещение по x и y
        val dx = (speed * deltaTime * Math.cos(angleInRadians)).toFloat()
        val dy = (speed * deltaTime * Math.sin(angleInRadians)).toFloat()

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

fun Star.isWithinRadius(secondStar: Star): Boolean {
    val dx = this.x - secondStar.x
    val dy = this.y - secondStar.y
    val distanceSquared = dx * dx + dy * dy
    return distanceSquared <= this.radius * this.radius
}
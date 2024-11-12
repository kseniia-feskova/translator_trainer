package com.presentation.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun Context.getLocalizedString(resourceId: Int, locale: Locale): String {
    // Создаем новую конфигурацию с заданной локалью
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    // Получаем строку из ресурсов с новой локалью
    val localizedContext = createConfigurationContext(configuration)
    return localizedContext.resources.getString(resourceId)
}
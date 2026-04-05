package com.translator.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createImageUri(context: Context): Uri {
    val imageDir = File(context.cacheDir, "images").apply { mkdirs() }
    val imageFile = File(imageDir, "photo_${System.currentTimeMillis()}.jpg")

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
}

//works bad on the POCO devices (the image is rotated)
fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    val resolver = context.contentResolver

    val bitmap = resolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it)
    } ?: return null

    val exifOrientation = resolver.openInputStream(uri)?.use {
        ExifInterface(it)
            .getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
    } ?: ExifInterface.ORIENTATION_UNDEFINED

    // Если EXIF нормальный — НИЧЕГО НЕ ДЕЛАЕМ
    if (exifOrientation == ExifInterface.ORIENTATION_NORMAL ||
        exifOrientation == ExifInterface.ORIENTATION_UNDEFINED
    ) {
        return bitmap
    }

    val rotation = when (exifOrientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }

    val isAlreadyPortrait = bitmap.height >= bitmap.width
    val needsRotation = rotation == 90 || rotation == 270

    if (needsRotation && isAlreadyPortrait) {
        return bitmap
    }

    val matrix = Matrix().apply {
        postRotate(rotation.toFloat())
    }

    return Bitmap.createBitmap(
        bitmap, 0, 0,
        bitmap.width, bitmap.height,
        matrix, true
    )
}
package com.presentation.ui.screens.texts

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.R
import com.presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.screens.auth.CustomShadowButton
import com.presentation.ui.screens.home.LanguageSwitch
import com.presentation.ui.views.Loader
import com.presentation.ui.views.SelectableChipSet
import com.presentation.utils.Language


fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun TextRecognitionScreen(
    state: TextFromPhotoUI = TextFromPhotoUI(),
    onTranslate: (String) -> Unit = {},
    onSelect: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onLanguageChange: (Language) -> Unit = {}
) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val textRecognizer = TextRecognizer()

    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Функция для создания URI
    fun createImageUri(): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "captured_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

// Лаунчер для фото с камеры
    val takePhotoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let { uri ->
                    imageBitmap = context.contentResolver.openInputStream(imageUri!!)?.use {
                        BitmapFactory.decodeStream(it)
                    }
                    val bitmap = uriToBitmap(context, uri)
                    bitmap?.let {
                        textRecognizer.recognizeText(it,
                            onSuccess = { text -> onTranslate(text) },
                            onError = { Log.e("Translate", "Ошибка распознавания") }
                        )
                    }
                }
            }
        }

// Лаунчер для выбора фото из галереи
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val bitmap = uriToBitmap(context, it)
                bitmap?.let {
                    imageBitmap = it
                    textRecognizer.recognizeText(it,
                        onSuccess = { text -> onTranslate(text) },
                        onError = { Log.e("Translate", "Ошибка распознавания") }
                    )
                }
            }
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Делаем Column скроллable ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LanguageSwitch(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                state.originalLanguage,
                state.resLanguage
            ) {
                onLanguageChange(it)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = "Выбранное изображение",
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            imageUri = createImageUri()  // Генерируем новый `Uri`
                            Log.e("Text", "imageUri = ${imageUri}")
                            imageUri?.let { takePhotoLauncher.launch(it) }
                        }
                        .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Плейсхолдер",
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            imageUri = createImageUri()  // Генерируем новый `Uri`
                            Log.e("Text", "imageUri = ${imageUri}")
                            imageUri?.let { takePhotoLauncher.launch(it) }
                        }
                        .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                )
            }


            Spacer(modifier = Modifier.height(16.dp))
            CustomShadowButton(
                onClick = { pickImageLauncher.launch("image/*") },
                text = "\uD83D\uDDBC Выбрать из галереи"
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (state.translate.isNotEmpty()) {
                Text(
                    state.translate, color = bgColor,
                    style = AppTypography.titleSmall.copy(fontSize = TextUnit(16f, TextUnitType.Sp))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (state.allWords.isNotEmpty()) {
                SelectableChipSet(
                    allWords = state.allWords,
                    selectedWords = state.selectedWords,
                    onSelect = onSelect
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (state.selectedWords.isNotEmpty()) {
                CustomShadowButton("Save words", onClick = onSaveClick)
            }
        }
        if (state.loading) {
            Loader(
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.Center)
            )
        }
    }
}


@Preview
@Composable
fun TextRecognitionScreenPreview() {
    TextRecognitionScreen()
}
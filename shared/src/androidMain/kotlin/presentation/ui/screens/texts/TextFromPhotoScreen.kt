package presentation.ui.screens.texts

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.translatortrainer.shared.R
import com.presentation.ui.darkColor
import com.presentation.ui.gradientBrush
import com.presentation.ui.views.Loader
import com.presentation.ui.yellowColor
import presentation.model.WordSelection
import presentation.model.toSelection
import presentation.test.smallList
import presentation.ui.AppTypography
import presentation.ui.screens.home.LanguageSwitch
import presentation.ui.views.buttons.CustomShadowButton
import presentation.utils.Language
import presentation.utils.TextToken
import presentation.utils.createImageUri
import presentation.utils.tokenize
import presentation.utils.uriToBitmap


@Composable
fun TextRecognitionScreen(
    state: TextFromPhotoUI = TextFromPhotoUI(),
    onSelect: (String) -> Unit = {},
    onSaveClick: (WordSelection) -> Unit = {},
    onLanguageChange: (Language) -> Unit = {},
    recognizeText: (Bitmap) -> Unit = {},
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val takePhotoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let { uri ->
                    imageBitmap = context.contentResolver.openInputStream(imageUri!!)?.use {
                        BitmapFactory.decodeStream(it)
                    }
                    uriToBitmap(context, uri)?.let {
                        recognizeText(it)
                    }
                }
            }
        }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uriToBitmap(context, uri)?.let {
                    imageBitmap = it
                    recognizeText(it)
                }
            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                if (state.originalLanguage != null && state.resLanguage != null) {
                    LanguageSwitch(
                        modifier = Modifier.align(Alignment.Center),
                        state.originalLanguage,
                        state.resLanguage
                    ) {
                        onLanguageChange(it)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap!!.asImageBitmap(),
                        contentDescription = "Выбранное изображение",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                imageUri = createImageUri(context)
                                imageUri?.let { takePhotoLauncher.launch(it) }
                            }
                            .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                imageUri = createImageUri(context)
                                imageUri?.let { takePhotoLauncher.launch(it) }
                            }
                            .border(4.dp, Color.Gray, RoundedCornerShape(24.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_photo),
                            contentDescription = "Плейсхолдер",
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.Center)
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))
                CustomShadowButton(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    onClick = { pickImageLauncher.launch("image/*") },
                    text = "\uD83D\uDDBC Выбрать из галереи"
                )
                Spacer(modifier = Modifier.height(16.dp))


                if (state.translate.isNotEmpty()) {
                    TranslatableText(
                        text = state.translate,
                        onSelect = onSelect,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            if (state.selectedWords.isNotEmpty()) {
                item {
                    Text(
                        "Selected words:",
                        modifier = Modifier.fillMaxWidth(),
                        style = AppTypography.displayMedium,
                        color = darkColor
                    )
                }
                itemsIndexed(state.selectedWords) { _, item ->
                    SelectedWord(item) {
                        onSaveClick(it)
                    }
                }
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

@Composable
fun SelectedWord(
    wordSelection: WordSelection,
    onSaveClick: (WordSelection) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            wordSelection.originalText, color = darkColor,
            style = AppTypography.titleSmall.copy(
                fontSize = 16.sp,
                color = darkColor
            )
        )
        Text(
            " - ", color = darkColor,
            style = AppTypography.titleSmall.copy(
                fontSize = 16.sp,
                color = darkColor
            )
        )
        Text(
            wordSelection.resText, color = darkColor,
            style = AppTypography.titleSmall.copy(
                fontSize = 16.sp,
                color = darkColor
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        if (wordSelection.isSaved) {
            Icon(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                imageVector = Icons.Default.CheckCircle,
                tint = darkColor,
                contentDescription = "Saved"
            )
        } else {
            Icon(
                Icons.Default.Add,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable { onSaveClick(wordSelection) }
                    .background(color = yellowColor, shape = CircleShape)
                    .border(1.dp, shape = CircleShape, color = darkColor),
                tint = darkColor,
                contentDescription = "Saved"
            )
        }
    }
}

@Composable
fun TranslatableText(
    text: String,
    onSelect: (String) -> Unit,
) {
    val tokens = remember(text) { text.tokenize() }

    Text(
        "Translation:",
        modifier = Modifier.fillMaxWidth(),
        style = AppTypography.displayMedium,
        color = darkColor
    )
    Spacer(modifier = Modifier.height(4.dp))
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        tokens.forEach { token ->
            when (token) {
                is TextToken.Word -> {
                    Text(
                        text = token.value,
                        style = AppTypography.titleSmall.copy(
                            fontSize = 16.sp,
                            color = darkColor
                        ),
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onSelect(token.value)
                            }
                    )
                }

                is TextToken.Separator -> {
                    Text(
                        text = token.value,
                        style = AppTypography.titleSmall.copy(
                            fontSize = 16.sp,
                            color = darkColor
                        )
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun TextRecognitionScreenPreview() {
    TextRecognitionScreen(
        TextFromPhotoUI(
            originalLanguage = Language.RUSSIAN,
            resLanguage = Language.GERMAN
        )
    )
}

@Preview
@Composable
fun TextRecognitionWithPhotoScreenPreview() {
    TextRecognitionScreen(
        TextFromPhotoUI(
            resLanguage = Language.RUSSIAN,
            originalLanguage = Language.GERMAN,
            translate = "Translated text to save by words",
            selectedWords = smallList.take(1).map { it.toSelection() }
        )
    )
}

@Preview
@Composable
fun SelectedWordPreview() {
    Column {
        SelectedWord(
            WordSelection(
                id = "1",
                "Name",
                "Имя", false
            )
        )

        SelectedWord(
            WordSelection(
                id = "2",
                "Mann",
                "Мужчина", true
            )
        )
    }
}
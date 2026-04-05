package presentation

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognizer {

    private val recognizer =
        TextRecognition.getClient(TextRecognizerOptions.Builder().build())

    fun recognizeText(image: Bitmap, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        val inputImage = InputImage.fromBitmap(image, 0)

        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                onSuccess(visionText.text)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }
}

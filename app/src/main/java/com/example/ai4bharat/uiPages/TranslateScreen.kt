package com.example.ai4bharat.uiPages

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.mlkit.nl.translate.*
import java.util.*
import com.google.mlkit.nl.translate.TranslateLanguage as MLTranslateLanguage

// 🔹 Custom enum for this screen (separate from your existing Language enum)
enum class TranslateLanguage(
    val displayName: String,
    val translateCode: String, // ML Kit string code
    val speechCode: String      // TextToSpeech locale code
) {
    ENGLISH("English", MLTranslateLanguage.ENGLISH, "en"),
    HINDI("Hindi", MLTranslateLanguage.HINDI, "hi"),
    MALAYALAM("Malayalam", "ml", "ml"), // use "ml" directly
    TAMIL("Tamil", MLTranslateLanguage.TAMIL, "ta")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateScreen() {
    val context = LocalContext.current
    val activity = context as Activity

    var sourceLanguage by remember { mutableStateOf(TranslateLanguage.ENGLISH) }
    var targetLanguage by remember { mutableStateOf(TranslateLanguage.HINDI) }

    var spokenText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }

    var sourceExpanded by remember { mutableStateOf(false) }
    var targetExpanded by remember { mutableStateOf(false) }

    // Dynamically exclude source from target options
    val targetOptions = TranslateLanguage.values().filter { it != sourceLanguage }

    // TTS instance
    var ttsReady by remember { mutableStateOf(false) }
    val tts = remember {
        TextToSpeech(context) { status ->
            ttsReady = status == TextToSpeech.SUCCESS
        }
    }

    // 🎤 Speech launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val resultText =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText = resultText ?: ""

            // Translate spoken text
            translateText(spokenText, sourceLanguage, targetLanguage) { translated ->
                translatedText = translated

                // Speak the translated text
                if (ttsReady) {
                    tts.language = Locale(targetLanguage.speechCode)
                    tts.speak(translated, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
    }

    // 🎤 Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { granted ->
        if (!granted) {
            Toast.makeText(context, "Mic permission required", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Live Voice Translation", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Source Dropdown
        ExposedDropdownMenuBox(
            expanded = sourceExpanded,
            onExpandedChange = { sourceExpanded = !sourceExpanded }
        ) {
            TextField(
                value = sourceLanguage.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Source Language") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = sourceExpanded,
                onDismissRequest = { sourceExpanded = false }
            ) {
                TranslateLanguage.values().forEach { lang ->
                    DropdownMenuItem(
                        text = { Text(lang.displayName) },
                        onClick = {
                            sourceLanguage = lang
                            sourceExpanded = false
                            // Ensure target is not same as source
                            if (targetLanguage == sourceLanguage) {
                                targetLanguage = targetOptions.first()
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Target Dropdown
        ExposedDropdownMenuBox(
            expanded = targetExpanded,
            onExpandedChange = { targetExpanded = !targetExpanded }
        ) {
            TextField(
                value = targetLanguage.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Target Language") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = targetExpanded,
                onDismissRequest = { targetExpanded = false }
            ) {
                targetOptions.forEach { lang ->
                    DropdownMenuItem(
                        text = { Text(lang.displayName) },
                        onClick = {
                            targetLanguage = lang
                            targetExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Start Speaking Button
        Button(
            onClick = {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    sourceLanguage.speechCode
                )
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                speechLauncher.launch(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🎤 Start Speaking")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("You Said:")
        Text(spokenText)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Translated:")
        Text(translatedText)
    }
}

// 🔹 Translation function
fun translateText(
    text: String,
    source: TranslateLanguage,
    target: TranslateLanguage,
    onResult: (String) -> Unit
) {
    val options = TranslatorOptions.Builder()
        .setSourceLanguage(source.translateCode)
        .setTargetLanguage(target.translateCode)
        .build()

    val translator = Translation.getClient(options)

    translator.downloadModelIfNeeded()
        .addOnSuccessListener {
            translator.translate(text)
                .addOnSuccessListener { translated -> onResult(translated) }
                .addOnFailureListener { onResult("Translation failed: ${it.message}") }
        }
        .addOnFailureListener {
            onResult("Model download failed: ${it.message}")
        }
}
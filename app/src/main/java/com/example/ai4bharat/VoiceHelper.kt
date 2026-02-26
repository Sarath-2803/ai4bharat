package com.example.ai4bharat

import android.content.Context
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class VoiceHelper(
    private val context: Context,
    private val onResult: (String) -> Unit
) {

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    fun startListening() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "en-IN"
        )

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: android.os.Bundle?) {
                val matches =
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                matches?.firstOrNull()?.let {
                    onResult(it)
                }
            }

            override fun onReadyForSpeech(p0: android.os.Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(p0: Float) {}
            override fun onBufferReceived(p0: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(p0: Int) {}
            override fun onPartialResults(p0: android.os.Bundle?) {}
            override fun onEvent(p0: Int, p1: android.os.Bundle?) {}
        })

        speechRecognizer.startListening(intent)
    }

    fun destroy() {
        speechRecognizer.destroy()
    }
}
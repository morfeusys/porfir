package com.porfir.tts

import android.content.Context
import com.justai.aimybox.model.AudioSpeech
import com.justai.aimybox.model.TextSpeech
import com.justai.aimybox.texttospeech.BaseTextToSpeech
import java.net.URLEncoder

class AimylogicTextToSpeech(
    context: Context,
    private val speaker: String? = DEFAULT_SPEAKER
): BaseTextToSpeech(context) {

    companion object {
        const val DEFAULT_SPEAKER = "levitan"
    }

    override suspend fun speak(speech: TextSpeech) {
        audioSynthesizer.play(AudioSpeech.Uri(
            "https://station.aimylogic.com/generate?speaker=${speaker}&text=${speech.text.urlEncode()}"
        ))
    }
}

private fun String.urlEncode() = URLEncoder.encode(this, "UTF-8")
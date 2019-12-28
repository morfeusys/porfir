package com.porfir

import android.app.Application
import android.content.Context
import com.justai.aimybox.Aimybox
import com.justai.aimybox.components.AimyboxProvider
import com.justai.aimybox.core.Config
import com.justai.aimybox.speechkit.google.platform.GooglePlatformSpeechToText
import com.justai.aimybox.speechkit.google.platform.GooglePlatformTextToSpeech
import com.porfir.api.PorfirDialogApi
import java.util.*

class PorfirApplication: Application(), AimyboxProvider {
    override val aimybox by lazy { createAimybox(this) }

    private fun createAimybox(context: Context): Aimybox {
        val textToSpeech = GooglePlatformTextToSpeech(context, Locale("ru"))
        val speechToText = GooglePlatformSpeechToText(context, Locale("ru"))

        val dialogApi = PorfirDialogApi()

        return Aimybox(Config.create(speechToText, textToSpeech, dialogApi))
    }
}
package com.porfir

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.DialogApi
import com.justai.aimybox.components.AimyboxProvider
import com.justai.aimybox.core.Config
import com.justai.aimybox.model.reply.TextReply
import com.justai.aimybox.speechkit.google.platform.GooglePlatformSpeechToText
import com.justai.aimybox.speechkit.google.platform.GooglePlatformTextToSpeech
import com.porfir.api.PorfirDialogApi
import com.porfir.dao.PorfirDatabase
import com.porfir.model.HistoryItem
import com.porfir.tts.AimylogicTextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.*

class PorfirApplication: Application(), AimyboxProvider, CoroutineScope {

    val database by lazy { createDatabase(this) }

    override val aimybox by lazy { createAimybox(this) }

    override val coroutineContext = Dispatchers.IO

    private fun createAimybox(context: Context): Aimybox {
        val textToSpeech = AimylogicTextToSpeech(context)
        val speechToText = GooglePlatformSpeechToText(context, Locale("ru"))
        val dialogApi = PorfirDialogApi(context)

        return Aimybox(Config.create(speechToText, textToSpeech, dialogApi))
            .also(::subscribeToDialogApi)
    }

    private fun createDatabase(context: Context): PorfirDatabase {
        return Room.databaseBuilder(
            context,
            PorfirDatabase::class.java,
            "porfir.db").build()
    }

    private fun subscribeToDialogApi(aimybox: Aimybox) {
        val channel = aimybox.dialogApiEvents.openSubscription()
        launch {
            channel.consumeEach { event ->
                event
                    .takeIf { it is DialogApi.Event.ResponseReceived }
                    ?.also { it ->
                        val now = Calendar.getInstance().timeInMillis
                        val response = (it as DialogApi.Event.ResponseReceived).response
                        response.replies
                            .filterIsInstance(TextReply::class.java)
                            .map { reply -> HistoryItem(0, reply.text, now) }
                            .forEach { item -> database.historyDao().insert(item) }
                    }
            }
        }
    }
}
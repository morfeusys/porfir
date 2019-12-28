package com.porfir.api

import com.google.gson.JsonObject
import com.justai.aimybox.model.JsonResponse
import com.justai.aimybox.model.reply.ButtonsReply
import com.justai.aimybox.model.reply.Reply
import com.justai.aimybox.model.reply.ReplyButton
import com.justai.aimybox.model.reply.TextReply
import java.net.URLEncoder

class PorfirResponse(
    override val query: String,
    json: JsonObject
): JsonResponse(json, replies = json["replies"].asJsonArray.mapTo(ArrayList<Reply>()) {
    TextReply(
        query + " " + it.asString,
        "<audio src=\"https://station.aimylogic.com/tts?voice=levitan&#038;text=${URLEncoder.encode(query + " " + it.asString, "UTF-8")}\"/>",
        null)
}.also { it.add(ButtonsReply(listOf(ReplyButton(text ="Еще", payload = MORE_PAYLOAD)))) }) {

    companion object {
        const val MORE_PAYLOAD = "more"
    }
}
package com.porfir.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.justai.aimybox.api.DialogApi
import com.justai.aimybox.core.CustomSkill
import com.justai.aimybox.model.reply.ButtonsReply
import com.justai.aimybox.model.reply.Reply
import com.justai.aimybox.model.reply.ReplyButton
import com.justai.aimybox.model.reply.TextReply
import com.porfir.R
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class PorfirDialogApi(
    private val context: Context,
    private val url: String = DEFAULT_API_URL,
    override val customSkills: LinkedHashSet<CustomSkill<PorfirRequest, PorfirResponse>> = linkedSetOf()
) : DialogApi<PorfirRequest, PorfirResponse>() {

    companion object {
        private const val DEFAULT_API_URL = "https://models.dobro.ai/gpt2/medium/"
        private const val NEXT_PAYLOAD = "next"
    }

    private val httpClient = OkHttpClient.Builder().build()
    private val gson = Gson()

    private lateinit var currentQuery: String

    override fun createRequest(query: String): PorfirRequest {
        return when (query) {
            NEXT_PAYLOAD -> PorfirRequest(currentQuery)
            else -> PorfirRequest(query).also { currentQuery = query }
        }
    }

    override suspend fun send(request: PorfirRequest): PorfirResponse {
        val req = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .post(gson.toJson(request).toRequestBody())
            .build()

        val res = httpClient.newCall(req).execute()
        val json = JsonParser().parse(res.body?.string()).asJsonObject

        return PorfirResponse(
            json = json,
            replies = json["replies"].asJsonArray.mapTo(ArrayList<Reply>()) {
                TextReply(request.query + " " + it.asString, null ,null)
            }.also {
                it.add(ButtonsReply(listOf(
                    ReplyButton(text = context.getString(R.string.next_button), payload = NEXT_PAYLOAD))
                ))
            })
    }
}
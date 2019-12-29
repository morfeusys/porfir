package com.porfir.api

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.justai.aimybox.api.DialogApi
import com.justai.aimybox.core.CustomSkill
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class PorfirDialogApi(
    private val url: String = DEFAULT_API_URL,
    override val customSkills: LinkedHashSet<CustomSkill<PorfirRequest, PorfirResponse>> = linkedSetOf()
) : DialogApi<PorfirRequest, PorfirResponse>() {

    companion object {
        private const val DEFAULT_API_URL = "https://models.dobro.ai/gpt2/medium/"
    }

    private val httpClient = OkHttpClient.Builder().build()
    private val gson = Gson()

    private lateinit var currentPrompt: String

    override fun createRequest(query: String): PorfirRequest {
        return when (query) {
            PorfirResponse.NEXT_PAYLOAD -> PorfirRequest(currentPrompt)
            else -> {
                currentPrompt = query
                PorfirRequest(query)
            }
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

        return PorfirResponse(request.query, json)
    }
}
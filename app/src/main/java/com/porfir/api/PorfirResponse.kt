package com.porfir.api

import com.google.gson.JsonObject
import com.justai.aimybox.model.JsonResponse
import com.justai.aimybox.model.reply.Reply

class PorfirResponse(
    json: JsonObject,
    override val replies: List<Reply>
): JsonResponse(json)
package com.porfir.api

import com.google.gson.annotations.SerializedName
import com.justai.aimybox.model.Request

data class PorfirRequest(
    @SerializedName("prompt")
    override val query: String,
    @SerializedName("num_samples")
    val samples: Int = 1,
    @SerializedName("length")
    val length: Int = 30
): Request
package com.porfir.module

import android.widget.TextView
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.porfir.R
import com.porfir.model.HistoryItem

class HistoryModule: ItemModule<HistoryItem>() {

    override fun provideModuleConfig() = object : ItemModuleConfig() {
        override fun withLayoutResource() = R.layout.history_item
    }

    override fun onBind(model: HistoryItem, viewBinder: ViewBinder) {
        val textView = viewBinder.findViewById<TextView>(R.id.history_item_text)
        textView.text = model.text
    }
}
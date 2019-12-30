package com.porfir

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.justai.aimybox.components.AimyboxAssistantFragment
import com.porfir.dao.PorfirDatabase
import com.porfir.model.HistoryItem
import com.porfir.module.HistoryModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main

    private lateinit var promptLayout: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: OneAdapter
    private lateinit var database: PorfirDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_main)

        promptLayout = findViewById(R.id.prompt_layout)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerAdapter = OneAdapter(recyclerView)
            .attachItemModule(HistoryModule().addEventHook(historyClickHook))

        database = (application as PorfirApplication).database

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.assistant_container, AimyboxAssistantFragment())
            commit()
        }

        onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        refreshRecyclerViewData()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == Intent.ACTION_ASSIST) {
            val aimybox = (application as PorfirApplication).aimybox
            val channel = aimybox.stateChannel.openSubscription()
            launch {
                channel.consume {
                    aimybox.startRecognition()
                }
            }.invokeOnCompletion { channel.cancel() }
        }
    }

    override fun onBackPressed() {
        val assistantFragment = (supportFragmentManager.findFragmentById(R.id.assistant_container)
                as? AimyboxAssistantFragment)
        if (assistantFragment?.onBackPressed() == true) {
            refreshRecyclerViewData()
        } else {
            super.onBackPressed()
        }
    }

    private fun refreshRecyclerViewData() = launch(Dispatchers.IO) {
        val items = database.historyDao().getAll()
        recyclerAdapter.setItems(items)

        promptLayout.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        recyclerView.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private val historyClickHook = object : ClickEventHook<HistoryItem>() {
        override fun onClick(model: HistoryItem, viewBinder: ViewBinder) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, model.text)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

}
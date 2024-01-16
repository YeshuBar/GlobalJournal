package de.byeshurun.globaljournal.journalEntry

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.byeshurun.globaljournal.R
import de.byeshurun.globaljournal.adapter.JournalAdapter
import de.byeshurun.globaljournal.database.DbManager

class JournalEntryActivity : AppCompatActivity() {
    private val TAG: String? = JournalEntryActivity::class.simpleName
    private val entryList: RecyclerView by lazy { this.findViewById(R.id.entryList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.journal_entry_activity_layout)
    }

    override fun onResume() {
        super.onResume()
        initEntryList()
        Log.i(TAG, "onResume()")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop()")
    }

    private fun initEntryList() {
        entryList.layoutManager = LinearLayoutManager(this)
        entryList.adapter = JournalAdapter(this, DbManager.getInstance(this).getAllJournalEntries())
    }
}

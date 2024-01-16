package de.byeshurun.globaljournal.dashboard


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import de.byeshurun.globaljournal.R
import de.byeshurun.globaljournal.journalEntry.JournalEntryActivity
import de.byeshurun.globaljournal.newEntry.NewEntryActivity


class DashboardActivity : AppCompatActivity(), OnClickListener {

    private val journalEntries by lazy { this.findViewById<Button>(R.id.journalEntries) }
    private val newEntry by lazy { this.findViewById<Button>(R.id.newEntry) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity_layout)
        this.journalEntries.setOnClickListener(this)
        this.newEntry.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)

        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.showInformation -> showInformationDialog()
        }
        return true;
    }

    private fun showInformationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.information)
            .setMessage(R.string.information_text)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.journalEntries -> showJournalEntries()
            R.id.newEntry -> showNewEntry()

        }
    }

    private fun showJournalEntries() {
        this.startActivity(Intent(this, JournalEntryActivity::class.java))
    }

    private fun showNewEntry() {
        this.startActivity(Intent(this, NewEntryActivity::class.java))
    }
}
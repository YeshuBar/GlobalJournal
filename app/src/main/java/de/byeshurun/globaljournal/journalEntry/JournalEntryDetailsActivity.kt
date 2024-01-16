package de.byeshurun.globaljournal.journalEntry

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.byeshurun.globaljournal.R
import de.byeshurun.globaljournal.database.DbManager
import de.byeshurun.globaljournal.files.CsvFileHandler
import de.byeshurun.globaljournal.model.JournalEntry

class JournalEntryDetailsActivity : AppCompatActivity() {
    companion object {
        private val TAG: String? = JournalEntryDetailsActivity::class.simpleName
        private var currentEntry: JournalEntry? = null
    }


    private val entryId: TextView by lazy { this.findViewById(R.id.entryId) }
    private val entryTitle: TextView by lazy { this.findViewById(R.id.entryTitle) }
    private val entryDate: TextView by lazy { this.findViewById(R.id.entryDate) }
    private val entryContent: TextView by lazy { this.findViewById(R.id.entryContent) }
    private val entryLatitude: TextView by lazy { this.findViewById(R.id.latitudeTextView) }
    private val entryLongitude: TextView by lazy { this.findViewById(R.id.longitudeTextView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.journal_entry_details_activity_layout)
        showEntryDetails()
    }

    private fun showEntryDetails() {
        val entryIdValue = intent.getIntExtra(getString(R.string.entry_id), -1)

        if (entryIdValue != -1) {
            currentEntry = DbManager.getInstance(this).getJournalEntryById(entryIdValue)

            currentEntry?.let {
                entryId.text = it.id.toString()
                entryTitle.text = it.title
                entryDate.text = it.date
                entryContent.text = it.content
                entryLatitude.text = it.latitude.toString()
                entryLongitude.text = it.longitude.toString()
                Log.d(TAG, "Latitude in showEntryDetails: ${it.latitude}")
                Log.d(TAG, "Longitude in showEntryDetails: ${it.longitude}")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.journal_entry_details_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(clickedItem: MenuItem): Boolean {
        when (clickedItem.itemId) {
            R.id.deleteEntry -> deleteEntry()
            R.id.shareEntry -> shareEntry()
            R.id.exportDbDataToCsvFiles -> exportDbDataToCsvFiles()
        }
        return true

    }

    private fun exportDbDataToCsvFiles() {
        val entriesFromDb = DbManager.getInstance(this).getAllJournalEntries()
        CsvFileHandler.getInstance().exportEntriesToCsvFile(this, entriesFromDb)
    }

    private fun deleteEntry() {
        currentEntry?.let {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes") { _, _ ->
                    val deletedRows = DbManager.getInstance(this).deleteJournalEntryById(it.id)

                    if (deletedRows > 0) {
                        Log.d(TAG, "Entry deleted successfully")
                        finish()
                    } else {
                        Log.d(TAG, "Failed to delete entry")
                    }
                }
                .setNegativeButton("No") { _, _ ->
                }

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun shareEntry() {
        currentEntry?.let {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, it.getEntryDataSharingText())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)

            this.startActivity(shareIntent)
        }
    }
}

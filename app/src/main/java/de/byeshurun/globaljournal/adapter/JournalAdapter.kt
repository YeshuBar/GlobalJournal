package de.byeshurun.globaljournal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.byeshurun.globaljournal.model.JournalEntry
import de.byeshurun.globaljournal.journalEntry.JournalEntryDetailsActivity

import de.byeshurun.globaljournal.R

class JournalAdapter(
    private val context: Context,
    private val entries: List<JournalEntry>
) : RecyclerView.Adapter<JournalAdapter.ViewHolder>() {

    inner class ViewHolder(entryItem: View) : RecyclerView.ViewHolder(entryItem) {
        val entryInfoEntryTitle: TextView = entryItem.findViewById(R.id.entryInfoEntryTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.journal_entry_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEntry = entries[position]

        holder.entryInfoEntryTitle.text = currentEntry.title

        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(context, JournalEntryDetailsActivity::class.java)
                    .putExtra(context.getString(R.string.entry_id), currentEntry.id)
            )
        }
    }

    override fun getItemCount(): Int {
        return entries.size
    }
}

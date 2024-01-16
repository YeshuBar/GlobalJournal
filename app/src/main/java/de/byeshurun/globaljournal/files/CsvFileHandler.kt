package de.byeshurun.globaljournal.files

import android.content.Context
import de.byeshurun.globaljournal.model.JournalEntry
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class CsvFileHandler private constructor() {
    companion object {
        private const val ENTRIES_CSV_FILE_NAME = "journal_entries.csv"

        @Volatile
        private var instance: CsvFileHandler? = null

        fun getInstance(): CsvFileHandler {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = CsvFileHandler()
                    }
                }
            }
            return instance!!
        }
    }

    fun exportEntriesToCsvFile(context: Context, entriesToExport: List<JournalEntry>) {
        try {
            val file = File(context.filesDir, ENTRIES_CSV_FILE_NAME)
            val fileWriter = FileWriter(file)

            for (entry in entriesToExport) {
                fileWriter.append(entry.getAllAttributesAsCsvLine())
            }
            fileWriter.flush()
            fileWriter.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

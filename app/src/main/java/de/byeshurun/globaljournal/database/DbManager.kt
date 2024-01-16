package de.byeshurun.globaljournal.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import de.byeshurun.globaljournal.model.JournalEntry

class DbManager private constructor(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val TAG: String = "DbManager"
        private const val DB_NAME = "globaljournal.db"
        private const val DB_VERSION = 1

        @Volatile
        private var instance: DbManager? = null

        fun getInstance(context: Context): DbManager {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = DbManager(context)
                    }
                }
            }
            return instance!!
        }
    }

    private val daoJournalEntries by lazy { DaoJournalEntries() }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(daoJournalEntries.createTable())
        Log.d(TAG, "Database table created: ${DaoJournalEntries.TABLE_NAME}")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // This method is not implemented for now
    }

    fun insertJournalEntry(entry: JournalEntry): Long {
        val db = writableDatabase
        val insertedId = daoJournalEntries.insertJournalEntry(db, entry)
        Log.d(TAG, "Inserted entry with ID: $insertedId")
        return insertedId
    }

    fun insertJournalEntries(entries: List<JournalEntry>): Long {
        val db = writableDatabase
        val insertedCount = daoJournalEntries.insertJournalEntries(db, entries)
        Log.d(TAG, "Inserted $insertedCount entries")
        return insertedCount
    }

    fun getAllJournalEntries(): List<JournalEntry> {
        val db = readableDatabase
        return daoJournalEntries.getAllJournalEntries(db)
    }

    fun getJournalEntryById(id: Int): JournalEntry? {
        val db = readableDatabase
        return daoJournalEntries.getJournalEntryById(db, id)
    }

    fun updateJournalEntry(entry: JournalEntry): Int {
        val db = writableDatabase
        val updatedRows = daoJournalEntries.updateJournalEntry(db, entry)
        Log.d(TAG, "Updated $updatedRows entries")
        return updatedRows
    }

    fun deleteJournalEntryById(id: Int): Int {
        val db = writableDatabase
        val deletedRows = daoJournalEntries.deleteJournalEntry(db, id)
        Log.d(TAG, "Deleted $deletedRows entries")
        return deletedRows
    }
}

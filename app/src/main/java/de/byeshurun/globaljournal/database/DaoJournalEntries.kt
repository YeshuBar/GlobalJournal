package de.byeshurun.globaljournal.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import de.byeshurun.globaljournal.model.JournalEntry

class DaoJournalEntries {

    companion object {
        private val TAG: String? = DaoJournalEntries::class.simpleName

        const val TABLE_NAME = "journal_entries"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
        private const val WHERE_CLAUSE = "$COLUMN_ID = ?"
    }

    fun createTable(): String {
        return "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_CONTENT TEXT, " +
                "$COLUMN_LATITUDE REAL, " +
                "$COLUMN_LONGITUDE REAL)"
    }


    fun insertJournalEntry(db: SQLiteDatabase, entryToInsert: JournalEntry): Long {
        val contentValues = getContentValuesFromEntry(entryToInsert)
        return db.use {
            it.insert(TABLE_NAME, null, contentValues)
        }
    }

    fun insertJournalEntries(db: SQLiteDatabase, entriesToInsert: List<JournalEntry>): Long {
        return db.use {
            var lastRowId = -1L
            for (entry in entriesToInsert) {
                val entryContentValues = getContentValuesFromEntry(entry)
                lastRowId = it.insert(TABLE_NAME, null, entryContentValues)
            }
            lastRowId
        }
    }

    fun getAllJournalEntries(db: SQLiteDatabase): List<JournalEntry> {
        return db.use {
            val entriesFromDb = mutableListOf<JournalEntry>()
            val query = "SELECT * FROM $TABLE_NAME"
            val cursor = it.rawQuery(query, null)
            if (cursor.moveToFirst()) {
                do {
                    val entryFromDb = getEntryFromCursor(cursor)
                    entriesFromDb.add(entryFromDb)
                } while (cursor.moveToNext())
            }
            cursor.close()
            entriesFromDb
        }
    }

    fun getJournalEntryById(db: SQLiteDatabase, entryId: Int): JournalEntry? {
        return db.use {
            val query = "SELECT * FROM $TABLE_NAME WHERE $WHERE_CLAUSE"
            val selectionArgs = arrayOf(entryId.toString())
            val cursor = it.rawQuery(query, selectionArgs)
            var entryFromDb: JournalEntry? = null
            if (cursor.moveToFirst()) {
                entryFromDb = getEntryFromCursor(cursor)
            }
            cursor.close()
            entryFromDb
        }
    }

    fun updateJournalEntry(db: SQLiteDatabase, entryToUpdate: JournalEntry): Int {
        val contentValuesFromEntry = getContentValuesFromEntry(entryToUpdate)
        val whereArgs = arrayOf(entryToUpdate.id.toString())
        return db.use {
            it.update(TABLE_NAME, contentValuesFromEntry, WHERE_CLAUSE, whereArgs)
        }
    }

    fun deleteJournalEntry(db: SQLiteDatabase, id: Int): Int {
        val whereArgs = arrayOf(id.toString())
        return db.use {
            it.delete(TABLE_NAME, WHERE_CLAUSE, whereArgs)
        }
    }

    private fun getContentValuesFromEntry(entry: JournalEntry): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, entry.title)
        contentValues.put(COLUMN_DATE, entry.date)
        contentValues.put(COLUMN_CONTENT, entry.content)
        contentValues.put(COLUMN_LATITUDE, entry.latitude)
        contentValues.put(COLUMN_LONGITUDE, entry.longitude)
        return contentValues
    }

    private fun getEntryFromCursor(cursor: Cursor): JournalEntry {
        val columnIndexId = cursor.getColumnIndex(COLUMN_ID)
        val columnIndexTitle = cursor.getColumnIndex(COLUMN_TITLE)
        val columnIndexDate = cursor.getColumnIndex(COLUMN_DATE)
        val columnIndexContent = cursor.getColumnIndex(COLUMN_CONTENT)
        val columnIndexLatitude = cursor.getColumnIndex(COLUMN_LATITUDE)
        val columnIndexLongitude = cursor.getColumnIndex(COLUMN_LONGITUDE)

        val id = cursor.getInt(columnIndexId)
        val title = cursor.getString(columnIndexTitle)
        val date = cursor.getString(columnIndexDate)
        val content = cursor.getString(columnIndexContent)
        val latitude = cursor.getDouble(columnIndexLatitude)
        val longitude = cursor.getDouble(columnIndexLongitude)

        Log.d(TAG, "Latitude: $latitude, Longitude: $longitude")

        return JournalEntry(id, title, date, content, latitude, longitude)
    }
}

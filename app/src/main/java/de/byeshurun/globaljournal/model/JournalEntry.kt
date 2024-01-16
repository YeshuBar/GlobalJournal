package de.byeshurun.globaljournal.model

data class JournalEntry(
    val id: Int,
    val title: String,
    val date: String,
    val content: String,
    val latitude: Double,
    val longitude: Double?
) {
    companion object {
        const val DEFAULT_ID = -1
        private const val DEFAULT_STRING_VALUE = ""
        private const val CSV_DELIMITER = ";"

        private const val SPLIT_INDEX_ID = 0
        private const val SPLIT_INDEX_TITLE = 1
        private const val SPLIT_INDEX_DATE = 2
        private const val SPLIT_INDEX_CONTENT = 3
        private const val SPLIT_INDEX_LATITUDE = 4
        private const val SPLIT_INDEX_LONGITUDE = 5

        private const val SHARE_TEXT_FORMAT =
            "Journal Entry\nId: %s\nTitle: %s\nDate: %s\nContent: %s\nLatitude: %s\nLongitude: %s"
    }

    constructor(
        title: String,
        date: String,
        content: String,
        latitude: Double?,
        longitude: Double?
    ) : this(DEFAULT_ID, title, date, content, latitude ?: 0.0, longitude ?: 0.0)

    constructor(csvLine: String?) : this(
        csvLine?.split(CSV_DELIMITER)?.get(SPLIT_INDEX_ID)?.toInt() ?: DEFAULT_ID,
        csvLine?.split(CSV_DELIMITER)?.get(SPLIT_INDEX_TITLE) ?: DEFAULT_STRING_VALUE,
        csvLine?.split(CSV_DELIMITER)?.get(SPLIT_INDEX_DATE) ?: DEFAULT_STRING_VALUE,
        csvLine?.split(CSV_DELIMITER)?.get(SPLIT_INDEX_CONTENT) ?: DEFAULT_STRING_VALUE,
        csvLine?.split(CSV_DELIMITER)?.get(SPLIT_INDEX_LATITUDE)?.toDoubleOrNull() ?: 0.0,
        csvLine?.split(CSV_DELIMITER)?.get(SPLIT_INDEX_LONGITUDE)?.toDoubleOrNull() ?: 0.0
    )

    fun getAllAttributesAsCsvLine(): String {
        return "$id$CSV_DELIMITER$title$CSV_DELIMITER$date$CSV_DELIMITER$content$CSV_DELIMITER$latitude$CSV_DELIMITER$longitude\n"
    }

    fun getEntryDataSharingText(): String {
        return String.format(
            SHARE_TEXT_FORMAT,
            this.id,
            this.title,
            this.date,
            this.content,
            this.latitude,
            this.longitude
        )
    }
}

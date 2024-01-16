package de.byeshurun.globaljournal.newEntry

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import de.byeshurun.globaljournal.R
import de.byeshurun.globaljournal.database.DbManager
import de.byeshurun.globaljournal.model.JournalEntry

class NewEntryActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val TAG: String? = NewEntryActivity::class.simpleName
        private const val NO_ENTRY_ID: Int = -1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
        private const val TAKE_PICTURE_REQUEST_CODE = 102
    }

    private lateinit var readCurrentLocation: ImageButton
    private lateinit var locationData: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var capturedImage: ImageView

    private var currentEntry: JournalEntry? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val entryId: TextView by lazy { this.findViewById(R.id.entryId) }
    private val entryTitle: TextView by lazy { this.findViewById(R.id.entryTitle) }
    private val entryDate: TextView by lazy { this.findViewById(R.id.entryDate) }
    private val entryContent: TextView by lazy { this.findViewById(R.id.entryContent) }
    private val submitEntry: Button by lazy { this.findViewById(R.id.submit) }
    private val capturePicture: ImageButton by lazy { this.findViewById(R.id.capturePicture) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_entry_activity_layout)
        submitEntry.setOnClickListener(this)
        readCurrentLocation = findViewById(R.id.readCurrentLocation)
        locationData = findViewById(R.id.locationData)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        readCurrentLocation.setOnClickListener(this)
        capturedImage = findViewById(R.id.capturedImage)
        capturePicture.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        showEntryById()
        Log.i(TAG, "onResume()")
    }

    private fun showEntryById() {
        val entryId = this.intent.getIntExtra(this.getString(R.string.entry_id), NO_ENTRY_ID)

        if (entryId != NO_ENTRY_ID) {
            currentEntry = DbManager.getInstance(this).getJournalEntryById(entryId)
            this.entryId.text = currentEntry?.id.toString()
            this.entryTitle.text = currentEntry?.title
            this.entryDate.text = currentEntry?.date
            this.entryContent.text = currentEntry?.content
        }
    }

    override fun onClick(v: View?) {
        val title = entryTitle.text.toString()
        val date = entryDate.text.toString()
        val content = entryContent.text.toString()
        Log.d(TAG, "Title: $title, Date: $date, Content: $content")

        when (v?.id) {
            R.id.readCurrentLocation -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    getCurrentLocation()
                    Log.d(TAG, "getCurrentLocation() called")
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }

            R.id.submit -> handleEntrySubmission(title, date, content)
            R.id.capturePicture -> capturePicture()
        }

    }

    private fun capturePicture() {
        Log.d(TAG, "capturePicture() called")

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            val takePictureIntent = Intent("android.media.action.IMAGE_CAPTURE")
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE)
            } else {
                Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")

        if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                capturedImage.setImageBitmap(it)
            }
        } else {
            Log.e(TAG, "onActivityResult: Picture capture failed")
        }
    }

    private fun entryNeedsToBeUpdated() = currentEntry != null

    private fun entryDataIsValid(title: String, date: String, content: String): Boolean {
        return (title.isNotEmpty() && date.isNotEmpty() && content.isNotEmpty())
    }

    private fun showEntrySubmitSuccessMessage() {
        showEntryInformation(R.string.submitted_entry_data_successfully)
    }

    private fun showEntrySubmitFailMessage() {
        showEntryInformation(R.string.submitting_entry_data_failed)
    }

    private fun showEntryInformation(@StringRes messageResId: Int) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        latitude = it.latitude
                        longitude = it.longitude
                        val locationText =
                            getString(R.string.location_coordinates, latitude, longitude)
                        locationData.text = locationText
                        Log.d(TAG, "Latitude in handleEntrySubmission: $latitude")
                        Log.d(TAG, "Longitude in handleEntrySubmission: $longitude")
                    } ?: run {
                        locationData.text = getString(R.string.location_not_found)
                    }
                }
                .addOnFailureListener { e ->
                    val errorMessage = getString(R.string.location_failure, e.message)
                    locationData.text = errorMessage
                }
        } catch (e: SecurityException) {
            Log.e(TAG, "Exception: ${e.message}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionsResult() called")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                locationData.text = getString(R.string.location_permission_denied)
            }
        }
    }

    private fun handleEntrySubmission(title: String, date: String, content: String) {
        Log.d(TAG, "Latitude just before insertion: $latitude")
        Log.d(TAG, "Longitude just before insertion: $longitude")

        if (entryDataIsValid(title, date, content)) {
            if (entryNeedsToBeUpdated()) {
                val entryToUpdate = JournalEntry(
                    currentEntry!!.id,
                    title,
                    date,
                    content,
                    latitude,
                    longitude
                )
                DbManager.getInstance(this).updateJournalEntry(entryToUpdate)
            } else {
                val entryToInsert = JournalEntry(
                    title,
                    date,
                    content,
                    latitude,
                    longitude
                )
                DbManager.getInstance(this).insertJournalEntry(entryToInsert)
            }
            showEntrySubmitSuccessMessage()
        } else {
            showEntrySubmitFailMessage()
        }
    }
}

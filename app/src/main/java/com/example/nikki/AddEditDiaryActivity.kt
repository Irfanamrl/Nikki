package com.example.nikki

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import com.example.nikki.R.drawable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.*
import java.util.jar.Manifest

class AddEditDiaryActivity : AppCompatActivity() {
    companion object {
        var EXTRA_ID : String = "com.example.nikki.EXTRA_ID"
        var EXTRA_TITLE : String = "com.example.nikki.EXTRA_TITLE"
        var EXTRA_DESCRIPTION : String = "com.example.nikki.EXTRA_DESCRIPTION"
        var EXTRA_LOCATION : String = "com.example.nikki.EXTRA_LOCATION"
    }

    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private var editTextLocation: TextView? = null
    private var btLocation: Button? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        editTextLocation = findViewById(R.id.edit_text_location)
        btLocation = findViewById(R.id.bt_location)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@AddEditDiaryActivity)
        btLocation?.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this@AddEditDiaryActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                ActivityCompat.requestPermissions(this@AddEditDiaryActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 44)
            }
        }

        supportActionBar?.setHomeAsUpIndicator(drawable.ic_close)

        val intent : Intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            title = Resources.getSystem().getString(R.string.title_edit_diary)
            editTextTitle?.setText(intent.getStringExtra(EXTRA_TITLE))
            editTextDescription?.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            editTextLocation?.text = intent.getStringExtra(EXTRA_LOCATION)
        } else {
            title = Resources.getSystem().getString(R.string.add_title_diary)
        }


    }

    private fun saveDiary() {
        var title : String = editTextTitle?.text.toString()
        var description : String = editTextDescription?.text.toString()
        var location : String = editTextLocation?.text.toString()

        if (title.trim().isEmpty() || description.trim().isEmpty() || location.trim().isEmpty()){
            Toast.makeText(this, "Tolong masukkan input", Toast.LENGTH_SHORT).show()
            return
        }

        var data = Intent().apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_DESCRIPTION, description)
            putExtra(EXTRA_LOCATION, location)
        }

        var id : Int = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }

        setResult(RESULT_OK, data)
        finish()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater : MenuInflater = menuInflater
        menuInflater.inflate(R.menu.add_diary_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.save_diary -> {
                saveDiary()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@AddEditDiaryActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@AddEditDiaryActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@AddEditDiaryActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
        fusedLocationProviderClient?.lastLocation?.addOnCompleteListener(object: OnCompleteListener<Location> {
            override fun onComplete(p0: Task<Location>) {
                var location : Location = p0.result
                if (location != null ) {

                    try {
                        var geocoder = Geocoder(this@AddEditDiaryActivity, Locale.getDefault())
                        var addresses : List<Address> = geocoder.getFromLocation(
                            location.latitude,location.longitude,1
                        )
                        editTextLocation?.text = addresses[0].locality
                    }
                    catch (e : IOException) {
                        e.printStackTrace()
                    }

                }
            }
        })
    }
}
package ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.R.drawable
import kotlinx.android.synthetic.main.activity_add_diary.*
import java.io.IOException
import java.util.*


class AddEditDiaryActivity : AppCompatActivity() {
    companion object {
        var EXTRA_ID : String = "ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.EXTRA_ID"
        var EXTRA_TITLE : String = "ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.EXTRA_TITLE"
        var EXTRA_DESCRIPTION : String = "ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.EXTRA_DESCRIPTION"
        var EXTRA_LOCATION : String = "ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.EXTRA_LOCATION"
        var EXTRA_URI : String = "ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.EXTRA_URI"
        var EXTRA_EMOTION : String = "ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.EXTRA_EMOTION"
        //image pick code
        val IMAGE_PICK_CODE = 1000;
        //Permission code
        val PERMISSION_CODE = 1001;
        init {
            System.loadLibrary("native-lib")
        }
    }

    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private var editTextLocation: TextView? = null
    private var btLocation: Button? = null
    private var img_pick_btn: Button? = null
    private var emotionButton: Button? = null
    private var textEmotion: TextView? = null
    private var diary_img: ImageView? = null
    private var temp_uri: Uri? = null

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        editTextLocation = findViewById(R.id.edit_text_location)
        btLocation = findViewById(R.id.bt_location)
        img_pick_btn = findViewById(R.id.img_pick_btn)
        emotionButton = findViewById(R.id.bt_emotion)
        textEmotion = findViewById(R.id.emotion_txt)
        diary_img = findViewById(R.id.image_view)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@AddEditDiaryActivity)
        btLocation?.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@AddEditDiaryActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                ActivityCompat.requestPermissions(
                    this@AddEditDiaryActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 44
                )
            }
        }

        emotionButton?.setOnClickListener{
            textEmotion?.text = randEmotion()
        }

        supportActionBar?.setHomeAsUpIndicator(drawable.ic_close)

        val intent : Intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            title = getString(R.string.title_edit_diary)
            editTextTitle?.setText(intent.getStringExtra(EXTRA_TITLE))
            editTextDescription?.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            editTextLocation?.text = intent.getStringExtra(EXTRA_LOCATION)
            textEmotion?.text = intent.getStringExtra(EXTRA_EMOTION)
            Log.w("URI GAMBAR", intent.getStringExtra(EXTRA_URI).toString())
            diary_img?.setImageURI(Uri.parse(intent.getStringExtra(EXTRA_URI)))


        } else {
            title = getString(R.string.add_title_diary)
        }

        img_pick_btn?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, PopUpWindow::class.java)
                    intent.putExtra("popuptitle", "Warning!")
                    intent.putExtra("popuptext", "Permission needed to access photos on gallery")
                    intent.putExtra("popupbtn", "Close")
                    intent.putExtra("darkstatusbar", false)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            temp_uri = data?.data
            diary_img?.setImageURI(temp_uri)
        }
    }

    private fun saveDiary() {
        var title : String = editTextTitle?.text.toString()
        var description : String = editTextDescription?.text.toString()
        var location : String = editTextLocation?.text.toString()
        var emotion : String = textEmotion?.text.toString()
        var uri : String? = temp_uri.toString()
        Log.w("URI SAVE", uri)

        if (title.trim().isEmpty() || description.trim().isEmpty() || location.trim().isEmpty()){
            Toast.makeText(this, getString(R.string.input_feedback), Toast.LENGTH_SHORT).show()
            return
        }

        var data = Intent().apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_DESCRIPTION, description)
            putExtra(EXTRA_LOCATION, location)
            putExtra(EXTRA_EMOTION, emotion)
            putExtra(EXTRA_URI, uri)
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@AddEditDiaryActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )) {
                ActivityCompat.requestPermissions(
                    this@AddEditDiaryActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@AddEditDiaryActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }
        fusedLocationProviderClient?.lastLocation?.addOnCompleteListener(object :
            OnCompleteListener<Location> {
            override fun onComplete(p0: Task<Location>) {
                var location: Location = p0.result
                if (location != null) {

                    try {
                        var geocoder = Geocoder(this@AddEditDiaryActivity, Locale.getDefault())
                        var addresses: List<Address> = geocoder.getFromLocation(
                            location.latitude, location.longitude, 1
                        )
                        editTextLocation?.text = addresses[0].locality
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        })
    }


    private external fun randEmotion() : String

    private fun getPathFromURI(context: Context, contentUri: Uri): String? {
        var mediaCursor: Cursor? = null
        return try {
            val dataPath = arrayOf(MediaStore.Images.Media.DATA)
            mediaCursor = context.contentResolver.query(contentUri, dataPath, null, null, null)
            val column_index: Int? = mediaCursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            mediaCursor?.moveToFirst()
            mediaCursor?.getString(column_index!!)
        } finally {
            if (mediaCursor != null) {
                mediaCursor.close()
            }
        }
    }
}
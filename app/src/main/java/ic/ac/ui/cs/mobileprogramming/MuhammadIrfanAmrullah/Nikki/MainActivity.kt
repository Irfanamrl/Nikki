package ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    companion object {
        const val ADD_NOTE_REQUEST : Int = 1
        const val EDIT_NOTE_REQUEST : Int = 2
    }

    private var diaryViewModel: DiaryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buttonAddDiary : FloatingActionButton = findViewById(R.id.button_add_diary)
        buttonAddDiary.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditDiaryActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }
        var recyclerView : RecyclerView = findViewById((R.id.recycler_view))
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.setHasFixedSize((true))

        val adapter = DiaryAdapter()
        recyclerView.adapter = adapter

        diaryViewModel = ViewModelProvider(this@MainActivity)[DiaryViewModel::class.java]
        diaryViewModel?.getDiaries()?.observe(this@MainActivity, object : Observer<List<Diary>>{
            override fun onChanged(t: List<Diary>?) {
                if (t != null) {
                    adapter.setDiaries(t)
                }
            }
        })

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    diaryViewModel?.deleteDiary(adapter.getDiaryAt(viewHolder.adapterPosition))
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.deleted_diary),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        adapter.setOnItemClickListener(object : DiaryAdapter.OnItemClickListener {
            override fun onItemClick(diary: Diary?) {
                val intent = Intent(this@MainActivity, AddEditDiaryActivity::class.java)
                intent.putExtra(AddEditDiaryActivity.EXTRA_ID, diary?.id)
                intent.putExtra(AddEditDiaryActivity.EXTRA_TITLE, diary?.title)
                intent.putExtra(AddEditDiaryActivity.EXTRA_DESCRIPTION, diary?.description)
                intent.putExtra(AddEditDiaryActivity.EXTRA_LOCATION, diary?.location)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })
        createNotificationChannel()
        notifyBroadcast()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            var title = data?.getStringExtra(AddEditDiaryActivity.EXTRA_TITLE)
            var description = data?.getStringExtra(AddEditDiaryActivity.EXTRA_DESCRIPTION)
            var location = data?.getStringExtra(AddEditDiaryActivity.EXTRA_LOCATION)

            val diary = Diary(title = title, description = description, location = location)
            diaryViewModel?.insertDiary(diary)

            Toast.makeText(this, getString(R.string.saved_diary), Toast.LENGTH_SHORT).show()

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            var id : Int? = data?.getIntExtra(AddEditDiaryActivity.EXTRA_ID, -1)

            if (id == -1) {
                Toast.makeText(this, getString(R.string.fail_update), Toast.LENGTH_SHORT).show()
                return
            }

            var title = data?.getStringExtra(AddEditDiaryActivity.EXTRA_TITLE)
            var description = data?.getStringExtra(AddEditDiaryActivity.EXTRA_DESCRIPTION)
            var location = data?.getStringExtra(AddEditDiaryActivity.EXTRA_LOCATION)

            val diary = Diary(title = title, description = description, location = location)
            if (id != null) {
                diary.id = id
            }
            diaryViewModel?.updateDiary(diary)
            Toast.makeText(this, getString(R.string.updated_diary), Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, getString(R.string.fail_updated_diary), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.delete_all_diary -> {
                diaryViewModel?.deleteAllDiaries()
                Toast.makeText(this, getString(R.string.all_deleted), Toast.LENGTH_SHORT).show()
                true
            } else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun notifyBroadcast() {
        var intent = Intent(this@MainActivity, ReminderBroadcast::class.java)
        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this@MainActivity,
            0,
            intent,
            0
        )
        var timeAtAppStart = System.currentTimeMillis()

        var aDayInMilis = 864*100000
        (getSystemService(Context.ALARM_SERVICE) as AlarmManager).apply {
            set(
                AlarmManager.RTC_WAKEUP,
                timeAtAppStart + aDayInMilis,
                pendingIntent
            )
        }


    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "NikkiReminderChannel"
            val descriptionText = getString(R.string.notification_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notifyNikki", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
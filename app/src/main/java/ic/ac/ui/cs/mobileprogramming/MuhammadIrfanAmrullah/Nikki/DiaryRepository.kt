package ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DiaryRepository(application:Application) {
    private var diaryDao: DiaryDao?
    private var allDiary: LiveData<List<Diary>>? = null

    init {
        val db = DiaryDatabase.getInstance(application.applicationContext)
        diaryDao = db?.diaryDao()
        allDiary = diaryDao?.getDiaries()
    }

    fun getDiaries(): LiveData<List<Diary>>? {
        return allDiary
    }

    fun insert(diary: Diary) = runBlocking {
        this.launch(Dispatchers.IO) {
            diaryDao?.insert(diary)
        }
    }

    fun delete(diary: Diary) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                diaryDao?.delete(diary)
            }
        }
    }

    fun update(diary: Diary) = runBlocking {
        this.launch(Dispatchers.IO) {
            diaryDao?.update(diary)
        }
    }

    fun deleteAllDiaries() {
        runBlocking {
            this.launch(Dispatchers.IO) {
                diaryDao?.deleteAllDiaries()
            }
        }
    }

}
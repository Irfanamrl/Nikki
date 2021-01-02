package id.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.database.entity.Diary
import id.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.repository.DiaryRepository

class DiaryViewModel(application: Application): AndroidViewModel(application) {
    private var diaryRepository = DiaryRepository(application)
    private var diaries: LiveData<List<Diary>>? = diaryRepository.getDiaries()

    fun insertDiary(diary: Diary) {
        diaryRepository.insert(diary)
    }

    fun getDiaries(): LiveData<List<Diary>>? {
        return diaries
    }

    fun deleteDiary(diary: Diary) {
        diaryRepository.delete(diary)
    }

    fun updateDiary(diary: Diary) {
        diaryRepository.update(diary)
    }

    fun deleteAllDiaries() {
        diaryRepository.deleteAllDiaries()
    }
}
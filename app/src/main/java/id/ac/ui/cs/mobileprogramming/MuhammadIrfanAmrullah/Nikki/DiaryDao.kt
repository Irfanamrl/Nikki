package id.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DiaryDao {

    @Query("DELETE FROM diary_table")
    fun deleteAllDiaries()

    @Query("SELECT * FROM diary_table ORDER BY id DESC")
    fun getDiaries(): LiveData<List<Diary>>

    @Insert
    suspend fun insert(diary: Diary)

    @Update
    suspend fun update(diary: Diary)

    @Delete
    suspend fun delete(diary: Diary)
}
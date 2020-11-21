package ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Diary::class], exportSchema = false, version = 1)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {

        private const val DB_NAME = "diary_database"
        private var instance: DiaryDatabase? = null

        fun getInstance(context: Context): DiaryDatabase? {
            if (instance == null) {
                synchronized(DiaryDatabase::class) {
                    instance = Room
                        .databaseBuilder(
                            context,
                            DiaryDatabase::class.java,
                            DB_NAME
                        )
                        .build()
                }
            }
            return instance
        }
    }
}
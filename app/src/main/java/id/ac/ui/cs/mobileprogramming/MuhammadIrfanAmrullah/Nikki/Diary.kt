package id.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "diary_table")
//@TypeConverter(Converters::class)
data class Diary(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id: Int = 0,

    @ColumnInfo(name="title")
    var title: String?,

    @ColumnInfo(name="description")
    var description: String?,

    @ColumnInfo(name="location")
    var location: String?,

    @ColumnInfo(name = "emotion")
    var emotion: String?,

    @ColumnInfo(name = "uri")
    var uri: String?,

)

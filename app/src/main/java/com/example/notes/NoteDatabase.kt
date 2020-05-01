package com.example.notes

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.math.log

@Database(entities = [Note::class], version = 3)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var instance: NoteDatabase? = null
        fun getInstance(context: Context): NoteDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        "note_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}

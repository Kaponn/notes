package com.example.notes

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var instance: NoteDatabase? = null
        fun getInstance(context: Context): NoteDatabase {
            instance?.let {
                instance = Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(RoomCallback)
                    .build()
            }
            return instance!!
        }
        object RoomCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                instance?.let { PopulateDbAsyncTask(it).execute() }
            }
        }
    }

    class PopulateDbAsyncTask(db: NoteDatabase) : AsyncTask<Void, Void, Void>() {
        private val noteDao = db.noteDao()

        override fun doInBackground(vararg params: Void?): Void? {
            noteDao.insert(Note(title = "Title 1", description = "Description 1", priority = 1))
            noteDao.insert(Note(title = "Title 2", description = "Description 2", priority = 2))
            noteDao.insert(Note(title = "Title 3", description = "Description 3", priority = 3))
            return null
        }
    }
}
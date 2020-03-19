package com.example.notes

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class NoteRepository(application: Application) {
    private val database: NoteDatabase = NoteDatabase.getInstance(application)
    private val noteDao: NoteDao = database.noteDao()

    fun insert(note: Note) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAll() {
        DeleteAllAsyncTask(noteDao).execute()
    }

    fun getAll(): LiveData<List<Note>> {
        return noteDao.getAllNotes()
    }

    private class InsertNoteAsyncTask(private val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.insert(notes[0])
            return null
        }
    }

    private class UpdateNoteAsyncTask(private val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.update(notes[0])
            return null
        }
    }

    private class DeleteNoteAsyncTask(private val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.delete(notes[0])
            return null
        }
    }

    private class DeleteAllAsyncTask(private val noteDao: NoteDao) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg voids: Void): Void? {
            noteDao.deleteAll()
            return null
        }
    }
}
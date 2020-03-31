package com.example.notes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_TASK")
class ViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val database: NoteDatabase = NoteDatabase.getInstance(context)
    private val noteDao: NoteDao = database.noteDao()

    private val noteRepository: NoteRepository = NoteRepository(noteDao)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NoteViewModel::class.java) -> NoteViewModel(noteRepository) as T
            else -> throw IllegalArgumentException("ViewModel $modelClass not found")
        }
    }
}

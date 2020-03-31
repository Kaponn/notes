package com.example.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    fun insert(note: Note) {
        repository.insert(note)
    }

    fun update(note: Note) {
        repository.update(note)
    }

    fun delete(note: Note) {
        repository.delete(note)
    }

    fun getAll(): LiveData<List<Note>> = repository.getAll()

    fun deleteAll() {
        repository.deleteAll()
    }
}

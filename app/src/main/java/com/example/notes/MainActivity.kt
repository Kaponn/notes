package com.example.notes

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.EnumSet.of
import java.util.Optional.of

class MainActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val notes = ArrayList<Note>()


        recycler_view.adapter = NoteAdapter(notes)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val adapter = NoteAdapter(notes)


//      val noteViewModel: NoteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        val noteViewModel: NoteViewModel by viewModels()
        noteViewModel.getAll().observe(this, Observer { notes ->
            adapter.setNotes(notes)
        })
    }
}

package com.example.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_note_activity.*

class AddEditNoteActivity : AppCompatActivity() {
  companion object {
    const val EXTRA_ID: String = "com.example.notes.EXTRA_ID"
    const val EXTRA_TITLE: String = "com.example.notes.EXTRA_TITLE"
    const val EXTRA_DESCRIPTION: String = "com.example.notes.EXTRA_DESCRIPTION"
    const val EXTRA_PRIORITY: String = "com.example.notes.EXTRA_PRIORITY"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.add_note_activity)

    number_picker_priority.minValue = 1
    number_picker_priority.maxValue = 10

    if (intent.hasExtra(EXTRA_ID)) {
      title = "Edit Note"
      edit_text_title.setText(intent.getStringExtra(EXTRA_TITLE))
      edit_text_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
      number_picker_priority.value = intent.getIntExtra(EXTRA_PRIORITY, 1)
    } else {
      title = "Add Note"
    }
  }

  private fun saveNote() {
    val title = edit_text_title.text.toString()
    val description = edit_text_description.text.toString()
    val priority = number_picker_priority.value

    if (title.trim().isEmpty() || description.trim().isEmpty()) {
      Snackbar.make(
        add_note_activity,
        "Please enter a title and a description",
        Snackbar.LENGTH_SHORT
      ).show();
      return
    }

    val data = Intent()
    data.putExtra(EXTRA_TITLE, title)
    data.putExtra(EXTRA_DESCRIPTION, description)
    data.putExtra(EXTRA_PRIORITY, priority)

    val id: Int = intent.getIntExtra(EXTRA_ID, -1)
    if (id != -1) {
      data.putExtra(EXTRA_ID, id)
    }

    setResult(Activity.RESULT_OK, data)
    finish()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val menuInflater = menuInflater
    menuInflater.inflate(R.menu.menu_add_note, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.item_save_note -> {
        saveNote()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}

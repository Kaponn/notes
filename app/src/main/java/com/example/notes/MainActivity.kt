package com.example.notes

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

const val ADD_NOTE_REQUEST = 1
const val EDIT_NOTE_REQUEST = 2

class MainActivity : AppCompatActivity() {

  private lateinit var viewModel: NoteViewModel
  private val swipeColor: ColorDrawable = ColorDrawable(Color.parseColor("#981815"))
  private val deleteIcon: Drawable by lazy { this.getDrawable(R.drawable.ic_delete)!! }
  private val adapter = NoteAdapter()


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val viewModelFactory = ViewModelFactory(application)

    button_add_note.setOnClickListener {
      val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
      startActivityForResult(intent, ADD_NOTE_REQUEST)
    }

    recycler_view.layoutManager = LinearLayoutManager(this)
    recycler_view.setHasFixedSize(true)
    recycler_view.adapter = adapter

    viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]
    viewModel.getAll().observe(this, Observer { notes ->
      adapter.submitList(notes)
    })

    val itemTouchCallback =
      object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
          recyclerView: RecyclerView,
          viewHolder: RecyclerView.ViewHolder,
          target: RecyclerView.ViewHolder
        ): Boolean {
          return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
          val removedPosition = viewHolder.adapterPosition
          val noteToDelete = adapter.getNoteAt(removedPosition)
          viewModel.delete(noteToDelete)
          Snackbar.make(main_activity, "Note deleted", Snackbar.LENGTH_LONG).setAction("UNDO") {
            viewModel.insert(noteToDelete)
          }.show()
        }

        override fun onChildDraw(
          c: Canvas,
          recyclerView: RecyclerView,
          viewHolder: RecyclerView.ViewHolder,
          dX: Float,
          dY: Float,
          actionState: Int,
          isCurrentlyActive: Boolean
        ) {
          super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

          val itemView = viewHolder.itemView
          // val iconMargin = (itemView.top + deleteIcon.intrinsicHeight) / 2

          if (dX > 0) {
            swipeColor.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
            deleteIcon.setBounds(
              itemView.left + 32, itemView.top + 32,
              itemView.left + deleteIcon.intrinsicWidth + 32, itemView.bottom - 32
            )
          } else {
            swipeColor.setBounds(
              itemView.right + dX.toInt(),
              itemView.top,
              itemView.right,
              itemView.bottom
            )
            deleteIcon.setBounds(
              itemView.right - deleteIcon.intrinsicWidth - 32,
              itemView.top + 32,
              itemView.right - 32,
              itemView.bottom - 32
            )
          }

//          swipeColor.draw(c)
          deleteIcon.setTint(resources.getColor(R.color.colorPrimaryDark))
          deleteIcon.draw(c)
        }
      }

    val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
    itemTouchHelper.attachToRecyclerView(recycler_view)

    adapter.onNoteClick = { note ->
      val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
      intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.id)
      intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.title)
      intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.description)
      intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.priority)
      startActivityForResult(intent, EDIT_NOTE_REQUEST)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
      val title = data!!.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
      val description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
      val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

      val note = Note(title = title, description = description, priority = priority)
      viewModel.insert(note)

      Snackbar.make(main_activity, "Note saved", Snackbar.LENGTH_SHORT).show()
    } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
      val id = data!!.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)

      if (id == -1) {
        Snackbar.make(main_activity, "Note can't be updated", Snackbar.LENGTH_SHORT).show()
        return
      }

      val title = data!!.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
      val description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
      val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

      val note = Note(title = title, description = description, priority = priority)
      note.id = id!!
      viewModel.update(note)
      Snackbar.make(main_activity, "Note updated", Snackbar.LENGTH_SHORT).show()
    } else {
      Snackbar.make(main_activity, "Note not saved", Snackbar.LENGTH_SHORT).show()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val menuInflater = menuInflater
    menuInflater.inflate(R.menu.main_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.item_delete_all_notes -> {
        viewModel.deleteAll()
        Snackbar.make(main_activity, "All notes deleted", Snackbar.LENGTH_SHORT).show()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}

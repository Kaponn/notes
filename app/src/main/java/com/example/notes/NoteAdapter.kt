package com.example.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteHolder>(
    object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
    }
) {
    var onNoteClick: ((Note) -> Unit)? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent,false)

        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.textViewTitle.text = getItem(position)?.title
        holder.textViewDescription.text = getItem(position)?.description
        holder.textViewPriority.text = getItem(position)?.priority?.toString()
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.text_view_title
        val textViewDescription: TextView = itemView.text_view_description
        val textViewPriority: TextView = itemView.text_view_priority

        init {
            itemView.setOnClickListener {
                onNoteClick?.invoke(getNoteAt(adapterPosition))
            }
        }
    }
}

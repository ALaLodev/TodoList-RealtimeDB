package com.alalodev.todolist_realtime

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alalodev.todolist_realtime.databinding.ItemTodoBinding

class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTodoBinding.bind(view)

    fun bind(todoTask: Pair<String, Todo>, onItemSelected: (Actions, String) -> Unit) {
        binding.tvTitle.text = todoTask.second.title
        binding.tvTDescription.text = todoTask.second.description
        binding.tvTReference.text = todoTask.first
        binding.ivDelete.setOnClickListener { onItemSelected(Actions.REMOVE, todoTask.first) }
        binding.ivDone.setOnClickListener { onItemSelected(Actions.DONE, todoTask.first) }
        Log.i("ariscurso", todoTask.second.toString())
        val color = if(todoTask.second.done == true){
            R.color.gold
        }else{
            R.color.purple
        }
        binding.cvItem.strokeColor = ContextCompat.getColor(binding.cvItem.context, color)
    }

}
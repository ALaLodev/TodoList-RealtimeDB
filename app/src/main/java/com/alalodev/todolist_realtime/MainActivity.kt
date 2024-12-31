package com.alalodev.todolist_realtime

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager.LayoutParams
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alalodev.todolist_realtime.data.FirebaseInstance
import com.alalodev.todolist_realtime.databinding.ActivityMainBinding
import com.alalodev.todolist_realtime.databinding.DialogAddTaskBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        setUI()
        setupListeners()
    }

    private fun setUI() {
        todoAdapter = TodoAdapter { action, reference ->
            when (action) {
                Actions.REMOVE -> firebaseInstance.removeFromDatabase(reference)
                Actions.DONE -> firebaseInstance.updateFromDatabase(reference)
            }

        }
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnAddTask -> {
                showDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val binding = DialogAddTaskBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(binding.root)

        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        binding.btnAddTask.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "No puedes dejar campos vacíos.", Toast.LENGTH_SHORT).show()
            } else {
                firebaseInstance.writeOnFirebase(title, description)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun setupListeners() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = getCleanSnapshot(snapshot)
                todoAdapter.setNewList(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("AristiDevs onCancelled", error.details)
            }
        }
        firebaseInstance.setupDatabaseListener(postListener)
    }

    private fun getCleanSnapshot(snapshot: DataSnapshot): List<Pair<String, Todo>> {
        //Pair(clave, valor)
        val list = snapshot.children.map { item ->
            Pair(item.key!!, item.getValue(Todo::class.java)!!)
        }
        return list
    }
}
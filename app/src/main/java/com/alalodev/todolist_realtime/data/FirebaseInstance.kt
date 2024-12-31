package com.alalodev.todolist_realtime.data

import android.content.Context
import com.alalodev.todolist_realtime.Todo
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database

class FirebaseInstance(context: Context) {

    private val database = com.google.firebase.ktx.Firebase.database
    private val myRef = database.reference

    init {
        FirebaseApp.initializeApp(context)
    }

    fun writeOnFirebase(title:String, description:String) {
        val newITem = myRef.push()
        newITem.setValue(Todo(title, description))
    }

    fun setupDatabaseListener(postListener: ValueEventListener) {
        database.reference.addValueEventListener(postListener)
    }

    fun removeFromDatabase(reference: String) {
        myRef.child(reference).removeValue()
    }

    fun updateFromDatabase(reference: String) {
        myRef.child(reference).child("done").setValue(true)
    }

}
package com.example.a123

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a123.databinding.ActivityAddToDoBinding

class AddToDo : AppCompatActivity() {

    private lateinit var binding: ActivityAddToDoBinding
    private lateinit var dbHandler: DbHandler
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
        dbHandler = DbHandler(context)

        binding.buttonAdd.setOnClickListener {
            val userTitle = binding.editTextTitle.text.toString().trim()
            val userDesc = binding.editTextDescription.text.toString().trim()

            if (validateInput(userTitle, userDesc)) {
                val started = System.currentTimeMillis()
                val toDo = ToDo(userTitle, userDesc, started, 0)
                dbHandler.addToDo(toDo)
                finish()
                startActivity(Intent(context, MainActivity::class.java))
            }
        }
    }

    private fun validateInput(title: String, description: String): Boolean {
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(context, "Please enter a description", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}

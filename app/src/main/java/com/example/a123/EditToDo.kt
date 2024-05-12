package com.example.a123

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditToDo : AppCompatActivity() {

    private lateinit var title: EditText
    private lateinit var des: EditText
    private lateinit var edit: Button
    private lateinit var dbHandler: DbHandler
    private lateinit var context: Context
    private var updateDate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_to_do)

        context = this
        dbHandler = DbHandler(context)

        title = findViewById(R.id.editToDoTextTitle)
        des = findViewById(R.id.editToDoTextDescription)
        edit = findViewById(R.id.buttonEdit)

        val id = intent.getStringExtra("id")
        val todo = dbHandler.getSingleToDo(id?.toInt() ?: 0)

        title.setText(todo?.title)
        des.setText(todo?.description)

        edit.setOnClickListener {
            val titleText = title.text.toString().trim()
            val descText = des.text.toString().trim()

            if (validateInput(titleText, descText)) {
                updateDate = System.currentTimeMillis()

                val toDo = ToDo(
                    id?.toInt() ?: 0,
                    titleText,
                    descText,
                    updateDate,
                    0
                )
                val state = dbHandler.updateSingleToDo(toDo)
                println(state)
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

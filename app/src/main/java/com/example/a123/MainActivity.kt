package com.example.a123

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var add: Button
    private lateinit var listView: ListView
    private lateinit var count: TextView
    private lateinit var context: Context
    private lateinit var dbHandler: DbHandler
    private lateinit var toDos: MutableList<ToDo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        dbHandler = DbHandler(context)
        add = findViewById(R.id.add)
        listView = findViewById(R.id.todolist)
        count = findViewById(R.id.todocount)
        toDos = mutableListOf()

        toDos = dbHandler.getAllToDos().toMutableList()

        val adapter = ToDoAdapter(context, R.layout.single_todo, toDos)

        listView.adapter = adapter

        // get todo counts from the table
        val countTodo = dbHandler.countToDo()
        count.text = "You have $countTodo todos"

        add.setOnClickListener {
            startActivity(Intent(context, AddToDo::class.java))
            finish()
        }

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val todo = toDos[position]
                val builder = AlertDialog.Builder(context)
                builder.setTitle(todo.title)
                builder.setMessage(todo.description)
                builder.setPositiveButton("Finished") { dialog, which ->
                    todo.finished = System.currentTimeMillis()
                    dbHandler.updateSingleToDo(todo)
                    finish()
                    startActivity(Intent(context, MainActivity::class.java))
                }
                builder.setNegativeButton("Delete") { dialog, which ->
                    dbHandler.deleteToDo(todo.id)
                    finish()
                    startActivity(Intent(context, MainActivity::class.java))
                }
                builder.setNeutralButton("Update") { dialog, which ->
                    val intent = Intent(context, EditToDo::class.java)
                    intent.putExtra("id", todo.id.toString())
                    startActivity(intent)
                    finish()
                }
                builder.show()
            }
    }
}

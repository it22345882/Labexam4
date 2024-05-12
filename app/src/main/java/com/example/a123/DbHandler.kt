package com.example.a123

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHandler(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val TABLE_CREATE_QUERY = ("CREATE TABLE $TABLE_NAME "
                + "($ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$TITLE TEXT,"
                + "$DESCRIPTION TEXT,"
                + "$STARTED TEXT,"
                + "$FINISHED TEXT"
                + ");")
        db.execSQL(TABLE_CREATE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE_QUERY)
        onCreate(db)
    }

    fun addToDo(toDo: ToDo) {
        val sqLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE, toDo.title)
        contentValues.put(DESCRIPTION, toDo.description)
        contentValues.put(STARTED, toDo.started)
        contentValues.put(FINISHED, toDo.finished)
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues)
        sqLiteDatabase.close()
    }

    fun countToDo(): Int {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        val count = cursor.count
        cursor.close()
        return count
    }

    fun getAllToDos(): List<ToDo> {
        val toDos: MutableList<ToDo> = ArrayList()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val toDo = ToDo(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getLong(4)
                )
                toDos.add(toDo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return toDos
    }

    fun deleteToDo(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun getSingleToDo(id: Int): ToDo? {
        val db = writableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME, arrayOf(ID, TITLE, DESCRIPTION, STARTED, FINISHED),
            "$ID=?", arrayOf(id.toString()), null, null, null
        )
        var toDo: ToDo? = null
        if (cursor.moveToFirst()) {
            toDo = ToDo(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getLong(3),
                cursor.getLong(4)
            )
        }
        cursor.close()
        return toDo
    }

    fun updateSingleToDo(toDo: ToDo): Int {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE, toDo.title)
        contentValues.put(DESCRIPTION, toDo.description)
        contentValues.put(STARTED, toDo.started)
        contentValues.put(FINISHED, toDo.finished)
        val status = db.update(TABLE_NAME, contentValues, "$ID=?", arrayOf(toDo.id.toString()))
        db.close()
        return status
    }

    companion object {
        private const val VERSION = 1
        private const val DB_NAME = "todo"
        private const val TABLE_NAME = "todo"

        // Column names
        private const val ID = "id"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val STARTED = "started"
        private const val FINISHED = "finished"
    }
}

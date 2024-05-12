package com.example.a123

class ToDo {
    var id: Int = 0
    var title: String = ""
    var description: String = ""
    var started: Long = 0
    var finished: Long = 0

    constructor()

    constructor(id: Int, title: String, description: String, started: Long, finished: Long) {
        this.id = id
        this.title = title
        this.description = description
        this.started = started
        this.finished = finished
    }

    constructor(title: String, description: String, started: Long, finished: Long) {
        this.title = title
        this.description = description
        this.started = started
        this.finished = finished
    }
}

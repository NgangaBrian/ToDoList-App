package com.example.todolist;

public class ToBeDone {
    String date;
    String todo;
    public ToBeDone() {
    }

    public ToBeDone(String date, String todo) {
        this.date = date;
        this.todo = todo;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
}

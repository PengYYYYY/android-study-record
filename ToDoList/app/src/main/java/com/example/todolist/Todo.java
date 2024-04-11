package com.example.todolist;

public class Todo {
    private String content;
    private boolean isComplete;

    public Todo(String content, int status) {
        this.content = content;
        this.isComplete = false;
    }

    public String getContent( ) {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

}

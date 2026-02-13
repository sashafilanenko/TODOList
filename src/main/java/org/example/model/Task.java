package org.example.model;

import java.time.LocalDateTime;

public class Task {

    private int id;
    private String title;
    private String category;
    private boolean isCompleted;
    private LocalDateTime createAt;

    public Task(String title, String category){
        this.title = title;
        this.category = category;
        this.isCompleted = false;
        this.createAt = LocalDateTime.now();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean complete) {
        isCompleted = complete;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    @Override
    public String toString(){
        return "" + title;
    }
}

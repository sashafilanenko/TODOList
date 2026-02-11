package org.example.model;

import java.time.LocalDateTime;

public class Task {

    private int id;
    private String title;
    private String description;
    private boolean isCompleted;
    private LocalDateTime createAt;

    public Task(String title, String description){
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isCompleted=" + isCompleted +
                ", createAt=" + createAt +
                '}';
    }
}

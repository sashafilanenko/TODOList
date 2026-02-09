package org.example;

public class TODO {
    private final Long id;
    private final String text;

    public TODO(Long id, String text){
        this.id = id;
        this.text = text;
    }

    public Long getId(){
        return id;
    }

    public String getText(){
        return text;
    }
}

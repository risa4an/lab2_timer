package com.example.tabataapplication.Models;

public class Sequence {
    private int id;
    private String title;
    private int colour;

    public Sequence(int id, String title, int colour) {
        this.id = id;
        this.title = title;
        this.colour = colour;
    }

    public Sequence(String title, int colour) {
        this.title = title;
        this.colour = colour;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public String getTitle() {
        return title;
    }

    public int getColour() {
        return colour;
    }
}

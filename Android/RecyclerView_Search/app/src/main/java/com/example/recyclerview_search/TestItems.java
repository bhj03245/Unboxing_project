package com.example.recyclerview_search;

import java.io.Serializable;

public class TestItems implements Serializable {

    private String title;
    private String name;
    public TestItems(String title){
        this.title = title;
        this.name  = "TT";
    }

    public TestItems(String title, String name){
        this.title=title;
        this.name=name;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

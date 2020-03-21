package com.example.listviewtest;


public class RecycleVO {
    private String num;
    private String title;
    private String size;
    private String length;
    //private String url;

    public void setNum(String num){
        this.num = num;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setSize(String size){
        this.size = size;
    }
    public void setLength(String length){
        this.length = length;
    }

    public String getNum(){
        return this.num;
    }
    public String getTitle(){
        return this.title;
    }
    public String getSize(){
        return this.size;
    }
    public String getLength(){
        return this.length;
    }

    public RecycleVO(String num, String title, String size, String length){
        this.num = num;
        this.title = title;
        this.size = size;
        this.length = length;
    }
}

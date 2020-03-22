package com.example.recyclerviewtest;

public class VideoVO {

    private String num;
    private String title;
    private String size;
    private String length;
    private String url;

    public VideoVO(){}

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
    public void setUrl(String url){this.url = url;}

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
    public String getUrl(){return this.url;}
}

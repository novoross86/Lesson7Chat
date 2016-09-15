package com.example.admin.chatapplication;


public class Post {

    private String channel, text, title;

    public Post(String channel, String text, String title){
        this.channel = channel;
        this.text = text;
        this.title = title;
    }

    public Post(){

    }

    public String getChannel(){
        return channel;
    }

    public void setChannel(String channel){
        this.channel = channel;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

}

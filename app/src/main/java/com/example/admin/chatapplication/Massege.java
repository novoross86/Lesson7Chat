package com.example.admin.chatapplication;

/**
 * Created by admin on 07.10.2016.
 */

public class Massege {

    private String name;
    private String msg;

    public Massege(String name, String msg){
        this.name = name;
        this.msg = msg;
    }

    public Massege(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

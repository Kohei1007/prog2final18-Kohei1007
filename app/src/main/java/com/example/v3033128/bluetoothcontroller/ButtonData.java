package com.example.v3033128.bluetoothcontroller;

public class ButtonData {
    long id;
    private String name;
    private int width,height;
    private int marginX,marginY;
    private String data;

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getMarginX() {
        return marginX;
    }

    public void setMarginX(int marginX) {
        this.marginX = marginX;
    }

    public int getMarginY() {
        return marginY;
    }

    public void setMarginY(int marginY) {
        this.marginY = marginY;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setAllData(String name,int width,int height,int x,int y,String data){
        this.name = name;
        this.width = width;
        this.height = height;
        this.marginX = x;
        this.marginY = y;
        this.data = data;
    }
}

package com.example.achtung_die_kurve;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

    private String username;
    private boolean isHost;
    private int color;
    private int circleSize = 10;
    private int currentY = 800;
    private int currentX = 200;
    private int directionX = 0;
    private int directionY = -10;
    private List<PointF> points;

    public Player(String username, boolean isHost) {
        this.username = username;
        this.isHost = isHost;
        points = new ArrayList<>();
    }

    public int getDirectionX() {
        return directionX;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    public void setDirectionY(int directionY) {
        this.directionY = directionY;
    }

    public String getUsername() {
        return username;
    }

    public boolean isHost() {
        return isHost;
    }

    public int getCircleSize(){
        return circleSize;
    }

    public int getCurrentY(){
        return currentY;
    }

    public void setCurrentY(int currentY){
        this.currentY = currentY;
    }

    public int getCurrentX(){
        return currentX;
    }

    public void setCurrentX(int currentX){
        this.currentX = currentX;
    }

    public List<PointF> getPoints(){
        return points;
    }

    public void addPoint(PointF point){
        points.add(point);
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getColor(){
        return color;
    }
}
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
    private List<PointF> pointsXY;

    private int playerNumber;

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    private int points = 0;

    public Player(String username, boolean isHost) {
        this.username = username;
        this.isHost = isHost;
        pointsXY = new ArrayList<>();
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
//test
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

    public List<PointF> getPointsXY(){
        return pointsXY;
    }

    public void addPoint(PointF point){
        pointsXY.add(point);
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getColor(){
        return color;
    }
}
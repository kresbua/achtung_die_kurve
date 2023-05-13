package com.example.achtung_die_kurve;

import android.graphics.Color;
import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

    private String username;
    private String ip;
    private boolean isHost;
    private Color color;
    private int points;
    private int direction; //-1=links, 0=gerade, 1=rechts
    private List<Pair<Float, Float>> coordinates;
    private int radius;
    private int holeDistance;
    private int speed;
    private boolean reverse;

    public Player(String username, boolean isHost) {
        this.username = username;
        this.isHost = isHost;
        coordinates = new ArrayList<>();
    }
    public void addCoordinates(float x, float y){
        coordinates.add(new Pair<>(x, y));
    }

    public List<Pair<Float, Float>> getCoordinates(){
        return coordinates;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getHoleDistance() {
        return holeDistance;
    }

    public void setHoleDistance(int holeDistance) {
        this.holeDistance = holeDistance;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
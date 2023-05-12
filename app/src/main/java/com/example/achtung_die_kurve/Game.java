package com.example.achtung_die_kurve;

import android.util.SparseBooleanArray;

import java.io.Serializable;

public class Game implements Serializable {

    private String gameName;
    private boolean isPrivate;
    private int playerNumber;
    private String inetAddress;
    private int port;

    private String password;


    public Game(String gameName, boolean isPrivate, String password) {
        this.gameName = gameName;
        this.isPrivate = isPrivate;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getGameName() {
        return gameName;
    }

    public boolean getPrivacyStatus() {
        return isPrivate;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    public String privacyStatusToString(){
        if(isPrivate){
            return "private";
        }else{
            return "public";
        }
    }
}

package com.example.achtung_die_kurve;

import android.util.SparseBooleanArray;

public class Game {

    private String gameName;
    private boolean isPrivate;
    private int playerNumber;
    private String inetAddress;
    private int port;

    public Game(String gameName, boolean isPrivate, int playerNumber, String inetAddress, int port) {
        this.gameName = gameName;
        this.isPrivate = isPrivate;
        this.playerNumber = playerNumber;
        this.inetAddress = inetAddress;
        this.port = port;
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

package com.example.achtung_die_kurve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Game implements Serializable {

    private String gameName;
    private boolean isPrivate;
    private String inetAddress;
    private int port;
    private String password;

    private ArrayList<Player> players;

    private HashMap<String, Boolean> items;

    public Game(String gameName, boolean isPrivate, String password) {
        this.gameName = gameName;
        this.isPrivate = isPrivate;
        this.password = password;
        players = new ArrayList<>();
        items = new HashMap<String, Boolean>();
    }

    public Game(String gameName, boolean isPrivate) {
        this.gameName = gameName;
        this.isPrivate = isPrivate;
        players = new ArrayList<>();
        items = new HashMap<String, Boolean>();
    }



    public HashMap<String, Boolean> getItems(){
        return items;
    }

    public int getPlayerNumber(){return players.size();}

    public void addItem(String itemName, Boolean active){
        items.put(itemName, active);
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
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

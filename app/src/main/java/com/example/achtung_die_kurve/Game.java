package com.example.achtung_die_kurve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Game implements Serializable {

    private String gameName;
    private boolean isPrivate;

    private int playerNumber;

    private String publicationAddress;

    private String inetAddressTCP;

    private String informationAddress;

    private int port;
    private String password;

    private ArrayList<Player> players;

    private HashMap<String, Boolean> items;
    private HashMap<String, Boolean> availableColors;

    public Game(String gameName, boolean isPrivate, String password) {
        this.gameName = gameName;
        this.isPrivate = isPrivate;
        this.password = password;
        players = new ArrayList<>();
        items = new HashMap<String, Boolean>();
        availableColors = new HashMap<String, Boolean>();
    }

    public Game(String gameName, boolean isPrivate) {
        this.gameName = gameName;
        this.isPrivate = isPrivate;
        players = new ArrayList<>();
        items = new HashMap<String, Boolean>();
    }

    public HashMap<String, Boolean> getAvailableColors() {
        return availableColors;
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

    public String getPublicationAddress() {
        return publicationAddress;
    }

    public void setPublicationAddress(String publicationAddress) {
        this.publicationAddress = publicationAddress;
    }

    public String getInetAddressTCP() {
        return inetAddressTCP;
    }

    public void setInetAddressTCP(String inetAddressTCP) {
        this.inetAddressTCP = inetAddressTCP;
    }

    public String getInformationAddress() {
        return informationAddress;
    }

    public void setInformationAddress(String informationAddress) {
        this.informationAddress = informationAddress;
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

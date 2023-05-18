package com.example.achtung_die_kurve;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GameReceiver {

    private String inetAddress;
    private int port;
    private DatagramSocket datagramSocket;
    private Game myGame;
    private boolean closeTCPSocket = false;

    public ArrayList<Game> searchGames(){
        ArrayList<Game> foundGames = new ArrayList<>();
        System.out.println("METHODE CALLED!");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("THREAD CALLED!");
                String addressPrefix = "224.0.0.";
                MulticastSocket tempSocket = null;
                byte[] buf;
                String received = "";
                for (int i = 10; i <= 30; i++){
                    try {
                        InetAddress tempAddress = InetAddress.getByName(addressPrefix + i);
                        tempSocket = new MulticastSocket(4446);
                        tempSocket.joinGroup(tempAddress);
                        tempSocket.setSoTimeout(100); //Abbruch, wenn nach 100ms nichts empfangen wurde
                        buf = new byte[2560];
                        DatagramPacket dp = new DatagramPacket(buf, buf.length);
                        tempSocket.receive(dp);
                        received = data(buf);
                        if (received.length() > 0) {
                            System.out.println("------GAME FOUND!!!!-------");
                            Gson gson = new Gson();
                            foundGames.add(gson.fromJson(received, Game.class));
                        }
                    }catch (SocketTimeoutException e){
                        System.out.println("No Game found!");
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    } catch (SocketException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                assert tempSocket != null;
                tempSocket.close();
                System.out.println("Thread finished!");
            }
        };
        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return foundGames;
    }

    public String getGameInformation(String address){
        final String[] received = {""};
        InetSocketAddress inetSocketAddress = new InetSocketAddress(address, 4446);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    MulticastSocket multicastSocket = new MulticastSocket(inetSocketAddress);
                    byte[] buf = new byte[2600];
                    DatagramPacket dp = new DatagramPacket(buf, buf.length);
                    multicastSocket.receive(dp);
                    received[0] = data(buf);
                    multicastSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(r).start();
        return received[0];
    }

    public void initiateTCPConnection(String address, Player myPlayer, Game game){
        myGame = game;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                DataInputStream inputStream = null;
                DataOutputStream outputStream = null;
                InetSocketAddress inetSocketAddress = new InetSocketAddress(address, 1990 + myGame.getPlayerNumber());
                System.out.println("Port: " + (1990 + myGame.getPlayerNumber()));
                while (!closeTCPSocket){
                    try {
                        socket = new Socket();
                        socket.bind(inetSocketAddress);
                        inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                        outputStream = new DataOutputStream(socket.getOutputStream());
                        Gson gson = new Gson();
                        String playerJSONString = gson.toJson(myPlayer);
                        outputStream.writeUTF(playerJSONString);
                        String information = inputStream.readUTF();
                        handleInformation(information);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    assert socket != null;
                    socket.close();
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(r).start();
    }

    public void handleInformation(String information){
        if(information.toCharArray()[0] == '{'){
            Gson gson = new Gson();
            Player player = gson.fromJson(information, Player.class);
            myGame.addPlayer(player);
        }else{
            switch (information){
                case "start game":
                    //...
                    break;
                case "start":
                    //...
                    break;
                case "stop":
                    //...
                    break;
                case "end game":
                    //...
                    break;
            }
        }
    }


    public String data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }
}

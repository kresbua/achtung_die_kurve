package com.example.achtung_die_kurve;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GameReceiver {

    private String inetAddress;
    private int port;
    private DatagramSocket datagramSocket;
    private Game myGame;

    public ArrayList<Game> searchGames(){
        ArrayList<Game> foundGames = new ArrayList<>();
        System.out.println("METHODE CALLED!");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("THREAD CALLED!");
                String addressPrefix = "10.10.30.";
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

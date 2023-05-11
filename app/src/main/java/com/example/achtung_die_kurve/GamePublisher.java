package com.example.achtung_die_kurve;

import android.util.JsonWriter;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GamePublisher {

    private String inetAddress;
    private int port;
    private DatagramSocket datagramSocket;
    private Game myGame;
    private boolean gameIsFull;

    public boolean checkFreeAddresses(){
        String addressPrefix = "224.0.0.";
        MulticastSocket tempSocket;
        byte[] buf;
        String received = "";
        for (int i = 10; i <= 30; i++){
            try {
                InetAddress tempAddress = InetAddress.getByName(addressPrefix + i);
                tempSocket = new MulticastSocket(4446);
                tempSocket.joinGroup(tempAddress);
                buf = new byte[256];
                for (int j = 0; j < 100; j++){
                    DatagramPacket dp = new DatagramPacket(buf, buf.length);
                    tempSocket.receive(dp);
                    received = "";
                    received = data(buf);
                    if(received.length() > 0){
                        break;
                    }
                }

                if(received.length() == 0){
                    inetAddress = addressPrefix + i;
                    return true;
                }

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public void startPublishingGame(){
        try {
            InetAddress publicationAddress = InetAddress.getByName(inetAddress);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        datagramSocket = new DatagramSocket();
                        byte[] buf;
                        String jsonInString = new Gson().toJson(myGame);
                        buf = jsonInString.getBytes();
                        while (!gameIsFull){
                            DatagramPacket dp = new DatagramPacket(buf, buf.length, publicationAddress, 4446);
                            datagramSocket.send(dp);
                        }

                    } catch (SocketException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            new Thread(r).start();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
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

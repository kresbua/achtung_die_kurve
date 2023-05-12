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
import java.net.UnknownHostException;

public class GamePublisher {

    private String inetAddressUDP;
    private String inetAddressTCP;
    private int tcpPort = 800;
    private DatagramSocket datagramSocket;
    private Game myGame;
    private boolean gameIsFull;

    private boolean closeTCPSocket = false;

    public boolean checkFreeAddresses(){
        String addressPrefix = "224.0.0.";
        MulticastSocket tempSocket = null;
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
                    received = data(buf);
                    if(received.length() > 0){
                        break;
                    }
                }

                if(received.length() == 0){
                    inetAddressUDP = addressPrefix + i;
                    tempSocket.close();
                    return true;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        tempSocket.close();
        return false;
    }

    public void launchTCPServer(){
        if(checkFreeTCPAddresses()){
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    ServerSocket serverSocket = null;
                    Socket socket = null;
                    DataInputStream inputStream = null;
                    DataOutputStream outputStream = null;
                    while (!closeTCPSocket){
                        try {
                            serverSocket = new ServerSocket(tcpPort);
                            serverSocket.bind(new InetSocketAddress(inetAddressTCP, tcpPort));
                            socket = serverSocket.accept();
                            inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                            outputStream = new DataOutputStream(socket.getOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        assert socket != null;
                        socket.close();
                        serverSocket.close();
                        inputStream.close();
                        outputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            new Thread(r).start();
        }
    }


    public boolean checkFreeTCPAddresses(){
        String addressPrefix = "225.0.0.";
        boolean addressIsFree = false;

        for (int i = 10; i < 30; i++){
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(addressPrefix + i, tcpPort), 2000); // Timeout nach 2 Sekunden
                System.out.println("Adresse ist besitzt: " + addressPrefix + i + ":" + tcpPort);
                socket.close();
            } catch (Exception e) {
                System.out.println("Adresse ist frei: " + addressPrefix + i + ":" + tcpPort);
                inetAddressTCP = addressPrefix + i;
                addressIsFree = true;
                break;
            }
        }
        if(!addressIsFree){
            System.out.println("Es konnten keine freien Adressen gefunden werden!");
            return false;
        }
        return true;
    }

    public void startPublishingGame(){
        try {
            InetAddress publicationAddress = InetAddress.getByName(inetAddressUDP);
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
                        datagramSocket.close();

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

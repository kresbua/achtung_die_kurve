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

public class GamePublisher {

    private String inetAddressUDP;

    private String inetAddressUDPInformation = "226.0.0.";
    private String inetAddressTCP = "225.0.0.";
    private int tcpPort = 800;
    private Game myGame;

    private Player myPlayer;
    private boolean gameIsFull = false;
    private boolean closeTCPSocket = false;

    public GamePublisher(Game myGame, Player myPlayer){
        this.myGame = myGame;
        this.myPlayer = myPlayer;
    }

    public boolean checkFreeAddresses(){
        boolean[] exitStatus = {false};

        Runnable r = new Runnable() {
            @Override
            public void run() {
                String addressPrefix = "224.0.0.";
                MulticastSocket tempSocket = null;
                byte[] buf;
                for (int i = 10; i <= 30; i++){
                    try {
                        InetAddress tempAddress = InetAddress.getByName(addressPrefix + i);
                        tempSocket = new MulticastSocket(4446);
                        tempSocket.joinGroup(tempAddress);
                        tempSocket.setSoTimeout(100);
                        buf = new byte[256];
                        for (int j = 0; j < 100; j++) {
                            DatagramPacket dp = new DatagramPacket(buf, buf.length);
                            tempSocket.receive(dp);
                        }
                    }catch (SocketTimeoutException e){
                        inetAddressUDP = addressPrefix + i;
                        inetAddressUDPInformation += i;
                        inetAddressTCP +=i;
                        myGame.setInetAddressTCP(inetAddressTCP);
                        myGame.setInformationAddress(inetAddressUDPInformation);
                        exitStatus[0] = true;
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                assert tempSocket != null;
                tempSocket.close();
            }
        };
        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return exitStatus[0];
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
                    int i = 1;
                    while (!closeTCPSocket){
                        try {
                            serverSocket = new ServerSocket();
                            serverSocket.bind(new InetSocketAddress(inetAddressTCP, 1990+i));
                            socket = serverSocket.accept();
                            inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                            outputStream = new DataOutputStream(socket.getOutputStream());
                            String playerJSONString = inputStream.readUTF();
                            Gson gson = new Gson();
                            Player player = gson.fromJson(playerJSONString, Player.class);
                            myGame.addPlayer(player);
                            handlePlayer(socket, inputStream, outputStream, player);
                            i++;
                            if(i == 4){
                                gameIsFull = true;
                                closeTCPSocket = true;
                            }
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



    public void handlePlayer(Socket socket, DataInputStream inputStream, DataOutputStream outputStream, Player player){
        Runnable r = new Runnable() {
            @Override
            public void run() {

            }
        };
        new Thread(r).start();
    }

    public Game getGame(){return myGame;}


    public boolean checkFreeTCPAddresses(){
        String addressPrefix = "225.0.0.";
        final boolean[] addressIsFree = {false};

        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 10; i < 30; i++){
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(addressPrefix + i, tcpPort), 200);
                        System.out.println("Adresse ist besetzt: " + addressPrefix + i + ":" + tcpPort);
                        socket.close();
                    } catch (Exception e) {
                        System.out.println("Adresse ist frei: " + addressPrefix + i + ":" + tcpPort);
                        inetAddressTCP = addressPrefix + i;
                        addressIsFree[0] = true;
                        break;
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return addressIsFree[0];
    }

    public void startPublishingGame(){
        if(checkFreeAddresses()){
            System.out.println("START PUBLISHING!");
            try {
                InetAddress publicationAddress = InetAddress.getByName(inetAddressUDP);
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DatagramSocket datagramSocket = new DatagramSocket();
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
    }

    public Player getPlayer(){
        final Player[] newPlayer = new Player[1];

        Runnable r = new Runnable() {
            @Override
            public void run() {
                MulticastSocket multicastSocket = null;
                try {
                    InetAddress inetAddress = InetAddress.getByName(inetAddressTCP);
                    multicastSocket = new MulticastSocket(4446);
                    multicastSocket.joinGroup(inetAddress);
                    byte[] buf = new byte[2600];
                    DatagramPacket dp = new DatagramPacket(buf, buf.length);
                    multicastSocket.receive(dp);
                    System.out.println("--------RECEIVED----------");
                    Gson gson = new Gson();
                    if(data(buf).length() > 0){
                        newPlayer[0] = gson.fromJson(data(buf), Player.class);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
                //multicastSocket.close();
        };
        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return newPlayer[0];
    }

    public void sendGameInformation(String information){
        try {
            InetAddress publicationAddress = InetAddress.getByName(inetAddressUDPInformation);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        DatagramSocket datagramSocket = new DatagramSocket();
                        byte[] buf;
                        buf = (myPlayer.getUsername() + ": " + information).getBytes();
                        DatagramPacket dp = new DatagramPacket(buf, buf.length, publicationAddress, 4446);
                        datagramSocket.send(dp);
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

    public void sendGameInformation(String information, String destinationAddress){
        try {
            InetAddress publicationAddress = InetAddress.getByName(destinationAddress);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        DatagramSocket datagramSocket = new DatagramSocket();
                        byte[] buf;
                        buf = (myPlayer.getUsername() + ": " + information).getBytes();
                        DatagramPacket dp = new DatagramPacket(buf, buf.length, publicationAddress, 4446);
                        datagramSocket.send(dp);
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

//package com.example.rustielin.chaoticevil;
//
//import android.util.Log;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.SocketException;
//
///**
// * Created by Rustie Lin on 10/3/2016.
// */
//
//public class ClientSend implements Runnable {
//
//    public String IP = "10.142.15.19";
//    public int PORT = 5006;
//    public String TEXT = "HTC One m8 CONNECTION ESTABLISHED";
//    public DatagramSocket udpSocket;
//
//    @Override
//    public void run() {
//        try {
//            // reset and keep connecting
//            if (udpSocket == null) {
//                udpSocket = new DatagramSocket(null);
//                udpSocket.setReuseAddress(true);
//                udpSocket.setBroadcast(true);
//                udpSocket.bind(new InetSocketAddress(PORT));
//            }
////            udpSocket = new DatagramSocket(PORT);
//            InetAddress serverAddr = InetAddress.getByName(IP);
//            byte[] buf = (TEXT).getBytes();
//            DatagramPacket packet = new DatagramPacket(buf, buf.length,serverAddr, PORT);
//            udpSocket.send(packet);
//        } catch (SocketException e) {
//            Log.e("Udp:", "Socket Error:", e);
//        } catch (IOException e) {
//            Log.e("Udp Send:", "IO Error:", e);
//        }
//    }
//
//    public void setIP(String ip){
//        IP = ip;
//    }
//
//    public void setPORT(int port){
//        PORT = port;
//    }
//
//    public void setText(String text){
//        TEXT = text;
//    }
//
//}
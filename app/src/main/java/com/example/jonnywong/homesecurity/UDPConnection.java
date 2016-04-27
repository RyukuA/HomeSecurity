package com.example.jonnywong.homesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Jonny Wong on 4/25/2016.
 */
public class UDPConnection extends AppCompatActivity {
   // public class Client implements Runnable {
        //private final static String BeagleIP = "192.168.7.2";
    String udpMsg = "";
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       Intent DataRecieve = getIntent();
       Bundle poop = DataRecieve.getExtras();

       udpMsg = poop.getString("Azimuth");
       System.out.println("JONNY IS A BITCH" + udpMsg);
       new Client(udpMsg, "X");
   }
       // Intent DataRecieve = getIntent();
   // Bundle poop = DataRecieve.getExtras();

        //String udpMsg = poop.getString("Azimuth");
        //udpMsg = poop.getString("Azimuth");

        //Log.d("udpMsg", udpMsg);
        public class Client implements Runnable {
            byte[] sendData = new byte[1024];

            //Constructor for taking just the slider value
            public Client(int progress) {
                // TODO Auto-generated constructor stub
                udpMsg = String.valueOf(progress) + "\0";
                run();
            }

            //Constructor for determining which motor to use
            public Client(String value, String axis) {
                // TODO Auto-generated constructor stub
                udpMsg = String.valueOf(value) + axis + "\0";
                //This will come in handy on the server side.
                //No real use on client side, other than setting
                //So the server can differentiate between the motors

                run();
            }

            //This is the function to run the UDP server
            //I call this function in my overloaded constructors

            @Override
            public void run() {
                try {
                    // TODO Auto-generated method stub
                    InetAddress serverAddr = InetAddress.getByName("192.168.0.23");
                    //note that 192.168.0.23 is the static IP address we used for the Wi-Fi adapter

                    //InetAddress serverAddr = InetAddress.getByName("192.168.7.2"); //Address of the BeagleBone

                    DatagramSocket socket = new DatagramSocket();
                    sendData = udpMsg.getBytes();
                    //There is also an error with sending data through the DatagramPacket


                    Log.d("IP", serverAddr.toString()); //This is printing out as /192.168.7.2

                    Log.d("Test0", "Before new DatagramPacket\n");
                    DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverAddr, 45678);
                    Log.d("Test1.5", "After the new DatagramPacket\n");

                    socket.send(packet); //This is where the exception is caught. Not sure why yet

                    Log.d("Test1", "The program is in the runUDP\n");
                    //dataSent.setText("Data has been sent to the server!");
                } catch (Exception e) {
                    Log.d("Test5", "Basic Exception", e);
                }
            }
        }

}

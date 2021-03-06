package com.example.jonnywong.homesecurity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import android.view.View.OnClickListener;
import android.view.View;
import android.util.Log;
import android.widget.TextView;

public class RaspberryMain extends AppCompatActivity implements OnClickListener{
    private static final int UDP_SERVER_PORT = 45788; //Port Number
    private String RaspberryIP = "192.168.7.2";
    private Button lock, unlock, lights_on, lights_off;
    private TextView dataSent;
    String udpMsg = "";
    byte[] sendData = new byte[1024];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raspberry_main);

        lock = (Button)findViewById(R.id.lock);
        unlock = (Button)findViewById(R.id.unlock);
        lights_off = (Button)findViewById(R.id.lights_off);
        lights_on = (Button)findViewById(R.id.lights_on);

        lock.setOnClickListener(this);
        unlock.setOnClickListener(this);
        lights_off.setOnClickListener(this);
        lights_on.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int lock_value = 200;
        int unlock_value = 0;
        Double lights_off_value = 0d;
        Double lights_on_value = 1d;
        switch (v.getId())
        {
            case R.id.lock:

                break;
            case R.id.unlock:

                break;
            case R.id.lights_off:
                new BackgroundProcess().execute(lights_off_value);
                break;
            case R.id.lights_on:
                new BackgroundProcess().execute(lights_on_value);
                break;
        }
    }
    /*
    public class Client implements Runnable {
        //private final static String BeagleIP = "192.168.7.2";

        String udpMsg = "";
        //Log.d("udpMsg", udpMsg);
        byte[] sendData = new byte[1024];
        //Constructor for taking just the slider value
        public Client(int progress) {
            // TODO Auto-generated constructor stub
            udpMsg = String.valueOf(progress) + "\0";
            run();
        }

        //Constructor for determining which motor to use
        public Client(int value, String operation) {
            // TODO Auto-generated constructor stub
            udpMsg = String.valueOf(value) + operation + "\0";
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
                InetAddress serverAddr = InetAddress.getByName("10.0.0.13");               //note that 192.168.0.23 is the static IP address we used for the Wi-Fi adapter

                //InetAddress serverAddr = InetAddress.getByName("192.168.7.2"); //Address of the BeagleBone

                DatagramSocket socket = new DatagramSocket();
                sendData = udpMsg.getBytes();
                //There is also an error with sending data through the DatagramPacket


                Log.d("IP", serverAddr.toString()); //This is printing out as /192.168.7.2

                Log.d("Test0", "Before new DatagramPacket\n");
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverAddr, 45889);
                Log.d("Test1.5", "After the new DatagramPacket\n");

                socket.send(packet); //This is where the exception is caught. Not sure why yet

                Log.d("Test1", "The program is in the runUDP\n");
                dataSent.setText("Data has been sent to the server!");
            }
            catch (Exception e)
            {
                Log.d("Test5", "Basic Exception", e);
            }
        }

    } */

    public class BackgroundProcess extends AsyncTask<Double, Void, Double> {
        @Override
        protected Double doInBackground(Double... arg0) {

            double value = arg0[0];
            int change = (int) value;
            // udpMsg = String.valueOf(azimut) + ", " + String.valueOf(pitch) + ", " + String.valueOf(roll) + "\0";
           // udpMsg =  String.valueOf(pitch*1000) + "," + String.valueOf(roll*1000) + "\0";
            udpMsg =  String.valueOf(change) + "\0";
            Log.d("Udp String", udpMsg);
            try {
                // TODO Auto-generated method stub
                InetAddress serverAddr = InetAddress.getByName("10.0.0.13");
                //note that 192.168.0.23 is the static IP address we used for the Wi-Fi adapter

                //InetAddress serverAddr = InetAddress.getByName("192.168.7.2"); //Address of the BeagleBone

                DatagramSocket socket = new DatagramSocket();
                sendData = udpMsg.getBytes();
                //There is also an error with sending data through the DatagramPacket


                Log.d("IP", serverAddr.toString()); //This is printing out as /192.168.7.2

                Log.d("Test0", "Before new DatagramPacket\n");
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverAddr, 45889);
                Log.d("Test1.5", "After the new DatagramPacket\n");

                socket.send(packet); //This is where the exception is caught. Not sure why yet

                Log.d("Test1", "The program is in the runUDP\n");
                //dataSent.setText("Data has been sent to the server!");
            }
            catch (Exception e)
            {
                Log.d("Test5", "Basic Exception", e);
            }

            return value;
        }


    }
}

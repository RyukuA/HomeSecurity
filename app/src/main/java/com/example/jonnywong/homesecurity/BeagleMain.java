package com.example.jonnywong.homesecurity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class BeagleMain extends AppCompatActivity implements SensorEventListener {
    private static final int UDP_SERVER_PORT = 45786; //Port Number
    private String BeagleIP = "10.0.0.3";
    private TextView dataSent;
    private TextView x_axis, y_axis, z_axis;
    private SensorManager mSensorManager;

    Sensor accelerometer;
    Sensor magnetometer;
    Float azimut;
    Float pitch;
    Float roll;
    String udpMsg = "";
    byte[] sendData = new byte[1024];
    DatagramPacket packet;
    DatagramSocket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beaglebone_main);
        x_axis = (TextView) findViewById(R.id.x_axis);
        y_axis = (TextView) findViewById(R.id.y_axis);
        z_axis = (TextView) findViewById(R.id.z_axis);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
       // new BackgroundProcess().execute(azimut, pitch, roll);


       // Intent UDPIntent = new Intent(this, UDPConnection.class);
       // UDPIntent.putExtra("Azimuth", azimut);
        //startActivity(UDPIntent);

        //new UDPConnection(azimut, "X");
        //new Client(azimut, "X");
        //new Thread(new Client(pitch, "Y")).start();
       // new Thread(new Client(roll, "Z")).start();
        /*
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } */

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
        public Client(Float value, String axis) {
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
            }
            catch (Exception e)
            {
                Log.d("Test5", "Basic Exception", e);
            }
        }

    }
*/
    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
    float[] mGravity;
    float[] mGeomagnetic;
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if(mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if(success) {
                float orientation[] = new float[3];

                    SensorManager.getOrientation(R, orientation);

                    azimut = orientation[0]; //Z Azis
                    pitch = orientation[1]; //X Axis
                    roll = orientation[2]; //Y Axis

                    azimut = Math.round(azimut * 1000.0f) / 1000.0f;
                    pitch = Math.round(pitch * 1000.0f) / 1000.0f;
                    roll = Math.round(roll * 1000.0f) / 1000.0f;


                    new BackgroundProcess().execute(azimut, pitch, roll);
                /*
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                */

            }

        }

       // udpMsg = String.valueOf(azimut) + ", " + String.valueOf(pitch) + ", " + String.valueOf(roll) + "\0";
        //Log.d("UDPMSG", udpMsg);
        /*
        try {
            // TODO Auto-generated method stub
            InetAddress serverAddr = InetAddress.getByName("192.168.0.23");
            //note that 192.168.0.23 is the static IP address we used for the Wi-Fi adapter

            //InetAddress serverAddr = InetAddress.getByName("192.168.7.2"); //Address of the BeagleBone

            socket = new DatagramSocket();
            sendData = udpMsg.getBytes();
            //There is also an error with sending data through the DatagramPacket


            Log.d("IP", serverAddr.toString()); //This is printing out as /192.168.7.2

            Log.d("Test0", "Before new DatagramPacket\n");
            packet = new DatagramPacket(sendData, sendData.length, serverAddr, 45678);
            Log.d("Test1.5", "After the new DatagramPacket\n");

           // socket.send(packet); //This is where the exception is caught. Not sure why yet

            Log.d("Test1", "The program is in the runUDP\n");
            //dataSent.setText("Data has been sent to the server!");
        }
        catch (Exception e)
        {
            Log.d("Test5", "Basic Exception", e);
        }
        */
        try {
            Log.d("X Axis", Float.toString(pitch));
            Log.d("Y Axis", Float.toString(roll));
            Log.d("Z Axis", Float.toString(azimut));
            x_axis.setText( "Orientation X: " + Float.toString(pitch));
            y_axis.setText("Orientation Y: " + Float.toString(roll));
            z_axis.setText("Orientation Z: " + Float.toString(azimut));
            /*
            new Thread(new Client(azimut, "X")).start();
            new Thread(new Client(pitch, "Y")).start();
            new Thread(new Client(roll, "Z")).start();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            //new BackgroundProcess().execute(azimut, pitch, roll);

        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        /*
        BeagleMain.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    */
    }



    public class BackgroundProcess extends AsyncTask<Float, Void,  Float> {
        @Override
        protected Float doInBackground(Float... arg0) {

            Float azimut = arg0[0];
            Float pitch = arg0[1];
            Float roll = arg0[2];
           // udpMsg = String.valueOf(azimut) + ", " + String.valueOf(pitch) + ", " + String.valueOf(roll) + "\0";
            udpMsg =  String.valueOf(pitch*1000) + "," + String.valueOf(roll*1000) + "\0";
            Log.d("Udp String", udpMsg);
            try {
                // TODO Auto-generated method stub
                InetAddress serverAddr = InetAddress.getByName("10.0.0.3");
                //note that 192.168.0.23 is the static IP address we used for the Wi-Fi adapter

                //InetAddress serverAddr = InetAddress.getByName("192.168.7.2"); //Address of the BeagleBone

                DatagramSocket socket = new DatagramSocket();
                sendData = udpMsg.getBytes();
                //There is also an error with sending data through the DatagramPacket


                Log.d("IP", serverAddr.toString()); //This is printing out as /192.168.7.2

                Log.d("Test0", "Before new DatagramPacket\n");
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverAddr, 45786);
                Log.d("Test1.5", "After the new DatagramPacket\n");

                socket.send(packet); //This is where the exception is caught. Not sure why yet

                Log.d("Test1", "The program is in the runUDP\n");
                //dataSent.setText("Data has been sent to the server!");
            }
            catch (Exception e)
            {
                Log.d("Test5", "Basic Exception", e);
            }






           // new Thread(new Client(azimut, "X")).start();
           // new Thread(new Client(pitch, "Y")).start();
           // new Thread(new Client(roll, "Z")).start();
            /*
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } */
            return azimut;
        }


    }

}

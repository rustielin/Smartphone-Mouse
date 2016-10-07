package com.example.rustielin.chaoticevil;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener{

    // public String IP_ADDR;
    public Button leftClick;
    public Button rightClick;
    public Button stop;
    public Button send;

    public EditText ipAddress;
    public EditText port;
    public EditText message;

    public TextView x_view;
    public TextView y_view;
    public TextView z_view;

    //    from client send
    public String IP = "10.142.15.19";
    public int PORT = 5006;
    public String MESSAGE = "HTC One m8 CONNECTION ESTABLISHED";
    public DatagramSocket udpSocket;

    // so that we can spam loop
    public AsyncTask sending;

    public boolean broadcast = true;
    public double TIME_MILLIS;

    // just some sensors
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    public float X_ACCEL = 0;
    public float Y_ACCEL = 0;
    public float Z_ACCEL = 0;

    public float[] DATA;

    public boolean SENSE_CHANGE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hard coding

        x_view = (TextView) findViewById(R.id.X);
        y_view = (TextView) findViewById(R.id.X);
        x_view = (TextView) findViewById(R.id.X);

//        x_view.setText("" + X_ACCEL);
//        y_view.setText("" + Y_ACCEL);
//        z_view.setText("" + Z_ACCEL);


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);


        leftClick = (Button) findViewById(R.id.left_click);
        rightClick = (Button) findViewById(R.id.right_click);
        stop = (Button) findViewById(R.id.stop);
        send = (Button) findViewById(R.id.send);




        // user inputs
        ipAddress = (EditText) findViewById(R.id.ip);
        port = (EditText) findViewById(R.id.port);
        message = (EditText) findViewById(R.id.message);


        // set up sensor
        senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Make the listeners
        leftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button", "Left!!!!!!!!");
                MESSAGE = "This is a left click!";
            }
        });

        rightClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button", "Right!!!!!!!!");
                MESSAGE = "This is a right click!";
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                broadcast = true;

                //bindings
                IP = ipAddress.getText().toString();
                PORT = Integer.parseInt(port.getText().toString());

//                MESSAGE = message.getText().toString();
//                MESSAGE = String.format("%s: %d: %s", IP, PORT, MESSAGE);

                senSensorManager.registerListener(MainActivity.this, senAccelerometer, 1); // lol sample every micro second

                sending = new ClientSend().execute(); // for the asynctask method
//                getApplicationContext().startService(new Intent(getApplicationContext(), SendService.class )); // service method

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MESSAGE = "broadcast stopped";
                sending.cancel(true);
                senSensorManager.unregisterListener(MainActivity.this);
                broadcast = false;

            }
        });




    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        SENSE_CHANGE = true;

        X_ACCEL = sensorEvent.values[0];
        Y_ACCEL = sensorEvent.values[1];
        Z_ACCEL = sensorEvent.values[2];



        Log.d("sensor", "got X:" + X_ACCEL + "  Y:" + Y_ACCEL + "  Z:" + Z_ACCEL);
//        getApplicationContext().startService(new Intent(getApplicationContext(), SendService.class ));

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }


//    public class SendService extends Service {
//
//        private static final String TAG = "SendService";
//
//        private boolean isRunning  = false;
//
//        @Override
//        public void onCreate() {
//            Log.i(TAG, "Service onCreate");
//
//            isRunning = true;
//        }
//
//
//        public int onStartCommand(Intent intent) {
//
//            Log.i(TAG, "Service onStartCommand");
//
//            //Creating new thread for my service
//            //Always write your long running tasks in a separate thread, to avoid ANR
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        // reset and keep connecting
//                        if (udpSocket == null) {
//                            udpSocket = new DatagramSocket(null);
//                            udpSocket.setReuseAddress(true);
//                            udpSocket.setBroadcast(true);
//                            udpSocket.bind(new InetSocketAddress(PORT));
//                        }
//                        //            udpSocket = new DatagramSocket(PORT);
//                        InetAddress serverAddr = InetAddress.getByName(IP);
//
//                        TIME_MILLIS = System.currentTimeMillis();
//
//                        MESSAGE = "T" + (TIME_MILLIS % 1000) + "X" + X_ACCEL + "Y" + Y_ACCEL + "Z" + Z_ACCEL;
//
//
//                        byte[] buf = (MESSAGE).getBytes();
//
//
//                        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, PORT);
//                        udpSocket.send(packet);
//                    } catch (SocketException e) {
//                        Log.e("Udp:", "Socket Error:", e);
//                    } catch (IOException e) {
//                        Log.e("Udp Send:", "IO Error:", e);
//                    }
//
//
//                    //Stop service once it finishes its task
//                    stopSelf();
//                }
//            }).start();
//
//            return Service.START_STICKY;
//        }
//
//        @Nullable
//        @Override
//        public IBinder onBind(Intent intent) {
//            return null;
//        }
//
//        @Override
//        public void onDestroy() {
//
//            isRunning = false;
//
//            Log.i(TAG, "Service onDestroy");
//        }
//    }


    // just keep sending data
    public class ClientSend extends AsyncTask  {

        public byte[] floatToByteArray(float f) {

            byte[] b = ByteBuffer.allocate(4).putFloat(f).array();
            return b;
        }


        @Override
        protected Object doInBackground(Object[] objects) {
            while (broadcast) {
                if (SENSE_CHANGE) {
                    try {
                        // reset and keep connecting
                        if (udpSocket == null) {
                            udpSocket = new DatagramSocket(null);
                            udpSocket.setReuseAddress(true);
                            udpSocket.setBroadcast(true);
                            udpSocket.bind(new InetSocketAddress(PORT));
                        }
                        //            udpSocket = new DatagramSocket(PORT);
                        InetAddress serverAddr = InetAddress.getByName(IP);

                        TIME_MILLIS = System.currentTimeMillis();

                        MESSAGE = "T" + (TIME_MILLIS % 1000) + "X" + X_ACCEL + "Y" + Y_ACCEL + "Z" + Z_ACCEL;

                        //
                        //
                        //                    // make an arraylist of Bytes
                        //                    ArrayList<Byte> byteArrayList = new ArrayList<>();
                        //
                        //                    float time = (float) TIME_MILLIS;
                        //                    for (byte b: floatToByteArray(time))
                        //                        byteArrayList.add(b);
                        //
                        //                    for (byte b: floatToByteArray(X_ACCEL))
                        //                        byteArrayList.add(b);
                        //
                        //                    for (byte b: floatToByteArray(Y_ACCEL))
                        //                        byteArrayList.add(b);
                        //
                        //                    for (byte b: floatToByteArray(Z_ACCEL))
                        //                        byteArrayList.add(b);
                        //
                        //
                        //                    // make array of bytes to send
                        //                    byte[] buf = new byte[byteArrayList.size()];
                        //                    for (int i = 0; i < buf.length; i++)
                        //                        buf[i] = byteArrayList.get(i).byteValue();


                        // REAL STUFF
                        byte[] buf = (MESSAGE).getBytes();


                        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, PORT);
                        udpSocket.send(packet);
                    } catch (SocketException e) {
                        Log.e("Udp:", "Socket Error:", e);
                    } catch (IOException e) {
                        Log.e("Udp Send:", "IO Error:", e);
                    }

                    SENSE_CHANGE = false;
                }
            }

            return null;
        }


    }
}

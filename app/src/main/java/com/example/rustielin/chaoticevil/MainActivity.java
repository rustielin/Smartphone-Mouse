package com.example.rustielin.chaoticevil;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;



public class MainActivity extends AppCompatActivity {

    // public String IP_ADDR;
    public Button leftClick;
    public Button rightClick;

    public EditText ipAddress;
    public EditText port;
    public EditText message;

    //    from client send
    public String IP = "10.142.15.19";
    public int PORT = 5006;
    public String MESSAGE = "HTC One m8 CONNECTION ESTABLISHED";
    public DatagramSocket udpSocket;


    public boolean broadcast = true;
    public double TIME_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);


        leftClick = (Button) findViewById(R.id.left_click);
        rightClick = (Button) findViewById(R.id.right_click);


        // user inputs
        ipAddress = (EditText) findViewById(R.id.ip);
        port = (EditText) findViewById(R.id.port);
        message = (EditText) findViewById(R.id.message);


        new ClientSend().execute();
//        new Thread(new ClientSend()).start();



        // Make the listeners
        leftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button", "Left!!!!!!!!");
                MESSAGE = "This is a left click!";
//                new Thread(new ClientSend()).start();
            }
        });

        rightClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button", "Right!!!!!!!!");
                MESSAGE = "This is a right click!";
//                new Thread(new ClientSend()).start();
            }
        });

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                broadcast = true;

                //bindings
                IP = ipAddress.getText().toString();
                PORT = Integer.parseInt(port.getText().toString());
                MESSAGE = message.getText().toString();


                MESSAGE = String.format("%s: %d: %s", IP, PORT, MESSAGE);

//                new Thread(new ClientSend()).start();

            }
        });

        Button stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MESSAGE = "broadcast stopped";
                broadcast = false;

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ClientSend extends AsyncTask  {
//    public class ClientSend extends AsyncTas implements Runnable{}

//        @Override
//        public void run() {
//            try {
//                // reset and keep connecting
//                if (udpSocket == null) {
//                    udpSocket = new DatagramSocket(null);
//                    udpSocket.setReuseAddress(true);
//                    udpSocket.setBroadcast(true);
//                    udpSocket.bind(new InetSocketAddress(PORT));
//                }
////            udpSocket = new DatagramSocket(PORT);
//                InetAddress serverAddr = InetAddress.getByName(IP);
//
//
//
//                byte[] buf = (MESSAGE).getBytes();
//                DatagramPacket packet = new DatagramPacket(buf, buf.length,serverAddr, PORT);
//                udpSocket.send(packet);
//            } catch (SocketException e) {
//                Log.e("Udp:", "Socket Error:", e);
//            } catch (IOException e) {
//                Log.e("Udp Send:", "IO Error:", e);
//            }
//        }


        @Override
        protected Object doInBackground(Object[] objects) {
            while (broadcast) {
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

                    TIME_MILLIS = (double) System.currentTimeMillis();

                    MESSAGE =""+ TIME_MILLIS%1000;


                    byte[] buf = (MESSAGE).getBytes();


                    DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, PORT);
                    udpSocket.send(packet);
                } catch (SocketException e) {
                    Log.e("Udp:", "Socket Error:", e);
                } catch (IOException e) {
                    Log.e("Udp Send:", "IO Error:", e);
                }

            }

            return null;
        }


    }
}

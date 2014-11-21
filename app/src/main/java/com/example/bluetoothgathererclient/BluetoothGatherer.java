package com.example.bluetoothgathererclient;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.UUID;

//I’ve talked with Christine, and she and I agree that for the initial simple demo app,
//we want to collect geotagged accelerometer data from other phones using peer to peer (bluetooth) connections,
//and display a heat map to the collector (red = area where people are stationary, blue = area where people are rapidly moving).
public class BluetoothGatherer extends Activity {

    private static final int BT_REQUEST_CODE = 15;
    private static final UUID MY_UUID = UUID.randomUUID();
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_gatherer);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //check to see if there is a bluetooth adapter and if it is enabled
        if (mBluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "No Bluetooth Adapter found.", Toast.LENGTH_SHORT);
        }
        else if(!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BT_REQUEST_CODE);
        }

        //Enable discoverability on the phone so other phones can connect to this one
        //and give their id when they are trying to discover other devices


        //BluetoothDevice.getUuids();


        //enable other bluetooth devices (i.e. other user's smartphones) to discover this device
        //in order to count the different devices
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);

        //create a listener and do the work on another thread
        AcceptThread thread = new AcceptThread();

        while(true){

        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bluetooth_gatherer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class AcceptThread extends Thread {
        private BluetoothServerSocket mBluetoothSSocket;
        private BluetoothSocket mBluetoothSocket;
        private BufferedReader fromClient;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final

            try {
                mBluetoothSSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("", MY_UUID);
                mBluetoothSocket = mBluetoothSSocket.accept();
                fromClient = new BufferedReader(new InputStreamReader(mBluetoothSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            //BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    manageConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        private void manageConnectedSocket(BluetoothSocket socket) {

        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }
}

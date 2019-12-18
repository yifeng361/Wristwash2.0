package com.example.wristwash20;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.renderscript.ScriptGroup;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;



public class BluetoothService {
    private static final String TAG = "BluetoothService";

    private final int READ_BUFFER_SIZE = 35;//

    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    private static final String NAME_INSECURE = "BluetoothChatInsecure";
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private  BluetoothAdapter mAdapter;
    private int mState;

    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;
    private BluetoothDevice mPairedDevice;
    private String myDeviceName;
    private String myAddress;

    InputStream mInputStream = null;
    OutputStream mOutputStream = null;

    private ReadThread mReadThread;
    private WriteThread mWriteThread;

    byte [] mReadBuffer;

    private Context mContext;

    //private boolean isBtActive = false;

    public BluetoothService(Context context){
        mContext = context;
    }

    public void stopConnection(){
        mReadThread.interrupt();
        mWriteThread.interrupt();
        mReadThread = null;
        mWriteThread = null;
        try{
            if(mSocket.isConnected()){
                mSocket.close();
            }
        }catch (Exception e){
            int x = 1;
        }

    }

    public void buildConnection(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        myDeviceName = mAdapter.getName();
        myAddress = mAdapter.getAddress();
        mPairedDevice = (BluetoothDevice) mAdapter.getBondedDevices().toArray()[0];
        mReadThread = new ReadThread();
        mWriteThread = new WriteThread();
        mWriteThread.start();
    }

    public void BtConnect(){
        try {
            mSocket = mPairedDevice.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
            mSocket.connect();
        } catch (IOException e) {
            try {
                mSocket = (BluetoothSocket) mPairedDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mPairedDevice, 1);
                mSocket.connect();
            } catch (Exception e2) {
                Log.e("", "Couldn't establish Bluetooth connection!");
            }
        }

        if(mSocket.isConnected()){
            try{
                mInputStream = mSocket.getInputStream();
                mOutputStream = mSocket.getOutputStream();
                if(!mReadThread.isAlive()){
                    mReadThread.start();
                }else{
                    mReadThread.run();
                }

            }catch (IOException e){
                Log.e("","Exception of .. after connected");
                e.printStackTrace();
            }
        } else {
            SystemClock.sleep(2000);
            try{
                mSocket.close();
            }catch (IOException e){
                Log.e("", "Closing failed.");
            }
        }
        //}

    }

    public boolean isConnectionActive(){
        return (mSocket != null) && (mSocket.isConnected());
    }

    private class WriteThread extends Thread{
        public void run(){
            while((mSocket == null || !mSocket.isConnected()) && !Thread.currentThread().isInterrupted()){
                BtConnect();
            }
        }
    }

    private class ReadThread extends Thread{
        byte [] tmp = new byte [READ_BUFFER_SIZE];
        public void run(){
            int ret = -1;
            while(!Thread.currentThread().isInterrupted() && mSocket.isConnected()){
                try{
                    ret = mInputStream.read(tmp);
                    if(ret != -1){
                        //data store
                        String s = "";
                        s += new String(tmp);
                        String [] info = s.split("@@@");
                        String [] infoList =  info[1].split(",");
                        int correctOrNot = Integer.valueOf(infoList[0]);
                        int step = Integer.valueOf(infoList[1]);

                        Intent retIntent = new Intent();
                        retIntent.setAction("retFromBluetooth");
                        retIntent.putExtra("retvalue", correctOrNot);
                        mContext.sendBroadcast(retIntent);
                        Log.d(TAG, "Ret value comes " + correctOrNot);
                    }
                }catch (IOException e){
                   try{
                       mSocket.close();
                       int x = 1;
                   }catch (IOException e2) {

                       Log.d(TAG, "Close error.");
                   }
                }
            }
        }
    }

    public void write(byte [] writeData){
        try{
            mOutputStream.write(writeData);
        }catch(IOException e){
            Log.d(TAG, "Failed to write into outputstream.");
        }

    }
}

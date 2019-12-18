package com.example.wristwashsmartphone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN_ACTIVITY";

    BluetoothService mBS;
    ArrayList<Button> buttonList = new ArrayList<>();
    ImageButton mReceiveButton;
    ImageButton mStopButton;
    BluetoothThread btThread;
    private boolean threadStartFlag = false;

    private int mode;
    private String filename = "";

    FileOutputStream outputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mode = Constants.CURMODE;

        init_ui();

        mReceiveButton = (ImageButton)findViewById(R.id.receive);
        mStopButton = (ImageButton) findViewById(R.id.stop);


        mReceiveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                mReceiveButton.setBackgroundColor(0xFF336600);
                mStopButton.setBackgroundColor(0xFFE0E0E0);
                btThread = new BluetoothThread();
                btThread.start();
                threadStartFlag = true;

            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReceiveButton.setBackgroundColor(0xFFE0E0E0);
                mStopButton.setBackgroundColor(0xFF336600);
                try{
                    btThread.stopService();
                    btThread = null;//Java will recycle the resources automatically
                }catch (Exception e){
                    Log.e(TAG, "Stop failed");
                }

            }
        });
    }

    private void init_ui(){
        if (mode == Constants.DATA_COLLECTION_MODE){
            setClickListenerToAll();
        }

    }

    private void setClickListenerToAll(){
        buttonList.add((Button) findViewById(R.id.step1));
        buttonList.add((Button) findViewById(R.id.step2));
        buttonList.add((Button) findViewById(R.id.step3));
        buttonList.add((Button) findViewById(R.id.step4));
        buttonList.add((Button) findViewById(R.id.step5));
        buttonList.add((Button) findViewById(R.id.step6));
        buttonList.add((Button) findViewById(R.id.step7));
        buttonList.add((Button) findViewById(R.id.step8));
        buttonList.add((Button) findViewById(R.id.step9));
        buttonList.add((Button) findViewById(R.id.step10));
        buttonList.add((Button) findViewById(R.id.step11));
        buttonList.add((Button) findViewById(R.id.step12));
        buttonList.add((Button) findViewById(R.id.step13));
        buttonList.add((Button) findViewById(R.id.step14));


        for(int i = 0; i < Constants.STATE_SPACE; i++){
            buttonList.get(i).setOnClickListener(new ClickSaveFileListener(i));
        }

    }

    public class ClickSaveFileListener implements  View.OnClickListener{
        int fileIdx;

        public ClickSaveFileListener(int fileidx){
            fileIdx = fileidx;
        }

        @Override
        public void onClick(View v) {
            for(int j = 0; j < Constants.STATE_SPACE; j++){
                if(fileIdx == j){
                    buttonList.get(j).setBackgroundColor(0xFF336600);
                    filename = Constants.SAVED_FILE_PREFIX+Constants.SAVED_FILE[fileIdx];
                }else{
                    buttonList.get(j).setBackgroundColor(0xFFE0E0E0);
                }
            }
        }
    }



    private class BluetoothThread extends Thread{

        public BluetoothThread(){
            mBS = new BluetoothService(MainActivity.this);
        }

        @Override
        public void run(){
            mBS.buildConnection(filename);
        }

        public void stopService(){
            mBS.stopListening();
        }
    }


}


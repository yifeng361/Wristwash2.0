package com.example.wristwash20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SensorService mSS;
    private static String MAIN_AC="mainActivity";
    Intent startServiceIntent;
     Button mSendButton;
     Button mStopButton;
     TextView mResutView;
     LinearLayout mShowlayout;

    IntentFilter mIntentFilter;

    private BroadcastReceiver receiver;
    Vibrator mVibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSendButton = (Button) findViewById(R.id.send);
        mStopButton = (Button) findViewById(R.id.stop);
        mResutView = (TextView) findViewById(R.id.result);
        mShowlayout  = (LinearLayout) findViewById(R.id.show_layout);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(500);

        receiver =  new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                int valueFromService = -1;
                if(intent.getAction().equals("retFromBluetooth")){
                    valueFromService = intent.getIntExtra("retvalue", 0);
                    if(Constants.OPEN_VIBRATION){
                        if(valueFromService == 1){
                            mVibrator.vibrate(500);
                            setVisionFeedback(true);
                        }else{
                            long [] duration = {0,100, 100,100};
                            mVibrator.vibrate(duration, -1);
                            setVisionFeedback(false);
                        }
                    }
                    //mResutView.setText(Integer.toString(valueFromService));

                }

            }
        };
        mIntentFilter =  new IntentFilter();
        mIntentFilter.addAction("retFromBluetooth");




        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                mSendButton.setBackgroundColor(0xFF336600);
                mStopButton.setBackgroundColor(0xFFE0E0E0);
                mVibrator.vibrate(200);
                startServiceIntent = new Intent(MainActivity.super.getApplicationContext(), SensorService.class);
                startService(startServiceIntent);
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                mStopButton.setBackgroundColor(0xFF336600);
                mSendButton.setBackgroundColor(0xFFE0E0E0);
                long [] duration = {0,100, 100,100};
                mVibrator.vibrate(duration, -1);
                try{
                    stopService(startServiceIntent);
                }catch (Exception e){
                    Log.e(MAIN_AC, "No active intent");
                }

            }
        });
    }

    protected void onResume(){
        super.onResume();

        registerReceiver(receiver,mIntentFilter);
    }

    protected void onPause (){
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void setVisionFeedback(boolean correct){
        if(correct){
            mShowlayout.setBackgroundColor(0xFF66CC00);
            mResutView.setText("Checked!");
            mResutView.setTypeface(null, Typeface.BOLD);
        }else{
            mShowlayout.setBackgroundColor(0xFFCC0000);
            mResutView.setText("Wrong!");

        }
    }
}
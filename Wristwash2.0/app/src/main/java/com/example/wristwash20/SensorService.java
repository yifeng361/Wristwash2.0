package com.example.wristwash20;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class SensorService extends Service {
    private SensorManager mSensorManager;

    private Sensor mAcceleration;

    private Sensor mGyroscope;

    private Sensor mGravitySensor;

    private static final String TAG = SensorService.class.getName();

    public final int SENSOR_BUFFER_SIZE = Constants.SENSOR_BUFFER_SIZE + 5;

    public final int SENSOR_SAMPLE_DURATION = 10000; //micro-sec

    private int accIdx = 0;

    private int gyIdx = 0;

    private int grIdx = 0;

    private float [] accData = new float [SENSOR_BUFFER_SIZE*3];

    private float [] gyData = new float [SENSOR_BUFFER_SIZE*3];;

    private float [] grData = new float [SENSOR_BUFFER_SIZE*3];;

    private TextView mResTextView;

    BluetoothService mBS;

    Context currentContext = this;

    private int nextsensor = 1;

    private SensorEventListener mAccListener;

    private SensorEventListener mGyListener;

    private Thread mBtThread;

    @Override
    public void onCreate(){
        super.onCreate();
        initialize();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        mSensorManager.unregisterListener(mAccListener);
        mSensorManager.unregisterListener(mGyListener);
        mBtThread.interrupt();
        mBS.stopConnection();
        accIdx = 0;
        gyIdx = 0;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        registerSensors();
        return START_STICKY;
    }


    public void initialize(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope= mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mBS = new BluetoothService(currentContext);
        mBS.buildConnection();
        mAccListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && accIdx < Constants.SENSOR_BUFFER_SIZE) {
                    accData[accIdx * 3] = event.values[0];
                    accData[accIdx * 3 + 1] = event.values[1];
                    accData[accIdx * 3 + 2] = event.values[2];
                    nextsensor = 0;
                    accIdx++;
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        };
        mGyListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE && gyIdx < Constants.SENSOR_BUFFER_SIZE) {
                    gyData[gyIdx * 3] = event.values[0];
                    gyData[gyIdx * 3 + 1] = event.values[1];
                    gyData[gyIdx * 3 + 2] = event.values[2];
                    nextsensor = 0;
                    gyIdx++;
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        };
        mBtThread = new BluetoothThread();
        if(!mBtThread.isAlive()){
            mBtThread.start();
        }else{
            mBtThread.run();
        }
    }

    private void registerSensors(){
        mSensorManager.registerListener(mAccListener, mAcceleration, SENSOR_SAMPLE_DURATION);
        mSensorManager.registerListener(mGyListener, mGyroscope, SENSOR_SAMPLE_DURATION);
    }

    private class BluetoothThread extends Thread{
        public void run(){
            while(!Thread.currentThread().isInterrupted()){
                if(accIdx == Constants.SENSOR_BUFFER_SIZE && gyIdx == Constants.SENSOR_BUFFER_SIZE){
                    String msg = "PREAMBLE#";
                    //msg += "acc ";
                    //String [] axis = {"x","y","z"};
                    for(int i = 0; i < Math.min(accIdx, gyIdx); i++) {
                        msg += "(";
                        for(int j = 0; j <  3; j++){
                            double val = (int)(100*accData[i*Constants.SENSOR_DIMENSION/Constants.SENSOR_TYPE_NUM + j])/100.0;
                            msg += Double.toString(val) + ",";
                        }
                        for(int j = 0; j <  3; j++){
                            double val = (int)(100*gyData[i*Constants.SENSOR_DIMENSION/Constants.SENSOR_TYPE_NUM + j])/100.0;
                            if(j == 2){
                                msg += Double.toString(val);
                            }else {
                                msg += Double.toString(val) + ",";
                            }
                        }
                        msg += ")";
                    }
                    msg += "#END";

                    byte [] tosend = msg.getBytes();
                    if(mBS.isConnectionActive()){
                        mBS.write(tosend);
                    }
                    accIdx = 0;
                    gyIdx = 0;
                }
            }
        }
    }

}

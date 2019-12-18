package com.example.wristwashsmartphone;

import android.app.Activity;
import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.logging.LogRecord;


public class BluetoothService {
    private static final String TAG = "BluetoothService";

    private String mSavedFile;

    private Activity mActivity;

    private boolean threadStartFlag = false;

    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    public static final int SENSOR_TYPE_ACC = 0;
    public static final int SENSOR_TYPE_GY = 1;
    public static final int SENSOR_TYPE_GR = 2;

    public final int MAX_SENSOR_DATA_SIZE = Constants.MAX_SENSOR_DATA_SIZE;//the maximal data stored in the array
    public final int WINDOW_SIZE = Constants.WINDOW_SIZE;// How many data do we use for calculate features
    public final int STEP_SIZE = Constants.STEP_SIZE;
    public final int FEATURE_SIZE = Constants.FEATURE_SIZE;
    public final int OUTPUT_DIMENSION = Constants.OUTPUT_DIMENSION;


    private static final String NAME_INSECURE = "BluetoothServer";
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter mAdapter;
    private int mState;

    private BluetoothServerSocket mServerSocket;
    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;
    private BluetoothDevice mPairedDevice;
    private String myDeviceName;
    private String myAddress;

    Semaphore lockOfThread = new Semaphore(1);


    InputStream mInputStream = null;
    OutputStream mOutputStream = null;

    public final int READ_BUFFER_SIZE = 1024;

    byte [] mReadBuffer;


    float [][] accData = new float[MAX_SENSOR_DATA_SIZE][3];
    float [][] gyData = new float[MAX_SENSOR_DATA_SIZE][3];
    float [][] grData = new float[MAX_SENSOR_DATA_SIZE][3];

    private int accIdx = 0;
    private int gyIdx = 0;
    private int grIdx = 0;

    private int dataType;

    private Boolean writeFlag = new Boolean(false);

    WriteThread mWriteThread;
    RecognitionThread mRecognitionThread;
    ReadThread mReadThread;

    FileOutputStream stream;

    private String bufferOfLastMessage = "";

    private UserWarningService mUserWarningService;

    private int lastGesture = 0;

    private boolean stateOfGesture = true ; // whether you are doing correctly for the last step
    private int failureTimes = 0;//How many successive recognition are failed

    public BluetoothService(Activity activity){
        mActivity = activity;

        mUserWarningService = new UserWarningService(mActivity.getApplicationContext());


/*
        float [][] testdata ={{1},{2},{6},{4},{5},{6},{7},{8},{9},{10}};
        float f1 = Mean(testdata, 0,10);
        float f2 = Var(testdata, 0,10);
        float f3 = Rms(testdata, 0,10);
        float f4 = Skew(testdata, 0,10);
        int x = 1;

 */
    }


    public void setSavedFile(String s){
        String mSavedFile = s;
        try{
            File path = mActivity.getExternalFilesDir(null);
            File file = new File(path, mSavedFile);
            stream = new FileOutputStream(file);

        }catch (Exception e){
            Log.d(TAG,"Open file failed.");
        }
    }

    public void buildConnection(String filename){
        try {
            setSavedFile(filename);
            mAdapter = BluetoothAdapter.getDefaultAdapter();
            myDeviceName = mAdapter.getName();
            myAddress = mAdapter.getAddress();
            mPairedDevice = (BluetoothDevice) mAdapter.getBondedDevices().toArray()[0];
            Thread.sleep(500);
            mUserWarningService.playSpeech("Start");

            mServerSocket = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
                    NAME_INSECURE, MY_UUID_INSECURE);
            mSocket = mServerSocket.accept();

            //mSocket.connect();
            mInputStream = mSocket.getInputStream();
            mOutputStream = mSocket.getOutputStream();
            //open a write thread
            mWriteThread = new WriteThread(mOutputStream);
            mReadThread = new ReadThread();
            mRecognitionThread = new RecognitionThread();
            mReadThread.start();
            mWriteThread.start();
            mRecognitionThread.start();

            threadStartFlag = true;
            mUserWarningService.playSpeech("Bluetooth connected");
            waitForPlayout(1000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d(TAG,"socket not created");
            e.printStackTrace();
        }
    }

    public void waitForPlayout(int time){
        try{
            Thread.sleep(time);
        }catch (Exception e){

        }
    }


    public void stopListening(){
        lastGesture = 0;
        stateOfGesture = true ;
        failureTimes = 0;
        if(mReadThread != null){
            mReadThread.stopReading();
            mReadThread.interrupt();
            mReadThread = null;
        }
        if(mWriteThread != null){
            mWriteThread.stopWriting();
            mWriteThread = null;
        }
        if(mRecognitionThread != null){
            try{
                lockOfThread.acquire();
                mRecognitionThread.stopRecognizing();
                mRecognitionThread = null;
                lockOfThread.release();
            }catch (Exception e) {

            }
        }
        try{
            stream.close();
        }catch (Exception e){
            Log.e(TAG, "Stream Close Failed");
        }
        try{
            mServerSocket.close();
        }catch (Exception e){
            Log.d(TAG, "Close Failed");
        }
        int x = 1;
    }

    private class ReadThread extends Thread{
        @Override
        public void run(){
            readData();
        }

        public void readData(){
            mReadBuffer = new byte[READ_BUFFER_SIZE];
            while(mSocket != null && mSocket.isConnected() && !Thread.currentThread().isInterrupted()){
                try{

                    int ret = mInputStream.read(mReadBuffer);
                    if(ret != -1){
                        /**
                         * Send the data to Recog Thread in the form of message through handler
                         * */

                        while(!Thread.currentThread().isInterrupted() && mRecognitionThread.recogHandler == null);
                        if(mRecognitionThread != null){
                            try{
                                lockOfThread.acquire();
                                Message recogMsg = mRecognitionThread.recogHandler.obtainMessage();
                                Message msg = new Message();
                                msg.obj = new String(mReadBuffer);
                                mRecognitionThread.recogHandler.sendMessage(msg);
                                lockOfThread.release();
                            }catch (Exception e){
                                int y = 1;
                            }
                        }
                        //Log.d(TAG, "Message received: " + new String(mReadBuffer));
                    }
                }catch (IOException e){
                    Log.d(TAG,"Read failed");
                }
            }
            int y = 1;
            //mRecognitionThread.stopRecogniz();
        }

        public void stopReading(){
            //Looper.myLooper().quit();
        }
    }

    private class WriteThread extends Thread{
        public Handler writeHandler;
        public OutputStream tmpOut;

        public WriteThread(OutputStream out){
            tmpOut = out;
        }

        @Override
        public void run(){
            Looper.prepare();
            writeHandler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    write(((String)msg.obj).getBytes());
                }
            };
            Looper.loop();
        }

        public void write(byte [] writeData){
            try{
                tmpOut.write(writeData);
            }catch(IOException e){
                Log.d(TAG, "Failed to write into outputstream.");
            }
        }

        public void stopWriting(){
            //Looper.myLooper().quit();
        }
    }

    private class RecognitionThread extends Thread{
        public Handler recogHandler;
        private GestureRecognizer recognizer;

        public RecognitionThread(){
            recognizer = new GestureRecognizer(mActivity);
            recognizer.setRecognitionState();
        }


        public void splitPacket(String dataStr){
            String testLastMst = bufferOfLastMessage;
            String curMsg = bufferOfLastMessage + dataStr;
            String tmpMsg = "", remainMsg = "";
            int preambleIdx = curMsg.indexOf(Constants.PREAMBLE);
            int postambleIdx = curMsg.indexOf(Constants.POSTAMBLE);
            int nextPreambleIdx;
            if(preambleIdx != -1 && postambleIdx != -1){
                if(preambleIdx < postambleIdx){
                    try{
                        tmpMsg = curMsg;
                        curMsg = tmpMsg.substring(preambleIdx+Constants.PREAMBLE.length(), postambleIdx);
                        remainMsg = tmpMsg.substring(postambleIdx+Constants.POSTAMBLE.length());
                        nextPreambleIdx = remainMsg.indexOf(Constants.PREAMBLE);
                        if(nextPreambleIdx != -1){
                            bufferOfLastMessage = remainMsg.substring(preambleIdx);
                        }else{
                            bufferOfLastMessage = "";
                        }

                        splitData(curMsg);
                    }catch (Exception e){
                        int x = 1;
                    }
                }else{
                    bufferOfLastMessage = curMsg.substring(preambleIdx);
                }
            }else if(preambleIdx != -1 && postambleIdx == -1){
                bufferOfLastMessage = curMsg;
            }else if(preambleIdx == -1 && postambleIdx == 1){
                bufferOfLastMessage = curMsg.substring(postambleIdx+Constants.POSTAMBLE.length());
            }else{
                bufferOfLastMessage = curMsg;
            }
        }

        public void splitData(String dataStr){
            float [] accMean = {0,0,0};
            float [] accVar = {1,1,1};
            float [] gyMean = {0,0,0};
            float [] gyVar = {1,1,1};

            List<String> sensorDataList = StringUtil.extractMessageByRegular(dataStr);
            int x = 1;
            for(int i = 0; i < sensorDataList.size(); i++){
                String[] sensorData = sensorDataList.get(i).split(",",0);
                if(sensorData.length == Constants.SENSOR_DIMENSION){
                    for(int j = 0; j < Constants.SENSOR_DIRECTION_NUM; j++){
                        float val;
                        try{
                            val = Float.valueOf(sensorData[j]);
                        }catch (Exception e){
                            val = 65536;
                        }
                        accData[accIdx][j] = (val - accMean[j]) / (float) Math.sqrt((double) accVar[j]);
                    }
                    for(int j = 0; j < Constants.SENSOR_DIMENSION / Constants.SENSOR_TYPE_NUM; j++){
                        float val;
                        try{
                            val = Float.valueOf(sensorData[j+Constants.SENSOR_DIRECTION_NUM]);
                        }catch (Exception e){
                            val = 65536;
                        }
                        gyData[gyIdx][j] = (val - gyMean[j]) / (float) Math.sqrt((double) gyVar[j]);
                    }
                    accIdx++; gyIdx++;
                    if(accIdx >= Constants.STEP_SIZE && gyIdx >= STEP_SIZE){
                        process();
                        accIdx = 0;
                        gyIdx = 0;
                    }
                }
            }


        }
//preprocess + let data come into your model

        public void process(){

            for(int i = 0; i < STEP_SIZE; i++){
                String tmp = "" + accData[i][0] + "," + accData[i][1] + ","+accData[i][2] +
                        "," + gyData[i][0] + "," + gyData[i][1] + "," + gyData[i][2] + "\n";
                try{
                    stream.write(tmp.getBytes());
                }catch(Exception e){
                    //Log.d(TAG,"!!!");
                }

            }

            /**
             * currently we don't apply zero-mean, unit-variance operation
             * to the data
             */
            // only accelerator's data is using, currently 200 * 3 floats
            // parse the 1D-array to 2D

            if(recognizer.isInRecognitionState() &&
                    recognizer.getGlobalRecognitionTimes() >= Constants.MAX_GLOBAL_RECOGNITIOME_TIMES){
                recognizer.resetRecognitionState();
            }
            if(recognizer.isInRecognitionState()){
                recognizer.removeOutliers(accData);
                recognizer.removeOutliers(gyData);

                double [] feature = new double [FEATURE_SIZE];
                recognizer.extractFeatures(feature, accData, gyData);

                int result = recognizer.runModel(feature);
                String ret = "@@@";
                if(result != Constants.UNDEF_RET){
                    if(result == Constants.FINISH_RET){
                        mUserWarningService.playSpeech("Step" + Integer.toString(recognizer.getLastGesture()) + Constants.RIGHT_GESTURE_INFO);
                        waitForPlayout(2000);
                        mUserWarningService.playSpeech(Constants.FINISH_INFO);
                        waitForPlayout(1);
                        recognizer.resetLastGesutre();
                        recognizer.resetRecognitionState();
                        ret += "1";
                    }else if(result == Constants.RIGHT_RET){
                        mUserWarningService.playSpeech("Step" + Integer.toString(recognizer.getLastGesture()) + Constants.RIGHT_GESTURE_INFO);
                        waitForPlayout(2000);
                        mUserWarningService.playSpeech("Now please do Step " + Integer.toString(recognizer.getExpectedGesture()));
                        waitForPlayout(1);
                        ret += "1";
                    }else if(result == Constants.ERROR_RET){
                        mUserWarningService.playSpeech(Constants.WRONG_GESTURE_WARNING + recognizer.getExpectedGesture());
                        waitForPlayout(1);
                        ret += "0";
                    }else if(result == Constants.ERROR_AND_FORWARD_RET){
                        mUserWarningService.playSpeech(Constants.WRONG_GESTURE_WARNING + recognizer.getLastGesture());
                        waitForPlayout(1);
                        if(recognizer.getExpectedGesture() <= Constants.STATE_SPACE){
                            mUserWarningService.playSpeech(
                                    "Step " + recognizer.getLastGesture() + "missing " +
                                            ". Now please do Step" + Integer.toString(recognizer.getExpectedGesture()));
                            waitForPlayout(1);
                        }else{
                            recognizer.resetRecognitionState();
                            mUserWarningService.playSpeech("Recognition finished.");
                            waitForPlayout(1);
                        }


                        ret += "0";
                    }
                    ret += "," + Integer.toString(result) + "@@@";

                    Log.d(TAG, ret);
                    Message msg = new Message();
                    msg.obj = ret;
                    mWriteThread.writeHandler.sendMessage(msg);
                }
            }

        }

        public void run(){
            Looper.prepare();
            recogHandler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    splitPacket((String)msg.obj);
                }
            };
            Looper.loop();
        }

        public void stopRecognizing(){
            recognizer.reset();
           // Looper.myLooper().quit();
        }


    }


}

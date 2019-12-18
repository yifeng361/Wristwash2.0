package com.example.wristwashsmartphone;

import android.app.Activity;
import android.nfc.Tag;
import android.util.Log;
import android.view.Display;

import com.example.wristwashsmartphone.model.RandomForestClassifier;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GestureRecognizer {
    private static final String TAG = "GESUTRE_RECOGNIZER";

    private Interpreter interpreter;

    // Set the frame and step size
    private final int WINDOW_SIZE = Constants.WINDOW_SIZE;
    private final int STEP_SIZE = Constants.STEP_SIZE;

    private Activity mActivity;

    /**
     * The current window consists of n/2 samples from the last recognition, and n/2 samples from the current recognition
     * */
    private float [][] accCurData;
    private float [][] gyCurData;

    private int lastGesture = 0;
    private int expectedGesture = 1;
    private int [] gestureCache = new int [Constants.RECOGNITION_DURATION];
    private int cacheSize = 0;
    private int errorTimes = 0;
    private int globalRecognitionTimes = 0;

    private int [] stateMachine;
    private int  stateMachineIdx = 0;
    private HashMap<Integer, Integer> gesture_count_bucket;
    private RandomForestClassifier rfc;

    private boolean inRecognitionState = false;
    private ModelSelectionLayer msl;

    public GestureRecognizer(Activity activity){
        mActivity  = activity;
        rfc = new RandomForestClassifier();
        accCurData = new float[WINDOW_SIZE][Constants.SENSOR_DIRECTION_NUM];
        gyCurData = new float[WINDOW_SIZE][Constants.SENSOR_DIRECTION_NUM];
        stateMachine = new int[Constants.STATE_MACHINE_SIZE];
        gesture_count_bucket = new HashMap<>();
        gesture_count_bucket.put(-1, Constants.STATE_MACHINE_SIZE);
        msl = new ModelSelectionLayer();
        for(int i = 0; i < Constants.STATE_MACHINE_SIZE; i++){
            stateMachine[i] = -1;
        }
        try{
            classifier_class classifier= new classifier_class(Constants.MODEL_NAME, mActivity);
            interpreter = classifier.getTflite();
        }catch(IOException e){
            System.out.println("model setup failed!!");
        }
        lastGesture = 0;
        expectedGesture = 1;
    }

    private boolean initFlag = false;

    public void removeOutliers(float [][] data){
        for(int i = 0; i < STEP_SIZE; i++){
            for(int j = 0; j < 3; j++){
                if(Math.abs(data[i][j] - Constants.errorVal) < 1){
                    if(i > 0){
                        data[i][j] = data[i-1][j];
                    }else{
                        if(i < STEP_SIZE - 1){
                            data[i][j] = data[i][j+1];
                        }
                    }
                }
            }
        }
        int x = 1;
    }

    public void setRecognitionState(){
        inRecognitionState = true;
    }

    public void resetRecognitionState(){
        inRecognitionState = false;
    }

    public boolean isInRecognitionState(){
        return inRecognitionState;
    }

    public int getGlobalRecognitionTimes(){
        return globalRecognitionTimes;
    }

    public int getExpectedGesture(){
        return expectedGesture;
    }

    public void extractFeatures(double [] feature, float [][] accData, float[][] gyData){
        for(int i = STEP_SIZE; i < WINDOW_SIZE; i++){
            for(int j = 0; j < Constants.SENSOR_DIRECTION_NUM; j++){
                accCurData[i][j] = (float)accData[i-STEP_SIZE][j];
            }
            for(int j = 0; j < Constants.SENSOR_DIRECTION_NUM; j++){
                gyCurData[i][j] = (float)gyData[i-STEP_SIZE][j];
            }
        }


        /** Format: Mean(acc_x), Mean(acc_y), Mean(acc_z), Mean(gy_x), Mean(gy_y), Mean(gy_z), Var(acc_x) ...
         *
         * */
        if(initFlag){
            for(int i=0;i < Constants.FEATURE_SIZE;i++){
                switch(i/Constants.SENSOR_DIMENSION){
                    case 0:
                        if(i % Constants.SENSOR_DIMENSION < 3){
                            feature[i]=Mean(accCurData,i % Constants.SENSOR_DIRECTION_NUM, WINDOW_SIZE);
                        }else{
                            feature[i]=Mean(gyCurData,i % Constants.SENSOR_DIRECTION_NUM, WINDOW_SIZE);
                        }
                        break;
                    case 1:
                        if(i % Constants.SENSOR_DIMENSION < 3){
                            feature[i]=Var(accCurData,i % Constants.SENSOR_DIRECTION_NUM, WINDOW_SIZE);
                        }else{
                            feature[i]=Var(gyCurData,i % Constants.SENSOR_DIRECTION_NUM, WINDOW_SIZE);
                        }
                        break;
                    case 2:
                        //feature[0][i]=Skew(dataForFeature,i%3,SENSOR_DATA_SIZE);
                        if(i % Constants.SENSOR_DIMENSION < 3){
                            feature[i]=Skew(accCurData,i % Constants.SENSOR_DIRECTION_NUM, WINDOW_SIZE);
                        }else{
                            feature[i]=Skew(gyCurData,i % Constants.SENSOR_DIRECTION_NUM, WINDOW_SIZE);
                        }
                        break;
                    case 3:
                        //feature[0][i]=Kurtosis(dataForFeature,i%3);
                        if(i % Constants.SENSOR_DIMENSION < 3){
                            feature[i]=Kurtosis(accCurData,i % Constants.SENSOR_DIRECTION_NUM, WINDOW_SIZE);
                        }else{
                            feature[i]=Kurtosis(gyCurData,i % Constants.SENSOR_DIRECTION_NUM, WINDOW_SIZE);
                        }
                        break;
                    case 4:
                        //feature[0][i]=Rms(dataForFeature,i%3,SENSOR_DATA_SIZE);
                        if(i % Constants.SENSOR_DIMENSION < 3){
                            feature[i]=Rms(accCurData,i % Constants.SENSOR_DIRECTION_NUM, WINDOW_SIZE);
                        }else{
                            feature[i]=Rms(gyCurData,i % Constants.SENSOR_DIRECTION_NUM, WINDOW_SIZE);
                        }
                        break;
                    default:
                        System.out.println("wrong operation occurred!");
                        break;
                }
            }
        }

        /**
         * Move the right half window to the left
         * */
        for(int i = 0; i < STEP_SIZE; i++){
            for(int j = 0; j < 3; j++){
                accCurData[i][j] = accCurData[i+STEP_SIZE][j];
                gyCurData[i][j] = gyCurData[i+STEP_SIZE][j];
            }

        }

        initFlag = true;
    }

    public int runModel(double [] feature){
        int recognition_result = 0;
        recognition_result = msl.predict(expectedGesture, feature); // 0 or 1
        String feature_info = "";
        for(int i = 0; i < feature.length; i++){
            feature_info += Double.toString(feature[i]) + ",";
        }
        Log.e(TAG, feature_info);
        /**
         * state machine stores successive k recognition results
         *
         */

        stateMachine[stateMachineIdx] = recognition_result;
        stateMachineIdx++;
        if(stateMachineIdx == Constants.STATE_MACHINE_SIZE){
            stateMachineIdx = 0;
            int final_recognition = synthesizeStateMachine();
            gestureCache[cacheSize] = final_recognition;
            cacheSize++;
            int retCode;
            if(cacheSize == Constants.RECOGNITION_DURATION){
                globalRecognitionTimes++;
                cacheSize = 0;
                retCode = evaluateResult();
                errorTimes++;
                if(retCode == Constants.ERROR_RET ){
                    if(errorTimes < Constants.ALLOWED_ERROR_TIMES){
                        return Constants.ERROR_RET;
                    }else{
                        errorTimes = 0;
                        lastGesture = expectedGesture;
                        expectedGesture = expectedGesture + 1;
                        return Constants.ERROR_AND_FORWARD_RET;
                    }
                }else{
                    errorTimes = 0;
                    lastGesture = expectedGesture;
                    expectedGesture = expectedGesture + 1;
                    return retCode;
                }
            }else{// Do not play out because time hasn't been run out
                return Constants.UNDEF_RET;
            }
        }else{
            return Constants.UNDEF_RET;
        }



    }

    private int synthesizeStateMachine(){

        /***
         * Note that in this function, all we use to label each step is not idx at all, we use the real name of the step
         * idx: 0,1,2,3,4,5,6,7,8,9,10...
         * real name: 1,2,3,4,5,6,7,8,9,10...
         */

        int [] ct = new int [2];
        for(int s : stateMachine){
            ct[s] += 1;
        }

        int cur_state = -1;
        if(ct[1] >= Constants.STATE_MACHINE_POSTIVE_THRESHOLD[expectedGesture-1]){
            cur_state = 1;
        }else{
            cur_state = 0;
        }
        String debug_state_machine = "0: " + Integer.toString(ct[0]) + " 1: " + Integer.toString(ct[1]);


        Log.e(TAG, "State machine: " + debug_state_machine);
        int ret = cur_state;
        return ret;
    }

    private int evaluateResult(){
        int expectedCur = 0;
        if(lastGesture < Constants.STATE_SPACE){
            expectedCur = lastGesture + 1;
        }
        String debug_multi_res = "";
        for(int i = 0; i < Constants.RECOGNITION_DURATION; i++){
            debug_multi_res = debug_multi_res + gestureCache[i] + " ";
            if(gestureCache[i] == 1){
                if(expectedCur == Constants.STATE_SPACE){
                    return Constants.FINISH_RET;
                }else{
                    return Constants.RIGHT_RET;
                }
            }
        }
        Log.e(TAG, "To recog: " + expectedCur + " " + debug_multi_res);
        return Constants.ERROR_RET;
    }

    public int getLastGesture(){
        return lastGesture;
    }

    public void resetLastGesutre(){
        lastGesture = 0;
    }

    public double Mean(float[][] data,int col, int num){
        float temp=0;
        for(int i=0;i<num;i++) temp+=data[i][col];
        return temp/num;
    }

    public double Var(float[][] data,int col, int num){
        float temp=0;
        double mean=Mean(data,col,num);
        for(int i=0;i<num;i++) temp+=(float)Math.pow((data[i][col]-mean),2);
        return temp/num;
    }

    public double Rms(float[][] data,int col, int num){
        float temp=0;
        for(int i=0;i<num;i++) temp+=(float)Math.pow(data[i][col],2);
        return (float)Math.sqrt(temp/num);
    }

    public double Skew(float[][] data,int col, int num){

        float temp=0;
        double mean=Mean(data,col,num);
        float std=(float)Math.sqrt(Var(data,col,num));
        float std3=(float)Math.pow(std,3);
        // Sum((x_i-Ux)^3)
        for(int i=0;i<num;i++) temp+=(float)Math.pow((data[i][col]-mean),3);
        float m3 = temp / num;
        float m2_23 = std3;

        return m3/m2_23;
    }

    public double Kurtosis(float[][] data,int col, int num){
        float temp=0;
        double mean=Mean(data,col,num);
        float var2=(float)Math.pow(Var(data,col,num),2);
        // fourth moment
        for(int i=0;i<num;i++) temp+=(float)Math.pow((data[i][col]-mean),4);
        temp/=num;
        return temp/var2;
    }

    public void reset(){
        initFlag = false;
    }

}

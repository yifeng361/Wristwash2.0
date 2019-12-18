package com.example.wristwashsmartphone;

public class Constants {
    public static final String MODEL_NAME = "DNN_3Step_handwash_nonorm.tflite";
    public static final String [] SAVED_FILE = {"step1.txt","step2.txt","step3.txt","step4.txt","step5.txt"
            ,"step6.txt","step7.txt","step8.txt","step9.txt","step10.txt","step11.txt"
    };

    /**
     * Message Constants
     * */
    public static final String PREAMBLE = "PREAMBLE#";
    public static final String POSTAMBLE = "#END";
    public static final int SENSOR_TYPE_NUM = 2;
    public static final int SENSOR_DIRECTION_NUM = 3;
    public static final int SENSOR_DIMENSION = SENSOR_TYPE_NUM * SENSOR_DIRECTION_NUM;

    public static final float errorVal = 65536;


    /** This is determined by how long each gesture sustains and how long it takes to generate
     * a recognition output.
     * */
    public static final int STATE_MACHINE_SIZE = 5;
    public static final int STATE_SPACE = 13; // how many steps needed to be recognized

    /***
     * Some other Constants
     */
    public static final int USER_MODE = 0;
    public static final int DATA_COLLECTION_MODE = 1;


    /**
     * Configs
     */
    public static final String WRONG_GESTURE_WARNING = "You are doing wrongly for step ";
    public static final String RIGHT_GESTURE_INFO = " Dectected. ";
    public static final String FINISH_INFO = "Handwashing Finished";
    public static final int MAX_SENSOR_DATA_SIZE = 105;
    public static final int WINDOW_SIZE = 90;
    public static final int STEP_SIZE = 45;
    public static final int FEATURE_DIMENSION = 5; // mean, var, rms, kri, skew, kurtosis
    public static final int FEATURE_SIZE = FEATURE_DIMENSION * SENSOR_DIMENSION;
    public static final int OUTPUT_DIMENSION = 3;
    public static final int SAMPLING_RATE = 15;
    public static final int MIN_MSG_LENGTH = STEP_SIZE*SENSOR_DIMENSION;
    public static final int CURMODE = DATA_COLLECTION_MODE;
    public static final String SAVED_FILE_PREFIX = "sensor_";
    public static final int RECOGNITION_DURATION = 3;
    public static final boolean OPEN_AUDIO_INFO = true;
    public static final int ALLOWED_ERROR_TIMES = 3;
    public static final int MAX_GLOBAL_RECOGNITIOME_TIMES = 33;
    public static final int [] STATE_MACHINE_POSTIVE_THRESHOLD = {
            STATE_MACHINE_SIZE/2,
            STATE_MACHINE_SIZE/2,
            1,
            2,
            2,
            STATE_MACHINE_SIZE/2,
            STATE_MACHINE_SIZE/2,
            STATE_MACHINE_SIZE/2,
            STATE_MACHINE_SIZE/2,
            2,
            STATE_MACHINE_SIZE/2,
            STATE_MACHINE_SIZE/2,
            STATE_MACHINE_SIZE/2,
    };

    /****
     * Ret Code
     */

    public static final int UNDEF_RET = -1;
    public static final int ERROR_RET = 0;
    public static final int ERROR_AND_FORWARD_RET = 1;
    public static final int RIGHT_RET = 2;
    public static final int FINISH_RET = 3;
}

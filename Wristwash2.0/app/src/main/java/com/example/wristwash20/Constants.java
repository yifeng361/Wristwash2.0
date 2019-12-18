package com.example.wristwash20;

public class Constants {
    public static final int SENSOR_DIMENSION = 6;
    public static final int SENSOR_TYPE_NUM = 2;
    public static final int SENSOR_BUFFER_SIZE = 100;

    public static final boolean OPEN_VIBRATION = true;

    public interface ACTION {
        String RECORD_LABEL = "com.teamwishwash.wristwash.action.record";
        String START_SERVICE = "com.teamwishwash.wristwash.action.start-service";
        String STOP_SERVICE = "com.teamwishwash.wristwash.action.stop-service";
        String SEND_SCORE = "com.teamwishwash.wristwash.action.send-score";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
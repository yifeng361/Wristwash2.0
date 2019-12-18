package com.example.wristwashsmartphone;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class UserWarningService {
    TextToSpeech ttsObj;

    public UserWarningService(Context context){
        ttsObj = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    ttsObj.setLanguage(Locale.US);
                }
            }
        });

    }

    public void playSpeech(String toSpeak){
        ttsObj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH,null);
    }
}

package com.example.wristwashsmartphone;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class classifier_class {

    /** An instance of the driver class to run model inference with Tensorflow Lite. */
    protected Interpreter tflite;
    /** The loaded TensorFlow Lite model. */
    private MappedByteBuffer tfliteModel;
    private String mModelName;

    /** Initializes a {@code Classifier}. */
    protected classifier_class(String modelName, Activity activity) throws IOException {
        mModelName = modelName;
        tfliteModel = loadModelFile(activity);
        tflite = new Interpreter(tfliteModel);
    }

    /** Memory-map the model file in Assets. */
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(mModelName);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public Interpreter getTflite(){return tflite;}

}

package com.example.wristwashsmartphone.model;

public class RandomForestClassifier1 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[6] <= 2.2854758501052856) {
            if (features[26] <= 7.69676399230957) {
                classes[0] = 27; 
                classes[1] = 152; 
            } else {
                classes[0] = 28; 
                classes[1] = 13; 
            }
        } else {
            if (features[15] <= -1.4495434165000916) {
                classes[0] = 0; 
                classes[1] = 1; 
            } else {
                classes[0] = 95; 
                classes[1] = 1; 
            }
        }
        int class_idx = 0;
        int class_val = classes[0];
        for (int i = 1; i < 2; i++) {
            if (classes[i] > class_val) {
                class_idx = i;
                class_val = classes[i];
            }
        }
        return class_idx;
    }
    
    public static int predict_1(double[] features) {
        int[] classes = new int[2];
        
        if (features[29] <= 0.473259374499321) {
            if (features[29] <= 0.1292823627591133) {
                classes[0] = 1; 
                classes[1] = 56; 
            } else {
                classes[0] = 59; 
                classes[1] = 99; 
            }
        } else {
            if (features[0] <= -2.8806110620498657) {
                classes[0] = 24; 
                classes[1] = 17; 
            } else {
                classes[0] = 59; 
                classes[1] = 2; 
            }
        }
        int class_idx = 0;
        int class_val = classes[0];
        for (int i = 1; i < 2; i++) {
            if (classes[i] > class_val) {
                class_idx = i;
                class_val = classes[i];
            }
        }
        return class_idx;
    }
    
    public static int predict(double[] features) {
        int n_classes = 2;
        int[] classes = new int[n_classes];
        classes[RandomForestClassifier1.predict_0(features)]++;
        classes[RandomForestClassifier1.predict_1(features)]++;
    
        int class_idx = 0;
        int class_val = classes[0];
        for (int i = 1; i < n_classes; i++) {
            if (classes[i] > class_val) {
                class_idx = i;
                class_val = classes[i];
            }
        }
        return class_idx;
    }

    public static void main(String[] args) {
        if (args.length == 30) {

            // Features:
            double[] features = new double[args.length];
            for (int i = 0, l = args.length; i < l; i++) {
                features[i] = Double.parseDouble(args[i]);
            }

            // Prediction:
            int prediction = RandomForestClassifier1.predict(features);
            System.out.println(prediction);

        }
    }
}
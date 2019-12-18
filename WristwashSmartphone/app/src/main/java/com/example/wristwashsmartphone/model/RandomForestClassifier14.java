package com.example.wristwashsmartphone.model;

public class RandomForestClassifier14 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[4] <= -0.2049444392323494) {
            if (features[7] <= 2.8953705728054047) {
                classes[0] = 5; 
                classes[1] = 0; 
            } else {
                classes[0] = 1; 
                classes[1] = 38; 
            }
        } else {
            if (features[0] <= -1.3597221970558167) {
                classes[0] = 94; 
                classes[1] = 6; 
            } else {
                classes[0] = 51; 
                classes[1] = 129; 
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
        
        if (features[27] <= 1.5898895859718323) {
            if (features[4] <= 0.09788889065384865) {
                classes[0] = 71; 
                classes[1] = 2; 
            } else {
                classes[0] = 4; 
                classes[1] = 5; 
            }
        } else {
            if (features[0] <= -0.770499974489212) {
                classes[0] = 52; 
                classes[1] = 7; 
            } else {
                classes[0] = 16; 
                classes[1] = 167; 
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
        classes[RandomForestClassifier14.predict_0(features)]++;
        classes[RandomForestClassifier14.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier14.predict(features);
            System.out.println(prediction);

        }
    }
}
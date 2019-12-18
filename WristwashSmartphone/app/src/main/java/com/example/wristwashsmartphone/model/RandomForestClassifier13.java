package com.example.wristwashsmartphone.model;

public class RandomForestClassifier13 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[26] <= 8.118812084197998) {
            if (features[6] <= 9.717658519744873) {
                classes[0] = 55; 
                classes[1] = 165; 
            } else {
                classes[0] = 33; 
                classes[1] = 4; 
            }
        } else {
            if (features[0] <= -3.918222188949585) {
                classes[0] = 2; 
                classes[1] = 1; 
            } else {
                classes[0] = 61; 
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
        
        if (features[22] <= -1.0781527757644653) {
            if (features[16] <= 0.22001811116933823) {
                classes[0] = 58; 
                classes[1] = 5; 
            } else {
                classes[0] = 6; 
                classes[1] = 7; 
            }
        } else {
            if (features[0] <= -3.6771111488342285) {
                classes[0] = 18; 
                classes[1] = 162; 
            } else {
                classes[0] = 60; 
                classes[1] = 6; 
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
        classes[RandomForestClassifier13.predict_0(features)]++;
        classes[RandomForestClassifier13.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier13.predict(features);
            System.out.println(prediction);

        }
    }
}
package com.example.wristwashsmartphone.model;

public class RandomForestClassifier5 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[6] <= 9.786612510681152) {
            if (features[6] <= 6.950969219207764) {
                classes[0] = 117; 
                classes[1] = 0; 
            } else {
                classes[0] = 12; 
                classes[1] = 8; 
            }
        } else {
            if (features[1] <= -2.68450003862381) {
                classes[0] = 2; 
                classes[1] = 151; 
            } else {
                classes[0] = 7; 
                classes[1] = 0; 
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
        
        if (features[29] <= 0.8360190093517303) {
            if (features[29] <= 0.7546246945858002) {
                classes[0] = 104; 
                classes[1] = 0; 
            } else {
                classes[0] = 6; 
                classes[1] = 2; 
            }
        } else {
            if (features[0] <= -2.4167778491973877) {
                classes[0] = 3; 
                classes[1] = 156; 
            } else {
                classes[0] = 18; 
                classes[1] = 8; 
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
        classes[RandomForestClassifier5.predict_0(features)]++;
        classes[RandomForestClassifier5.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier5.predict(features);
            System.out.println(prediction);

        }
    }
}
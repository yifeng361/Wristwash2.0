package com.example.wristwashsmartphone.model;

public class RandomForestClassifier7 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[26] <= 6.859022855758667) {
            if (features[2] <= -0.9423333406448364) {
                classes[0] = 23; 
                classes[1] = 7; 
            } else {
                classes[0] = 50; 
                classes[1] = 0; 
            }
        } else {
            if (features[0] <= -4.313222169876099) {
                classes[0] = 4; 
                classes[1] = 65; 
            } else {
                classes[0] = 73; 
                classes[1] = 95; 
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
        
        if (features[29] <= 0.22480254620313644) {
            classes[0] = 29; 
            classes[1] = 0; 
        } else {
            if (features[12] <= -0.005549436784349382) {
                classes[0] = 85; 
                classes[1] = 61; 
            } else {
                classes[0] = 29; 
                classes[1] = 113; 
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
        classes[RandomForestClassifier7.predict_0(features)]++;
        classes[RandomForestClassifier7.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier7.predict(features);
            System.out.println(prediction);

        }
    }
}
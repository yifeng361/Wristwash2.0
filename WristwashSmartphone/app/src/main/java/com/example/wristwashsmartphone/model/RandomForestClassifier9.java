package com.example.wristwashsmartphone.model;

public class RandomForestClassifier9 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[16] <= -0.19090720266103745) {
            if (features[2] <= -2.895777702331543) {
                classes[0] = 26; 
                classes[1] = 3; 
            } else {
                classes[0] = 13; 
                classes[1] = 94; 
            }
        } else {
            if (features[8] <= 22.915550231933594) {
                classes[0] = 85; 
                classes[1] = 15; 
            } else {
                classes[0] = 27; 
                classes[1] = 61; 
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
        
        if (features[27] <= 4.452652931213379) {
            if (features[12] <= -1.0239375829696655) {
                classes[0] = 8; 
                classes[1] = 2; 
            } else {
                classes[0] = 111; 
                classes[1] = 2; 
            }
        } else {
            if (features[13] <= -1.1665745973587036) {
                classes[0] = 4; 
                classes[1] = 2; 
            } else {
                classes[0] = 20; 
                classes[1] = 175; 
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
        classes[RandomForestClassifier9.predict_0(features)]++;
        classes[RandomForestClassifier9.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier9.predict(features);
            System.out.println(prediction);

        }
    }
}
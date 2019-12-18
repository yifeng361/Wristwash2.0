package com.example.wristwashsmartphone.model;

public class RandomForestClassifier10 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[12] <= 0.1347956359386444) {
            if (features[2] <= 0.7543333172798157) {
                classes[0] = 64; 
                classes[1] = 0; 
            } else {
                classes[0] = 46; 
                classes[1] = 50; 
            }
        } else {
            if (features[15] <= 0.039899665862321854) {
                classes[0] = 32; 
                classes[1] = 12; 
            } else {
                classes[0] = 8; 
                classes[1] = 105; 
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
        
        if (features[27] <= 1.4173645377159119) {
            classes[0] = 75; 
            classes[1] = 0; 
        } else {
            if (features[15] <= -0.12849918752908707) {
                classes[0] = 39; 
                classes[1] = 12; 
            } else {
                classes[0] = 29; 
                classes[1] = 162; 
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
        classes[RandomForestClassifier10.predict_0(features)]++;
        classes[RandomForestClassifier10.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier10.predict(features);
            System.out.println(prediction);

        }
    }
}
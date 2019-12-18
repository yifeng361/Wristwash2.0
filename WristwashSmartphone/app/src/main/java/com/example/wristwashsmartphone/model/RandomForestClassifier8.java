package com.example.wristwashsmartphone.model;

public class RandomForestClassifier8 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[6] <= 3.7794183492660522) {
            if (features[2] <= 4.110944390296936) {
                classes[0] = 61; 
                classes[1] = 0; 
            } else {
                classes[0] = 7; 
                classes[1] = 145; 
            }
        } else {
            if (features[8] <= 7.2764482498168945) {
                classes[0] = 13; 
                classes[1] = 14; 
            } else {
                classes[0] = 57; 
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
        
        if (features[27] <= 1.9587923288345337) {
            if (features[29] <= 1.4244861602783203) {
                classes[0] = 90; 
                classes[1] = 0; 
            } else {
                classes[0] = 6; 
                classes[1] = 1; 
            }
        } else {
            if (features[20] <= -0.9778823852539062) {
                classes[0] = 20; 
                classes[1] = 7; 
            } else {
                classes[0] = 15; 
                classes[1] = 158; 
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
        classes[RandomForestClassifier8.predict_0(features)]++;
        classes[RandomForestClassifier8.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier8.predict(features);
            System.out.println(prediction);

        }
    }
}
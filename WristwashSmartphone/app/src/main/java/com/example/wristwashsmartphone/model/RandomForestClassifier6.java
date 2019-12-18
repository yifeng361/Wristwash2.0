package com.example.wristwashsmartphone.model;

public class RandomForestClassifier6 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[6] <= 1.825486958026886) {
            if (features[2] <= 7.356333255767822) {
                classes[0] = 61; 
                classes[1] = 2; 
            } else {
                classes[0] = 4; 
                classes[1] = 3; 
            }
        } else {
            if (features[1] <= -6.0179443359375) {
                classes[0] = 33; 
                classes[1] = 160; 
            } else {
                classes[0] = 52; 
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
    
    public static int predict_1(double[] features) {
        int[] classes = new int[2];
        
        if (features[29] <= 0.5920373797416687) {
            if (features[29] <= 0.1860102415084839) {
                classes[0] = 16; 
                classes[1] = 1; 
            } else {
                classes[0] = 54; 
                classes[1] = 168; 
            }
        } else {
            if (features[3] <= -2.4323889017105103) {
                classes[0] = 0; 
                classes[1] = 2; 
            } else {
                classes[0] = 73; 
                classes[1] = 3; 
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
        classes[RandomForestClassifier6.predict_0(features)]++;
        classes[RandomForestClassifier6.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier6.predict(features);
            System.out.println(prediction);

        }
    }
}
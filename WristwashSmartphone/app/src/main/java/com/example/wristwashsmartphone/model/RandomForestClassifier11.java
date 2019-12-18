package com.example.wristwashsmartphone.model;

public class RandomForestClassifier11 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[6] <= 2.080430507659912) {
            if (features[7] <= 4.83964729309082) {
                classes[0] = 24; 
                classes[1] = 117; 
            } else {
                classes[0] = 27; 
                classes[1] = 3; 
            }
        } else {
            if (features[0] <= -0.48955555260181427) {
                classes[0] = 70; 
                classes[1] = 7; 
            } else {
                classes[0] = 29; 
                classes[1] = 40; 
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
        
        if (features[27] <= 1.2267800569534302) {
            if (features[15] <= -0.601514995098114) {
                classes[0] = 19; 
                classes[1] = 7; 
            } else {
                classes[0] = 30; 
                classes[1] = 160; 
            }
        } else {
            if (features[3] <= -2.6910555362701416) {
                classes[0] = 0; 
                classes[1] = 3; 
            } else {
                classes[0] = 94; 
                classes[1] = 4; 
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
        classes[RandomForestClassifier11.predict_0(features)]++;
        classes[RandomForestClassifier11.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier11.predict(features);
            System.out.println(prediction);

        }
    }
}
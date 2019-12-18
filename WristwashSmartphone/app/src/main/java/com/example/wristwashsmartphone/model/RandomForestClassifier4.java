package com.example.wristwashsmartphone.model;

public class RandomForestClassifier4 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[6] <= 3.55240797996521) {
            if (features[2] <= 5.810333251953125) {
                classes[0] = 87; 
                classes[1] = 0; 
            } else {
                classes[0] = 19; 
                classes[1] = 38; 
            }
        } else {
            if (features[8] <= 12.109400749206543) {
                classes[0] = 10; 
                classes[1] = 124; 
            } else {
                classes[0] = 34; 
                classes[1] = 5; 
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
        
        if (features[27] <= 1.0357344150543213) {
            if (features[29] <= 0.35986340045928955) {
                classes[0] = 37; 
                classes[1] = 22; 
            } else {
                classes[0] = 4; 
                classes[1] = 145; 
            }
        } else {
            if (features[13] <= -1.1652618646621704) {
                classes[0] = 11; 
                classes[1] = 4; 
            } else {
                classes[0] = 91; 
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
        classes[RandomForestClassifier4.predict_0(features)]++;
        classes[RandomForestClassifier4.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier4.predict(features);
            System.out.println(prediction);

        }
    }
}
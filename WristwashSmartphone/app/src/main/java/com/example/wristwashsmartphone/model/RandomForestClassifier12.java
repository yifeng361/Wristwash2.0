package com.example.wristwashsmartphone.model;

public class RandomForestClassifier12 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[26] <= 5.294013023376465) {
            if (features[29] <= 0.9260288774967194) {
                classes[0] = 5; 
                classes[1] = 100; 
            } else {
                classes[0] = 5; 
                classes[1] = 0; 
            }
        } else {
            if (features[0] <= -0.7809999883174896) {
                classes[0] = 91; 
                classes[1] = 2; 
            } else {
                classes[0] = 37; 
                classes[1] = 57; 
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
        
        if (features[27] <= 2.8339000940322876) {
            if (features[29] <= 0.23113112151622772) {
                classes[0] = 18; 
                classes[1] = 0; 
            } else {
                classes[0] = 73; 
                classes[1] = 165; 
            }
        } else {
            if (features[22] <= 1.716038465499878) {
                classes[0] = 40; 
                classes[1] = 0; 
            } else {
                classes[0] = 0; 
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
    
    public static int predict(double[] features) {
        int n_classes = 2;
        int[] classes = new int[n_classes];
        classes[RandomForestClassifier12.predict_0(features)]++;
        classes[RandomForestClassifier12.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier12.predict(features);
            System.out.println(prediction);

        }
    }
}
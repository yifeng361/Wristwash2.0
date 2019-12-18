package com.example.wristwashsmartphone.model;

public class RandomForestClassifier3 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[26] <= 8.009511232376099) {
            if (features[26] <= 6.798527717590332) {
                classes[0] = 68; 
                classes[1] = 0; 
            } else {
                classes[0] = 37; 
                classes[1] = 2; 
            }
        } else {
            if (features[25] <= 7.336930751800537) {
                classes[0] = 22; 
                classes[1] = 157; 
            } else {
                classes[0] = 11; 
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
        
        if (features[27] <= 1.6150864958763123) {
            if (features[16] <= 0.06406119465827942) {
                classes[0] = 20; 
                classes[1] = 148; 
            } else {
                classes[0] = 49; 
                classes[1] = 10; 
            }
        } else {
            if (features[3] <= -2.6434444189071655) {
                classes[0] = 0; 
                classes[1] = 3; 
            } else {
                classes[0] = 62; 
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
    
    public static int predict(double[] features) {
        int n_classes = 2;
        int[] classes = new int[n_classes];
        classes[RandomForestClassifier3.predict_0(features)]++;
        classes[RandomForestClassifier3.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier3.predict(features);
            System.out.println(prediction);

        }
    }
}
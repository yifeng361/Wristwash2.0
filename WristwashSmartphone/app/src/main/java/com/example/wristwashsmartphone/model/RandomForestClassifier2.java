package com.example.wristwashsmartphone.model;

public class RandomForestClassifier2 {
    public static int predict_0(double[] features) {
        int[] classes = new int[2];
        
        if (features[26] <= 8.558786869049072) {
            if (features[7] <= 4.274806022644043) {
                classes[0] = 29; 
                classes[1] = 176; 
            } else {
                classes[0] = 88; 
                classes[1] = 18; 
            }
        } else {
            if (features[15] <= -0.7410460412502289) {
                classes[0] = 2; 
                classes[1] = 6; 
            } else {
                classes[0] = 69; 
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
    
    public static int predict_1(double[] features) {
        int[] classes = new int[2];
        
        if (features[27] <= 0.547767698764801) {
            if (features[29] <= 0.491501048207283) {
                classes[0] = 6; 
                classes[1] = 99; 
            } else {
                classes[0] = 6; 
                classes[1] = 0; 
            }
        } else {
            if (features[22] <= 1.0131151676177979) {
                classes[0] = 163; 
                classes[1] = 86; 
            } else {
                classes[0] = 5; 
                classes[1] = 26; 
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
        classes[RandomForestClassifier2.predict_0(features)]++;
        classes[RandomForestClassifier2.predict_1(features)]++;
    
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
            int prediction = RandomForestClassifier2.predict(features);
            System.out.println(prediction);

        }
    }
}
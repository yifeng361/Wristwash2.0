package com.example.wristwashsmartphone;

import com.example.wristwashsmartphone.model.RandomForestClassifier;
import com.example.wristwashsmartphone.model.RandomForestClassifier1;
import com.example.wristwashsmartphone.model.RandomForestClassifier2;
import com.example.wristwashsmartphone.model.RandomForestClassifier3;
import com.example.wristwashsmartphone.model.RandomForestClassifier4;
import com.example.wristwashsmartphone.model.RandomForestClassifier5;
import com.example.wristwashsmartphone.model.RandomForestClassifier6;
import com.example.wristwashsmartphone.model.RandomForestClassifier7;
import com.example.wristwashsmartphone.model.RandomForestClassifier8;
import com.example.wristwashsmartphone.model.RandomForestClassifier9;
import com.example.wristwashsmartphone.model.RandomForestClassifier10;
import com.example.wristwashsmartphone.model.RandomForestClassifier11;
import com.example.wristwashsmartphone.model.RandomForestClassifier12;
import com.example.wristwashsmartphone.model.RandomForestClassifier13;
import com.example.wristwashsmartphone.model.RandomForestClassifier14;



public class ModelSelectionLayer {
    RandomForestClassifier1 rfc1;
    RandomForestClassifier2 rfc2;
    RandomForestClassifier3 rfc3;
    RandomForestClassifier4 rfc4;
    RandomForestClassifier5 rfc5;
    RandomForestClassifier6 rfc6;
    RandomForestClassifier7 rfc7;
    RandomForestClassifier8 rfc8;
    RandomForestClassifier9 rfc9;
    RandomForestClassifier10 rfc10;
    RandomForestClassifier11 rfc11;
    RandomForestClassifier12 rfc12;
    RandomForestClassifier13 rfc13;
    RandomForestClassifier14 rfc14;

    public int predict(int modelIdx, double [] feature){
        int ret = 0;
        switch (modelIdx){
            case 1:
                ret = rfc1.predict(feature);
                break;
            case 2:
                ret = rfc2.predict(feature);
                break;
            case 3:
                ret = rfc3.predict(feature);
                break;
            case 4:
                ret = rfc4.predict(feature);
                break;
            case 5:
                ret = rfc5.predict(feature);
                break;
            case 6:
                ret = rfc6.predict(feature);
                break;
            case 7:
                ret = rfc7.predict(feature);
                break;
            case 8:
                ret = rfc8.predict(feature);
                break;
            case 9:
                ret = rfc9.predict(feature);
                break;
            case 10:
                ret = rfc10.predict(feature);
                break;
            case 11:
                ret = rfc11.predict(feature);
                break;
            case 12:
                ret = rfc12.predict(feature);
                break;
            case 13:
                ret = rfc13.predict(feature);
                break;
            case 14:
                ret = rfc14.predict(feature);
                break;

        }
        return ret;
    }
}

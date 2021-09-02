package com.indico.toolkit;
import org.json.simple.parser.JSONParser;
import com.indico.toolkit.Association;
import com.indico.toolkit.Prediction;

import java.util.Arrays;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class TestAssociation{
    Prediction[] predictions;

    @Test
    void testNumberPredictions(){
        Assertions.assertEquals(1, 1);
        String preds = predictions.getClass().getClassLoader().getResource("preds.json").toString();
        String data = Resources.getResource("preds.json");
    }
}
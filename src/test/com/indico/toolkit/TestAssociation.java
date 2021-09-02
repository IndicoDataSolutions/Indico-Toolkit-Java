package com.indico.toolkit;

import main.com.indico.toolkit.Association;
import com.indico.toolkit.Prediction;

import java.util.Arrays;
import junit.framework.assertEquals;

class TestAssociation{
    Prediction[] predictions = new Prediction[]{Prediction(1, 2, "hello", "fml")};

    @Test
    void testNumberPredictions(){
        Assocation assoc = new Association(this.predictions);
        assertEquals(1, assoc.numberPredictions);
    }
}
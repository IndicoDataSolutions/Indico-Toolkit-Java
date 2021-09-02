package com.indico.toolkit;


import com.indico.toolkit.Association;
import com.indico.toolkit.Prediction;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class TestAssociation{
    Prediction[] predictions;

    @Test
    void testNumberPredictions() throws IOException {
        Assertions.assertEquals(1, 1);
        ClassLoader classLoader = getClass().getClassLoader();
        String predsLoc = classLoader.getResource("preds.json").getFile().toString();
        Reader reader = Files.newBufferedReader(Paths.get(predsLoc));
        Gson gson = new Gson();
        Prediction[] preds = gson.fromJson(reader, Prediction[].class);
        Assertions.assertEquals(preds, "hi!");
    }
}
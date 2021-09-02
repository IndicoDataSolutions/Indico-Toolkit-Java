package com.indico.toolkit;

import com.indico.toolkit.Association;
import com.indico.toolkit.Prediction;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Type;
import java.lang.Object;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestAssociation{
    List<Prediction> predictions = null;
    List<Object> tokens = null;
    ArrayList<String> line_fields = new ArrayList<>();

    @BeforeAll
    public void setup() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Gson gson = new Gson();
        Type predType = new TypeToken<ArrayList<Prediction>>(){}.getType();
        Type tokenType = new TypeToken<ArrayList<Object>>(){}.getType();
        String predsLoc = classLoader.getResource("preds.json").getFile().toString();
        Reader predsReader = Files.newBufferedReader(Paths.get(predsLoc));
        predictions = gson.fromJson(predsReader, predType);
        String tokensLoc = classLoader.getResource("tokens.json").getFile().toString();
        Reader tokenReader = Files.newBufferedReader(Paths.get(tokensLoc));
        tokens = gson.fromJson(predsReader, tokenType);
        line_fields.add("A");
        line_fields.add("B");
        line_fields.add("C");
    }
    @Test
    void testNumberPredictions() throws IOException {
        Association associate = new Association(predictions, line_fields);
        Assertions.assertEquals(associate.numberPredictions(), 5);
    }
}
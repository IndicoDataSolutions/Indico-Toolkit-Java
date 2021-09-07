package com.indico.toolkit;

import com.indico.toolkit.Association;
import com.indico.toolkit.Prediction;
import com.indico.toolkit.Token;

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
    List<Token> tokens = null;
    ArrayList<String> line_fields = new ArrayList<>();

    @BeforeAll
    public void setup() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Gson gson = new Gson();
        Type predType = new TypeToken<ArrayList<Prediction>>(){}.getType();
        String predsLoc = classLoader.getResource("preds.json").getFile().toString();
        Reader predsReader = Files.newBufferedReader(Paths.get(predsLoc));
        predictions = gson.fromJson(predsReader, predType);
        Type tokenType = new TypeToken<ArrayList<Token>>(){}.getType();
        String tokensLoc = classLoader.getResource("tokens.json").getFile().toString();
        Reader tokenReader = Files.newBufferedReader(Paths.get(tokensLoc));
        tokens = gson.fromJson(tokenReader, tokenType);
        line_fields.add("work_order_tonnage");
        line_fields.add("line_date");
        line_fields.add("work_order_number");
    }
    @Test
    void testNumberPredictions() {
        Association associate = new Association(predictions, line_fields);
        Assertions.assertEquals(6, associate.numberPredictions());
    }

    @Test
    void testRemoveUnneeded() {
        Association associate = new Association(predictions, line_fields);
        List<Prediction> neededPreds = associate.removeUnneededPredictions(predictions);
        Assertions.assertEquals(5, neededPreds.size());
        Assertions.assertEquals(1, associate.unmappedPositions.size());
    }

    @Test
    void testSequencesOverlapTrue() {
        Prediction pred = predictions.get(0);
        Token token = tokens.get(0);
        boolean result = Association.sequencesOverlap(pred, token);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testSequencesOverlapFalse() {
        Prediction pred = predictions.get(0);
        Token token = tokens.get(1);
        boolean result = Association.sequencesOverlap(pred, token);
        Assertions.assertEquals(false, result);
    }

    @Test
    void testSortPredictions(){
        Prediction firstPred = new Prediction();
        firstPred.start = 1;
        firstPred.end = 3;
        Prediction secondPred = new Prediction();
        secondPred.start = 4;
        secondPred.end = 6;
        List<Prediction> preds = new ArrayList<Prediction>();
        preds.add(secondPred);
        preds.add(firstPred);
        List<Prediction> finalList = Association.sortPredictions(preds);
        Assertions.assertEquals(1, finalList.get(0).start);
        Assertions.assertEquals(4, finalList.get(1).start);
        }
}
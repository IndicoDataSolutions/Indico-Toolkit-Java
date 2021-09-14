package com.indico.toolkit;

import com.indico.toolkit.types.DocOffset;
import com.indico.toolkit.types.Prediction;
import com.indico.toolkit.types.Token;


import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.lang.Integer;
import java.lang.reflect.Type;


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
        Assertions.assertEquals(1, associate.getNonLineItemPreds().size());
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

    @Test
    void testSortMappedPreds(){
        Prediction firstPred = new Prediction();
        firstPred.pageNum = 0;
        firstPred.bbTop = 10;
        Prediction secondPred = new Prediction();
        secondPred.pageNum = 1;
        secondPred.bbTop = 5;
        List<Prediction> preds = new ArrayList<Prediction>();
        preds.add(secondPred);
        preds.add(firstPred);
        List<Prediction> finalList = Association.sortMappedPreds(preds);
        Assertions.assertEquals(0, finalList.get(0).pageNum);
        Assertions.assertEquals(1, finalList.get(1).pageNum);
    }

    @Test
    void testSortTokens(){
        Token firstToken = new Token();
        firstToken.doc_offset = new DocOffset();
        firstToken.doc_offset.start = 1;
        firstToken.doc_offset.end = 3;
        Token secondToken = new Token();
        secondToken.doc_offset = new DocOffset();
        secondToken.doc_offset.start = 4;
        secondToken.doc_offset.end = 7;
        List<Token> badorder = new ArrayList<Token>();
        badorder.add(secondToken);
        badorder.add(firstToken);
        List<Token> finalList = Association.sortTokens(badorder);
        Assertions.assertEquals(1, finalList.get(0).doc_offset.start);
        Assertions.assertEquals(4, finalList.get(1).doc_offset.start);
    }

    @Test
    void testMatchPredToToken(){
        Association associate = new Association(predictions, line_fields);
        Prediction firstPred = new Prediction();
        firstPred.start = 26;
        firstPred.end = 70;
        associate.matchPredToToken(firstPred, tokens);
        Assertions.assertEquals(0, firstPred.pageNum);
        Assertions.assertEquals(25, firstPred.bbTop);
        Assertions.assertEquals(105, firstPred.bbBot);
    }

    @Test
    void testGetBoundingBoxes() {
        Association associate = new Association(predictions, line_fields);
        associate.getBoundingBoxes(tokens);
        for (Prediction pred : associate.getLineItemPreds()) {
            Assertions.assertTrue(pred.bbBot instanceof Integer);
            Assertions.assertTrue(pred.bbTop instanceof Integer);
            Assertions.assertTrue(pred.pageNum instanceof Integer);
        }
        Assertions.assertEquals(1, associate.getNonLineItemPreds().size());
        Assertions.assertEquals(5, associate.getLineItemPreds().size());
    }

    @Test
    void testAssignRowNumber() {
        Association associate = new Association(predictions, line_fields);
        associate.getBoundingBoxes(tokens);
        associate.assignRowNumber();
        for (Prediction pred : associate.getLineItemPreds()) {
            if(pred.text.contains("row 1")){
                Assertions.assertEquals(1, pred.rowNumber);
            }
            else if(pred.text.contains("row 2")){
                Assertions.assertEquals(2, pred.rowNumber);
            }
            else if(pred.text.contains("row 3")){
                Assertions.assertEquals(3, pred.rowNumber);
        }
        Assertions.assertEquals(1, associate.getNonLineItemPreds().size());
        Assertions.assertEquals(5, associate.getLineItemPreds().size());
        Assertions.assertEquals(6, associate.getAllPreds().size());
        Assertions.assertEquals(3, associate.getGroupedRows().size());
        }
    }
}
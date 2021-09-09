package com.indico.toolkit;

import com.indico.toolkit.Prediction;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Collections;
// TODO: test getBoundingBoxes and matchPredToToken

public class Association{
    public List<Prediction> predictions;
    public List<String> lineItemFields;
    public List<Prediction> mappedPositions = new ArrayList<Prediction>();
    public List<Prediction> unmappedPositions = new ArrayList<Prediction>();

    public Association(List<Prediction> predictions, List<String> lineItemFields){
        this.predictions = predictions;
        this.lineItemFields = lineItemFields;
    }

    public void getBoundingBoxes(List<Token> tokens){
        List<Prediction> preds = this.removeUnneededPredictions(this.predictions);
        preds = Association.sortPredictions(preds);
        tokens = Association.sortTokens(tokens);
        for (Prediction pred: preds) {
            this.matchPredToToken(pred, tokens);
            mappedPositions.add(pred);
        }
    }

    public void matchPredToToken(Prediction pred, List<Token> tokens){
        boolean no_match = true;
        for (Token token: tokens) {
            boolean overlap = Association.sequencesOverlap(pred, token);
            if (no_match && overlap){
                no_match = false;
                pred.bbTop = token.position.bbTop;
                pred.bbBot = token.position.bbBot;
                pred.pageNum = token.page_num;
            }
            else if (overlap){
                pred.bbTop = Math.min(pred.bbTop, token.position.bbTop);
                pred.bbBot =  Math.max(pred.bbBot, token.position.bbBot);
            }
            else if (token.doc_offset.start > pred.end) {
                break;
            }
        }
    }

    public void assignRowNumber(){
        List<Prediction> preds = Association.sortMappedPreds(this.mappedPositions);
        int minTop = preds.get(0).bbTop;
        int maxBot = preds.get(0).bbBot;
        int pageNum = preds.get(0).pageNum;
        int rowNum = 1;
        for(Prediction pred: preds){
            if(pred.bbTop > maxBot || pred.pageNum != pageNum){
                rowNum ++;
                pageNum = pred.pageNum;
                minTop = pred.bbTop;
                maxBot = pred.bbBot;
            }
            else{
                minTop = Math.min(minTop, pred.bbTop);
                maxBot = Math.max(maxBot, pred.bbBot);
            }
            pred.rowNumber = rowNum;
        }
    }

    public List<Prediction> removeUnneededPredictions(List<Prediction> predictions){
        List<Prediction> neededPreds = new ArrayList<>();
        for(Prediction pred: predictions){
            if(this.lineItemFields.contains(pred.label)){
                neededPreds.add(pred);
            }
            else {
                   this.unmappedPositions.add(pred);
            }
        }
        return neededPreds;
    }


    public static boolean sequencesOverlap(Prediction pred, Token token){
        if(pred.start < token.doc_offset.end){
            if(token.doc_offset.start < pred.end){
                return true;
            }
        }
        return false;
    }

    public static List<Prediction> sortPredictions(List<Prediction> predictions){
        Collections.sort(predictions);
        return predictions;
    }

    public static List<Token> sortTokens(List<Token> tokens){
        Collections.sort(tokens);
        return tokens;
    }

    public static List<Prediction> sortMappedPreds(List<Prediction> predictions){
        Collections.sort(predictions, new PredictionComparator());
        return predictions;
    }

    public int numberPredictions(){
        return this.predictions.size();
    }

}

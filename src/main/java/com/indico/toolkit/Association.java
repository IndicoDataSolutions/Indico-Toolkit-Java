package com.indico.toolkit;

import com.indico.toolkit.Prediction;

import java.util.*;
import java.lang.Math;

/**
 Association
 Map OCR bounding box positions from tokens to predictions and assign row numbers to predictions
 Example usage:::
    Association associate = new Association(List<Prediction> yourPreds, List<String> lineItemLabels);
    associate.getBoundingBoxes(List<Token> allOcrDocTokens);
    associate.assignRowNumber();

    // After running methods above:
    // All predictions line item and non line item
    List<Prediction> allPreds = associate.getAllPreds();
    // Just line item predictions
    List<Prediction> lineItemPreds = associate.getLineItemPreds();
    // Line item predictions grouped by their rows
    ArrayList<ArrayList<Prediction>> grouped = associate.getGroupedRows();
    // Just *non* line item predictions
    List<Prediction> nonLineItemPreds = associate.getNonLineItemPreds();

 */
public class Association{
    public List<Prediction> predictions;
    public List<String> lineItemFields;
    private List<Prediction> mappedPositions = new ArrayList<Prediction>();
    private List<Prediction> unmappedPositions = new ArrayList<Prediction>();


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

    public List<Prediction> getAllPreds(){
        List<Prediction> allPreds = new ArrayList<Prediction>();
        allPreds.addAll(this.mappedPositions);
        allPreds.addAll(this.unmappedPositions);
        return allPreds;
    }

    public List<Prediction> getLineItemPreds(){
        return this.mappedPositions;
    }

    public List<Prediction> getNonLineItemPreds(){
        return this.unmappedPositions;
    }

    public ArrayList<ArrayList<Prediction>> getGroupedRows(){
        Hashtable<Integer, ArrayList<Prediction>> grouped = new Hashtable<Integer, ArrayList<Prediction>>();
        for(Prediction pred: this.mappedPositions){
            if(grouped.containsKey(pred.rowNumber)){
                grouped.get(pred.rowNumber).add(pred);
            }
            else{
                ArrayList<Prediction> newRow = new ArrayList<Prediction>();
                newRow.add(pred);
                grouped.put(pred.rowNumber, newRow);
            }
        }
        ArrayList<ArrayList<Prediction>> groupedRows = new ArrayList<ArrayList<Prediction>>(grouped.values());
        return groupedRows;
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

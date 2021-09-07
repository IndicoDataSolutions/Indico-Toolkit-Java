package com.indico.toolkit;

import com.indico.toolkit.Prediction;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class Association{
    private static Object SortPreds;
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

    public int numberPredictions(){
        return this.predictions.size();
    }

}

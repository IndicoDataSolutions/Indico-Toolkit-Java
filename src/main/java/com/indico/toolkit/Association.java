package com.indico.toolkit;

import com.indico.toolkit.Prediction;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class Association{
    public ArrayList<Prediction> predictions;
    public ArrayList<String> lineItemFields;
    private ArrayList<Prediction> mappedPositions = new ArrayList<Prediction>();
    private ArrayList<Prediction> unmappedPositions = new ArrayList<Prediction>();

    public Association(ArrayList<Prediction> predictions, ArrayList<String> lineItemFields){
        this.predictions = predictions;
        this.lineItemFields = lineItemFields;
    }

    public void getBoundingBoxes(ArrayList<Prediction> tokens){
        ArrayList<Prediction> preds = this.removeUnneededPredictions(this.predictions);

    }

    private ArrayList<Prediction> removeUnneededPredictions(ArrayList<Prediction> predictions){
        ArrayList<Prediction> neededPreds = new ArrayList<Prediction>();
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



    public int numberPredictions(){
        return this.predictions.size();
    }

}

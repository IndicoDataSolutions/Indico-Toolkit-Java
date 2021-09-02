package com.indico.toolkit;

import com.indico.toolkit.Prediction;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class Association{
    public List<Prediction> predictions;
    public List<String> lineItemFields;
    private List<Prediction> mappedPositions = new ArrayList<Prediction>();
    private List<Prediction> unmappedPositions = new ArrayList<Prediction>();

    public Association(List<Prediction> predictions, List<String> lineItemFields){
        this.predictions = predictions;
        this.lineItemFields = lineItemFields;
    }

    public void getBoundingBoxes(List<Prediction> tokens){
        List<Prediction> preds = this.removeUnneededPredictions(this.predictions);

    }

    private List<Prediction> removeUnneededPredictions(List<Prediction> predictions){
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



    public int numberPredictions(){
        return this.predictions.size();
    }

}

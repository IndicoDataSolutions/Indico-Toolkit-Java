package com.indico.toolkit;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;

public class Association{
    public ArrayList<HashMap> predictions;
    public ArrayList<String> lineItemFields;
    private ArrayList<HashMap> mappedPositions = new ArrayList<HashMap>();
    private ArrayList<HashMap> unmappedPositions = new ArrayList<HashMap>();

    public Association(ArrayList<HashMap> predictions, ArrayList<String> lineItemFields){
        this.predictions = predictions;
        this.lineItemFields = lineItemFields;
    }

    public void getBoundingBoxes(ArrayList<HashMap> tokens){
        ArrayList<HashMap> preds = this.removeUnneededPredictions(this.predictions);

    }

    private ArrayList<HashMap> removeUnneededPredictions(ArrayList<HashMap> predictions){
        ArrayList<HashMap> neededPreds = new ArrayList<HashMap>();
        for(HashMap obj: predictions){
            String label = (String)obj.get("label");
            if(this.lineItemFields.contains(label)){
                neededPreds.add(obj);
            }
            else {
                   this.unmappedPositions.add(obj);
            }
        }
        return neededPreds;
    }

    private ArrayList<HashMap> sortPredictionsByStartIndex(ArrayList<HashMap> predictions){
        return Collections.sort(predictions, new Comparator<HashMap<String, Integer>>(){
            public int compare(HashMap<String, Integer> one, HashMap<String, Integer> two) {
                return one.get("start").compareTo(two.get("start"));
            }
        });
    }

    public int numberPredictions(){
        return this.predictions.size();
    }

}

class IndexCompare implements Comparator<HashMap> {
    @Override
    public int compare(HashMap o1, HashMap o2) {
        return (Integer)o1.get("start").compareTo((Integer)two.get("start"));
    }
}
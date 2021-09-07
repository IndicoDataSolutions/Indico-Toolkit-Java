package com.indico.toolkit;

public class Prediction implements Comparable<Prediction>{
    public int start;
    public int end;
    public String label;
    public String text;
    public Object confidence;
    public Integer rowIndex = null;

    public int getStart(){
        return this.start;
    }

    @Override
    public int compareTo(Prediction o){
        return Integer.compare(this.getStart(), o.getStart());
    }
}

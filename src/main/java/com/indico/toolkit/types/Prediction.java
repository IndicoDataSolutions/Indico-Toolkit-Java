package com.indico.toolkit.types;

public class Prediction implements Comparable<Prediction>{
    public int start;
    public int end;
    public String label;
    public String text;
    public Object confidence;
    public Integer rowNumber = null;
    public Integer bbTop = null;
    public Integer bbBot = null;
    public Integer pageNum = null;

    public int getStart(){
        return this.start;
    }

    @Override
    public int compareTo(Prediction o){
        return Integer.compare(this.getStart(), o.getStart());
    }
}

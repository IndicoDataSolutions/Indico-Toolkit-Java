package com.indico.toolkit;

import com.indico.toolkit.Position;
import com.indico.toolkit.DocOffset;


public class Token implements Comparable<Token>{
    public Position position;
    public int page_num;
    public DocOffset doc_offset;

    public int getStart(){
        return this.doc_offset.start;
    }

    @Override
    public int compareTo(Token o){
        return Integer.compare(this.getStart(), o.getStart());
    }
}

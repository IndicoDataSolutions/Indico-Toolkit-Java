package com.indico.toolkit;
import java.util.Comparator;

import com.indico.toolkit.types.Prediction;
import org.apache.commons.lang3.builder.CompareToBuilder;

public class PredictionComparator implements Comparator<Prediction> {

    @Override
    public int compare(Prediction o1, Prediction o2) {
        return new CompareToBuilder()
                .append(o1.pageNum, o2.pageNum)
                .append(o1.bbTop, o2.bbTop).toComparison();
    }
}

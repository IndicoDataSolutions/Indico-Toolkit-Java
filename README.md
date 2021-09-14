# Indico Toolkit (Java)
###  Tools to help integrate the Indico IPA

### Row Association
To group row predictions you must have already obtained your list of extraction predictions, 
your list of OCR token objects, and have a list of label strings for values that should be 
included as line items. Below find an example of how to use the association classes: 
```
Association associate = new Association(List<Prediction> yourPreds, List<String> lineItemLabels);
associate.getBoundingBoxes(List<Token> allOcrDocTokens);
associate.assignRowNumber();

// Get all predictions including line item and non line item predictions
List<Prediction> allPreds = associate.getAllPreds();

// Get just line item predictions grouped by their respective rows
ArrayList<ArrayList<Prediction>> grouped = associate.getGroupedRows();
```

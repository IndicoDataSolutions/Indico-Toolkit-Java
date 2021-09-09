# The Indico Java Toolkit
## Tools to help integrate the Indico IPA

### Line Item Association
To group line items you must have already obtained your list of extraction predictions, your list of
OCR token objects, and have a list of label strings for values that should be included as line items. 
```
Association associate = new Association(List<Prediction> yourPreds, List<String> lineItemLabels);
associate.getBoundingBoxes(List<Token> allOcrDocTokens);
associate.assignRowNumber();

// Get all predictions line item and non line item (only line item preds will have row and bounding box data added)
List<Prediction> allPreds = associate.getAllPreds();

// Get just line item predictions grouped by their rows
ArrayList<ArrayList<Prediction>> grouped = associate.getGroupedRows();
```

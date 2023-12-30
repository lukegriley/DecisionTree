package sol;

import src.IDataset;
import src.Row;

import java.util.LinkedList;
import java.util.List;

/**
 * A class that implements the IDataset interface,
 * representing a training data set.
 */
public class Dataset implements IDataset {
    // TODO: Implement methods declared in IDataset interface!
    List<String> attributeList;
    List<Row> dataObjects;
    public Dataset(List<String> attributeList, List<Row> dataObjects) {
        // TODO: implement the constructor!
        this.attributeList = attributeList;
        this.dataObjects = dataObjects;
    }

    /**
     * @return the Dataset's list of attributes
     */
    public List<String> getAttributeList() {
        return this.attributeList;
    }

    /**
     * @return a List of every Row in the Dataset
     */
    public List<Row> getDataObjects() {
        return this.dataObjects;
    }

    Dataset filterData(String attributeType, String attributeValue) {
        //start with empty list
        List filteredDataObjects = new LinkedList();

        //go through this.dataObjects list, add matching attributeValues to new list
        for (Row currentRow : this.dataObjects) {
            if(currentRow.getAttributeValue(attributeType).equals(attributeValue)){
                filteredDataObjects.add(currentRow);
            }
        }
        //return a NEW Dataset with a filtered dataObjects field
        return new Dataset(this.attributeList,filteredDataObjects);
    }

    public int size() {
        return this.dataObjects.size();
    }
}

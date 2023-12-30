package src;

import java.util.List;

/**
 * An interface to represent the dataset
 * (equivalent to the data table you worked with during the design check!)
 */
public interface IDataset {

    /**
     * Gets list of attributes in the dataset
     *
     * @return a list of strings
     */
    public List<String> getAttributeList();

    /**
     * Gets list of data objects (row) in the dataset
     *
     * @return a list of Rows
     */
    public List<Row> getDataObjects();

    /**
     * finds the size of the dataset (number of rows)
     *
     * @return the number of rows in the dataset
     */
    public int size();
}

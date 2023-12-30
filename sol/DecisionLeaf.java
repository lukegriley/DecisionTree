package sol;

import src.ITreeNode;
import src.Row;

/**
 * A class representing a leaf in the decision tree.
 */
public class DecisionLeaf implements ITreeNode {
    String outcomeValue;

    /**
     * constructs an instance of DecisionLeaf
     * @param outcomeValue the value of the tree's target attribute, returned in getDecision()
     */
    public DecisionLeaf(String outcomeValue) {
        this.outcomeValue = outcomeValue;
    }

    /**
     * returns the leaf's Decision given an input Row of data
     * @param forDatum the datum to lookup a decision for
     * @return the outcomeValue String for the tree's target attribute
     */
    @Override
    public String getDecision(Row forDatum) {
        //System.out.println("DL decision from:"+this.toString());
        return outcomeValue;
    }

    /**
     * @return a String representing the DecisionLeaf, to be used in the TreeGenerator's toString() method
     */
    @Override
    public String toString() {
        return "DL:"+this.outcomeValue;
    }
}

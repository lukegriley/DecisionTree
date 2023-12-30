package sol;

import src.ITreeNode;

/**
 * A class that represents the edge of a decision tree
 */
public class ValueEdge {
    private ITreeNode child;
    private String attributeValue;

    /**
     * constructs an instance of the ValueEdge class
     * @param child the ITreeNode that this edge will lead to
     * @param attributeValue a variant of a specific attribute that this edge represents
     */
    public ValueEdge(ITreeNode child, String attributeValue) {
        this.child = child;
        this.attributeValue = attributeValue;
    }

    /**
     * retrieves the edge's attributeValue
     * @return a String representing the edge's attribute value
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * retrieves the node under the edge
     * @return an ITreeNode that the edge leads to
     */
    public ITreeNode getChildNode() {
        return child;
    }

    /**
     * @return a String representing the ValueEdge, to be used in the TreeGenerator's toString() method
     */
    @Override
    public String toString() {
        return "EDGE:"+this.attributeValue+",child:"+this.child;
    }


}

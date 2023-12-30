package sol;

import com.sun.jdi.Value;
import src.ITreeNode;
import src.Row;

import javax.management.Attribute;
import java.util.List;

/**
 * An inner node in the decision tree
 */
public class AttributeNode implements ITreeNode {
    private List<ValueEdge> outgoingEdges;
    private String attributeType;
    private String defaultValue;


    /**
     * constructs an instance of AttributeNode
     * @param attributeType the type of attribute that this node will examine
     * @param defaultValue the decision returned if the Node's edges don't
     *                     contain the given row's value in getDecision()
     * @param outgoingEdges a List of ValueEdge for each distinct variant of attributeType, which lead
     *                      to child ITreeNodes
     */
    public AttributeNode(String attributeType,String defaultValue, List<ValueEdge> outgoingEdges) {
        this.attributeType = attributeType;
        this.defaultValue = defaultValue;
        this.outgoingEdges = outgoingEdges;
    }

    /**
     * Looks up the decision for a given row, starting from this AttributeNode
     * @param forDatum the datum to lookup a decision for
     * @return a String representing the decision for the row
     */
    @Override
    public String getDecision(Row forDatum) {
        for (ValueEdge edge : outgoingEdges) {
            if (forDatum.getAttributeValue(attributeType).equals(edge.getAttributeValue())) {
                return edge.getChildNode().getDecision(forDatum);
            }
        }
        return defaultValue;
    }

    /**
     * @return a String representing the decision tree starting from this AttributeNode
     */
    @Override
    public String toString() {
        return "NODE:"+this.attributeType+","+this.defaultValue+","+this.outgoingEdges.toString();
    }

}
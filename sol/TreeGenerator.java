package sol;

import src.ITreeGenerator;
import src.ITreeNode;
import src.Row;

import java.util.ArrayList;
import java.util.Random;
import java.util.LinkedList;
import java.util.List;

/**
 * A class that implements the ITreeGenerator interface
 * used to generate a tree
 */
public class TreeGenerator implements ITreeGenerator<Dataset> {

    /**
     * the top node, which is initialized when the this.generateTree() is called
     */
    private ITreeNode top;

    /**
     * creates an ITreeNode instance (either DecisionLeaf or AttributeNode) within the decision tree.
     * Recursively called, along with createEdges()
     * @param usedAttributes a LinkedList of attributes that have already been examined in the tree.
     *                       It is ok for there to be duplicate instances of the same attribute. This
     *                       function filters those out.
     * @param targetAttribute the attribute that the tree is trying to predict
     * @param subset the partition of the original input Dataset that lead to this node
     * @return an ITreeNode, either a DecisionLeaf that returns a Decision, or an AttributeNode
     *         examines one more attribute
     */
    private ITreeNode createNode(LinkedList<String> usedAttributes, String targetAttribute, Dataset subset) {

        //ensure that usedAttributes only contains distinct values
        LinkedList<String> updatedUsedAttributes = new LinkedList<String>();
        for (String att : usedAttributes) {
            if(!updatedUsedAttributes.contains(att)) {
                updatedUsedAttributes.add(att);
            }
        }

        //1st parameter: choose attribute to split upon
        Random random = new Random();
        List<String> allAttributes = subset.getAttributeList();
        int upperBound = allAttributes.size();

        //if you used every attribute except the targetAttribute, then you should create a DecisionLeaf
        //this prevents the while loop from looping infinitely, since otherwise any index it chooses would be
        //contained in the usedAttributes list or be the targetAttribute
        if(updatedUsedAttributes.size() >= upperBound - 1) {
            String outcome = subset.getDataObjects().get(0).getAttributeValue(targetAttribute);
            return new DecisionLeaf(outcome);
        }
        int randomIndex = random.nextInt(upperBound);
        //int randomIndex = 3;
        while (allAttributes.get(randomIndex).equals(targetAttribute) || updatedUsedAttributes.contains(allAttributes.get(randomIndex))){
            randomIndex = random.nextInt(upperBound);
        }
        String attributeType =  allAttributes.get(randomIndex);
        List<Row> subsetDataObjects = subset.getDataObjects();

        //2nd parameter: find defaultValue

        //a list for attribute values already checked
        List<String> valuesAlreadyChecked = new LinkedList<>();

        //a list for frequency of attributes values (index in this list corresponds to index previous)
        List<Integer> valueFrequencies = new LinkedList<>();

        //go through subsetDataObjects
        for (Row currentRow : subsetDataObjects) {

            String currentValue = currentRow.getAttributeValue(targetAttribute);
            // check if targetAttribute value is in our first list
            if (!valuesAlreadyChecked.contains(currentValue)) {
                //if not, call filterData and add the size of the output Dataset into our frequency list
                int currentFreq = subset.filterData(targetAttribute,currentValue).size();
                valuesAlreadyChecked.add(currentValue);
                valueFrequencies.add(currentFreq);
            }
        }
        //after going through every row in subsetDataObjects, find the max int in frequency list; get the index of that
        int currentMax = -1;
        int currentMaxIndex = -1;
        for (int i=0;i<valueFrequencies.size();i++) {
            if(valueFrequencies.get(i)>=currentMax) {
                currentMax = valueFrequencies.get(i);
                currentMaxIndex = i;
            }
        }
        //if this max frequency == subsetDataObjects.size(), then make a DecisionLeaf
        if(currentMax>=subsetDataObjects.size()) {
            //System.out.println("making DL bc max for:"+valuesAlreadyChecked.get(currentMaxIndex));
            return new DecisionLeaf(valuesAlreadyChecked.get(currentMaxIndex));
        }
        //initialize defaultValue
        String defaultValue = valuesAlreadyChecked.get(currentMaxIndex);

        //3rd parameter value: create list of edges (todo: decide on parameters)
        List<ValueEdge> outgoingEdges = this.createEdges(updatedUsedAttributes,targetAttribute,attributeType,subset);

        return new AttributeNode(attributeType,defaultValue,outgoingEdges);
    }

    /**
     * creates a List of ValueEdge that will go out from a specific node
     * @param usedAttributes a list of the attributes that have already been examined by previous nodes.
     *                       Ok if there are duplicates of the same attribute.
     * @param targetAttribute the attribute that the tree is trying to predict
     * @param nodeAttribute the attribute being examined by the AttributeNode these edges are connected to
     * @param subset the partition of the original Dataset that lead to these edges' parent node
     * @return
     */
    private List<ValueEdge> createEdges(LinkedList<String> usedAttributes, String targetAttribute, String nodeAttribute, Dataset subset) {

        //create list of distinct attribute variants in the nodeAttribute column of each Row
        //each variant will be used to create a new ValueEdge
        List<String> distinctAttributeValues = new ArrayList<String>();

        for (Row currentRow : subset.getDataObjects()) {
            String nodeAttValue = currentRow.getAttributeValue(nodeAttribute);
            if(!distinctAttributeValues.contains(nodeAttValue)) {
                distinctAttributeValues.add(nodeAttValue);
            }
        }

        //for each variant, create an edge with attributeValue = variant
        //this will be added to an ArrayList
        List<ValueEdge> outgoingEdges = new ArrayList<>();
        for (String variant : distinctAttributeValues) {
            usedAttributes.add(nodeAttribute);
            //this may modify the same list in memory down different recursive paths.
            //which is why the distinct values filter was added at start of this.createNode()
            Dataset filteredData = subset.filterData(nodeAttribute,variant);
            ITreeNode childNode = this.createNode(usedAttributes,targetAttribute,filteredData);
            outgoingEdges.add(new ValueEdge(childNode,variant));
        }
        return outgoingEdges;
    }

    /**
     * Generates the tree for a given training dataset.
     * @param trainingData    the dataset to train on
     * @param targetAttribute the attribute to predict
     */
    @Override
    public void generateTree(Dataset trainingData, String targetAttribute) {
       LinkedList<String> usedAttributes = new LinkedList<String>();
       top = this.createNode(usedAttributes, targetAttribute, trainingData);

    }

    /**
     * @return a String representation of the tree. Only can be called once tree has been generated
     */
    @Override
    public String toString() {
        try {
            return top.toString();
        } catch (NullPointerException e) {
            throw new NullPointerException("Tree has not yet been generated.");
        }
    }

    /**
     * Looks up the decision for a datum in the decision tree.
     * @param datum the datum to lookup a decision for
     * @return a String representing the decision for the row
     */
    public String getDecision(Row datum) {
        //try {
            return top.getDecision(datum);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Tree has not been generated yet.");
//        }
    }
}

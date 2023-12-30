package sol;

import com.sun.source.tree.Tree;
import org.junit.Assert;
import org.junit.Test;
import src.DecisionTreeCSVParser;
import src.Row;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;

public class BasicDatasetTest {
    // IMPORTANT: for this filepath to work, make sure the project is open as the top-level directory in IntelliJ
    // (See the first yellow information box in the handout testing section for details)
    String TRAINING_PATH = "sol/admissions-stats.csv"; // TODO: replace with your own input file
    String TARGET_ATTRIBUTE = "outcome"; // TODO: replace with your own target attribute

    TreeGenerator testGenerator;

    @Before
    public void buildTreeForTest() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(TRAINING_PATH);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects);
        // builds a TreeGenerator object and generates a tree for "foodType"
       testGenerator = new TreeGenerator();
       testGenerator.generateTree(training, TARGET_ATTRIBUTE);
    }

    @Test
    public void testClassification() {
        // makes a new (partial) Row representing the tangerine from the example
        // TODO: make your own rows based on your dataset
        Row studentTest1 = new Row("test row (student test 1)");
        studentTest1.setAttributeValue("GPA", "B");
        studentTest1.setAttributeValue("high school", "private");
        studentTest1.setAttributeValue("extra curriculars", "strong");
        studentTest1.setAttributeValue("ACT score", "low");
        studentTest1.setAttributeValue("outcome", " reject");
        // TODO: make your own assertions based on the expected classifications
        Assert.assertEquals("reject", testGenerator.getDecision(studentTest1));
    }

    @Test
    public void testIDatasetMethods() {
        String FRUITS_PATH = "/Users/Luke/Documents/CS200/Projects/decision-tree-" +
                "aong9-lriley2/data/fruits-and-vegetables.csv";
        List<Row> dataObjects1 = DecisionTreeCSVParser.parse(FRUITS_PATH);
        List<String> attributeList1 = new ArrayList<>(dataObjects1.get(0).getAttributes());
        Dataset fruitData = new Dataset(attributeList1,dataObjects1);
        Assert.assertEquals(attributeList1,fruitData.getAttributeList());
        Assert.assertEquals(dataObjects1,fruitData.getDataObjects());
        Assert.assertEquals(7,fruitData.size());


    }
}

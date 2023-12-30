package sol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import src.DecisionTreeCSVParser;
import src.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DecisionTreeTest {
    
    @Test
    public void trueIsTrue() {
        assertEquals(true, true);
    }

    /**
     * tests IDatasetMethods
     */
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


    /**
     * tests ITreeGenerator Methods with fruits-and-vegetables.csv dataset
     */
    @Test
    public void testFruitData() {
        String FRUITS_PATH = "/Users/Luke/Documents/CS200/Projects/" +
                "decision-tree-aong9-lriley2/data/fruits-and-vegetables.csv";
        List<Row> dataObjects1 = DecisionTreeCSVParser.parse(FRUITS_PATH);
        List<String> attributeList1 = new ArrayList<>(dataObjects1.get(0).getAttributes());
        Dataset fruitData = new Dataset(attributeList1,dataObjects1);

        TreeGenerator tg = new TreeGenerator();
        tg.generateTree(fruitData,"foodType");
        System.out.println(tg.toString());
        //test on known training data: 100% success rate
        for (Row currentRow : dataObjects1){
            Assert.assertEquals(currentRow.getAttributeValue("foodType"),tg.getDecision(currentRow));
        }

        //unknown value: red pepper
        HashMap MValues = new HashMap();
        MValues.put("highProtein","false");
        MValues.put("calories","low");
        MValues.put("color","red");
        MValues.put("foodType","vegetable");
        Row pepper = new Row(MValues);

        //unknown value: green apple
        HashMap AValues = new HashMap();
        AValues.put("highProtein","false");
        AValues.put("calories","high");
        AValues.put("color","green");
        AValues.put("foodType","fruit");
        Row apple = new Row(AValues);
        int successCount = 0;
        for (int i=0; i<100;i++) {
            tg.generateTree(fruitData,"foodType");
            if (apple.getAttributeValue("foodType").equals(tg.getDecision(apple))) {
                successCount++;
            }
            if (pepper.getAttributeValue("foodType").equals(tg.getDecision(pepper))) {
                successCount++;
            }
        }
        System.out.println(successCount);
        Assert.assertTrue(successCount >= 140);//70% success rate
    }

    /**
     * tests ITreeGenerator methods with our own heights.csv dataset
     */
    @Test
    public void testHeightData() {
        String HEIGHTS_PATH = "/Users/Luke/Documents/CS200/Projects/" +
                "decision-tree-aong9-lriley2/data/heights.csv";
        List<Row> dataObjects1 = DecisionTreeCSVParser.parse(HEIGHTS_PATH);
        List<String> attributeList1 = new ArrayList<>(dataObjects1.get(0).getAttributes());
        Dataset heightData = new Dataset(attributeList1,dataObjects1);

        TreeGenerator tg = new TreeGenerator();
        tg.generateTree(heightData,"outcome");

        for (Row currentRow : dataObjects1){
            Assert.assertEquals(currentRow.getAttributeValue("outcome"),tg.getDecision(currentRow));
        }

        //test above6Feet false
        HashMap FValue = new HashMap();
        FValue.put("above6Feet","no");
        FValue.put("outcome","short");
        Row notTall = new Row(FValue);
        Assert.assertEquals(notTall.getAttributeValue("outcome"),tg.getDecision(notTall));

        //test above6Feet true
        HashMap TValue = new HashMap();
        TValue.put("above6Feet","yes");
        TValue.put("outcome","tall");
        Row tall = new Row(TValue);
        Assert.assertEquals(tall.getAttributeValue("outcome"),tg.getDecision(tall));
    }


}

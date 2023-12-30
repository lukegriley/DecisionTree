package sol;
import org.junit.Assert;
import org.junit.Test;
import src.DecisionTreeCSVParser;
import src.Row;

import java.util.*;

public class DatasetTest {

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

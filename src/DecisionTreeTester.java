package src;

import sol.Dataset;
import sol.TreeGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DecisionTreeTester<G extends ITreeGenerator<D>, D extends IDataset> {
    // IMPORTANT: for this filepath to work, make sure the project is open as the top-level directory in IntelliJ
    // (See the first yellow information box in the handout testing section for details)
    private static final String DATA_BASE = "data/";
    private static final String LIKE_TO_EAT = "likeToEat";

    // mushrooms dataset
    private static final String IS_POISONOUS = "isPoisonous";
    private static final String MUSHROOMS_BASE = DATA_BASE + "mushrooms/";
    private static final String MUSHROOMS_TRAINING = MUSHROOMS_BASE + "training.csv";
    private static final String MUSHROOMS_TESTING = MUSHROOMS_BASE + "testing.csv";

    // villains dataset
    private static final String IS_VILLAIN = "isVillain";
    private static final String VILLAINS_BASE = DATA_BASE + "villains/";
    private static final String VILLAINS_TRAINING = VILLAINS_BASE + "training.csv";
    private static final String VILLAINS_TESTING = VILLAINS_BASE + "testing.csv";

    // candidates dataset
    private static final String CANDIDATES_BASE = DATA_BASE + "candidates/";
    private static final String CANDIDATES_TRAINING_GENDER_EQUAL =
            CANDIDATES_BASE + "training-gender-equal.csv";
    private static final String CANDIDATES_TRAINING_GENDER_UNEQUAL =
            CANDIDATES_BASE + "training-gender-unequal.csv";
    private static final String CANDIDATES_TRAINING_GENDER_CORRELATED =
            CANDIDATES_BASE + "training-gender-correlated.csv";
    private static final String CANDIDATES_TESTING = CANDIDATES_BASE + "testing.csv";

    // songs dataset
    private static final String IS_POPULAR = "isPopular";
    private static final String SONG_BASE = DATA_BASE + "songs/";
    private static final String SONG_TRAINING = SONG_BASE + "training.csv";
    private static final String SONG_TESTING = SONG_BASE + "testing.csv";

    private Class<G> generatorClass;
    private Class<D> datasetClass;
    private ITreeGenerator<D> generator;

    /**
     * Constructor for a recommender tester.
     *
     * @param datasetClass   the DataTable class
     * @param generatorClass the TreeGenerator class
     */
    public DecisionTreeTester(Class<G> generatorClass, Class<D> datasetClass)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        this.generatorClass = generatorClass;
        this.datasetClass = datasetClass;
        Constructor<G> generatorConstructor = this.generatorClass.getConstructor();
        generatorConstructor.setAccessible(true);
        this.generator = generatorConstructor.newInstance();
    }

    public double getAverageDecisionTreeAccuracy(String trainingDataPath, String testingDataPath,
                                                 String targetAttribute, int numIterations)
            throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {

        D trainingData = makeDataset(trainingDataPath, this.datasetClass);
        D testingData = makeDataset(testingDataPath, this.datasetClass);
        return this.getAverageDecisionTreeAccuracy(trainingData, testingData, targetAttribute,
                numIterations);
    }

    public double getAverageDecisionTreeAccuracy(D trainingData, D testingData,
                                                 String targetAttribute, int numIterations) {

        double[] accuracies = new double[numIterations];
        for (int i = 0; i < numIterations; i++) {
            accuracies[i] = this.getDecisionTreeAccuracy(trainingData, testingData, targetAttribute);
        }
        return this.getMean(accuracies);
    }

    public double getDecisionTreeAccuracy(D trainingData, D testingData, String targetAttribute) {
        this.generator.generateTree(trainingData, targetAttribute);
        return this.getDecisionTreeAccuracy(testingData, targetAttribute);
    }

    public double getDecisionTreeAccuracy(D testingData, String targetAttribute) {
        double numCorrectClassifications = 0;
        for (Row datum : testingData.getDataObjects()) {
            String prediction = this.generator.getDecision(datum);
            if (prediction.equals(datum.getAttributeValue(targetAttribute))) {
                numCorrectClassifications += 1;
            }
        }
        return numCorrectClassifications / testingData.size();
    }

    private double getMean(double[] arr) {
        double sum = 0;
        for (double d : arr) {
            sum += d;
        }
        return sum / arr.length;
    }

    public static <D extends IDataset> D makeDataset(String dataPath, Class<D> datasetClass)
            throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Row> dataList = DecisionTreeCSVParser.parse(dataPath);

        Constructor<D> constructor = datasetClass.getConstructor(List.class, List.class);
        constructor.setAccessible(true);
        return constructor.newInstance(getAttributesFromData(dataList), dataList);
    }

    private static List<String> getAttributesFromData(List<Row> data) {
        Set<String> attributeSet = new HashSet<>();
        for (Row datum : data) {
            attributeSet.addAll(datum.getAttributes());
        }
        return new ArrayList<>(attributeSet);
    }

    public static void main(String[] args) {
        src.DecisionTreeTester<TreeGenerator, Dataset> tester;
        // If you would like to test your accuracy on other data, you may change MUSHROOMS_TRAINING,
        // IS_POISONOUS, and MUSHROOMS_TESTING to a different public static final variable defined above
        try {
            tester = new src.DecisionTreeTester<>(TreeGenerator.class, Dataset.class);
            Dataset trainingData = makeDataset(MUSHROOMS_TRAINING, Dataset.class);
            double accuracy =
                    tester.getDecisionTreeAccuracy(trainingData, trainingData, IS_POISONOUS);
            System.out.println("Accuracy on training data: " + accuracy);

            int NUM_ITERS = 100;
            Dataset testingData = makeDataset(MUSHROOMS_TESTING, Dataset.class);
            accuracy = tester.getAverageDecisionTreeAccuracy(trainingData, testingData, IS_POISONOUS, NUM_ITERS);
            System.out.println("Accuracy on testing data: " + accuracy);

        } catch (InstantiationException | InvocationTargetException
                | NoSuchMethodException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

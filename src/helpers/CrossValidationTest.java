package helpers;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AddClassification;

/**
 * Performs a single run of cross-validation and adds the prediction on the test
 * set to the dataset.
 * 
 * Command-line parameters:
 * <ul>
 * <li>-t filename - the dataset to use</li>
 * <li>-o filename - the output file to store dataset with the predictions in</li>
 * <li>-x int - the number of folds to use</li>
 * <li>-s int - the seed for the random number generator</li>
 * <li>-c int - the class index, "first" and "last" are accepted as well; "last"
 * is used by default</li>
 * <li>-W classifier - classname and options, enclosed by double quotes; the
 * classifier to cross-validate</li>
 * </ul>
 * 
 * Example command-line:
 * 
 * <pre>
 * java CrossValidationAddPrediction -t anneal.arff -c last -o predictions.arff -x 10 -s 1 -W "weka.classifiers.trees.J48 -C 0.25"
 * </pre>
 * 
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CrossValidationTest {

	/**
	 * Performs the cross-validation. See Javadoc of class for information on
	 * command-line parameters.
	 * 
	 * @param args
	 *            the command-line parameters
	 * @throws Excecption
	 *             if something goes wrong
	 */
	public static void main(String[] args) throws Exception {
		// loads data and set class index
		Instances data = DataSource.read(System.getProperty("user.dir")
				+ "/data/Arffs/train.arff");
		System.out.println("Read instances...");
		
		String clsIndex = "last";
		if (clsIndex.length() == 0)
			clsIndex = "last";
		if (clsIndex.equals("first"))
			data.setClassIndex(0);
		else if (clsIndex.equals("last"))
			data.setClassIndex(data.numAttributes() - 1);
		else
			data.setClassIndex(Integer.parseInt(clsIndex) - 1);
		System.out.println("Set classindex : " + data.classIndex());

		RandomForest cls = new RandomForest();
		cls.setNumTrees(3000);
		
		// other options
		int seed = (int) System.currentTimeMillis();
		int folds = data.numInstances();
		// randomize data
		
		Random rand = new Random(seed);
		Instances randData = new Instances(data);
		randData.randomize(rand);
		System.out.println("Randomized data");
		

		if (randData.classAttribute().isNominal()) {
			System.out.println("Class attribute is nominal. Stratifying...");
			randData.stratify(folds);

		}

		// perform cross-validation and add predictions
		Instances predictedData = null;
		Evaluation eval = new Evaluation(randData);
		for (int n = 0; n < folds; n++) {

			System.out.println("Fold #" + (n + 1)
					+ "\n=============\nGetting training data...");
			Instances train = randData.trainCV(folds, n);
			System.out.println("Getting testing data...");
			Instances test = randData.testCV(folds, n);

			// the above code is used by the StratifiedRemoveFolds filter, the
			// code below by the Explorer/Experimenter:

			// build and evaluate classifier
			Classifier clsCopy = Classifier.makeCopy(cls);
			System.out.println("Building classifier...");
			clsCopy.buildClassifier(train);
			System.out.println("Evaluating model...");
			eval.evaluateModel(clsCopy, test);
		
			// add predictions
			AddClassification filter = new AddClassification();
			System.out.println("Creating filter...");
			filter.setClassifier(cls);
			filter.setOutputClassification(true);
			filter.setOutputDistribution(true);
			filter.setOutputErrorFlag(true);
			filter.setInputFormat(train);

			System.out.println("Training classifier...");
			Filter.useFilter(train, filter); // trains the classifier

			System.out.println("Performing predictions on testing data...");
			Instances pred = Filter.useFilter(test, filter); // perform
																// predictions
																// on test set
			if (predictedData == null)
				predictedData = new Instances(pred, 0);
			for (int j = 0; j < pred.numInstances(); j++)
				predictedData.add(pred.instance(j));
		}

		// output evaluation
		System.out.println();
		System.out.println("=== Setup ===");
		System.out.println("Classifier: " + cls.getClass().getName() + " "
				+ Utils.joinOptions(cls.getOptions()));
		System.out.println("Dataset: " + data.relationName());
		System.out.println("Folds: " + folds);
		System.out.println("Seed: " + seed);
		System.out.println();
		System.out.println(eval.toSummaryString("=== " + folds
				+ "-fold Cross-validation ===", true));

		
	}
}

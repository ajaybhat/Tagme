package classifier;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

import com.sun.org.apache.xpath.internal.axes.SelfIteratorNoPredicate;

import de.lmu.ifi.dbs.jfeaturelib.edgeDetector.Kernel;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.RandomForest;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;

/**
 * 
 */
public class Main {
	public static void main(String[] args) throws Exception {
		File train = new File(System.getProperty("user.dir")
				+ "/data/Arffs/train.arff");
		File test = new File(System.getProperty("user.dir")
				+ "/data/Arffs/test.arff");

	String hiddenLayers = "50,15";
		double learningRate = 0.1, momentum = 0.1;
		int epochs = 700;
		
		//classify(train, test, hiddenLayers, learningRate, momentum, epochs);
		 writeArffFile("train", "train", "train");

	}
	// classify instances
	public static void classify(File trainFile, File testFile,
			String hiddenLayers, double learningRate, double momentum,
			int epochs) throws Exception {

		int classes[] = {1, 2, 3, 4, 5};

		Instances train = new Instances(new FileReader(trainFile));
		train.setClassIndex(train.numAttributes() - 1);

		Instances test = new Instances(new BufferedReader(new FileReader(
				testFile)));
		test.setClassIndex(train.numAttributes() - 1);

		System.out.println("Loaded data from arff file.");

		AdaBoostM1 adaBoost = new AdaBoostM1();
		adaBoost.setNumIterations(500);
		adaBoost.setWeightThreshold(100);
		adaBoost.setUseResampling(true);

		System.out.println("Setup AdaBoost.");

		MultilayerPerceptron multiLayerPerceptron = new MultilayerPerceptron();
		multiLayerPerceptron.setHiddenLayers(hiddenLayers);
		multiLayerPerceptron.setLearningRate(learningRate);
		multiLayerPerceptron.setMomentum(momentum);
		multiLayerPerceptron.setTrainingTime(epochs);
		multiLayerPerceptron.setSeed((int) System.currentTimeMillis());
		multiLayerPerceptron.setGUI(true);

		adaBoost.setClassifier(multiLayerPerceptron);

		System.out.println("Setup Multilayer Perceptron");
		System.out.println("Running Back-Propagation Network...");
		adaBoost.buildClassifier(train);

		System.out.println("Training complete. Testing...");
		PrintStream ot = System.out;
		System.setOut(new PrintStream(new File(System.getProperty("user.dir")
				+ "/data/Data/testlabels.txt")));
		for (int i = 0; i < test.numInstances(); i++) {
			Instance current = test.instance(i);
			int clsLabel = (int) adaBoost.classifyInstance(current);
			clsLabel = classes[clsLabel];
			System.out.println(clsLabel);

		}

		System.setOut(ot);
		System.out.println("Testing complete. Writing output to file...");

		writeToValid();
		System.out.println("---------\nDone.");

	}

	@SuppressWarnings("resource")
	static void writeToValid() throws Exception {
		BufferedReader buf1 = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(System.getProperty("user.dir")
						+ "/data/Data/test_imagenames.txt")))), buf2 = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(new File(
								System.getProperty("user.dir")
										+ "/data/Data/testlabels.txt"))));
		PrintStream ot = System.out;
		System.setOut(new PrintStream(new File(System.getProperty("user.dir")
				+ "/data/Data/output.txt")));
		String line1, line2;
		Scanner s1, s2;
		while ((line1 = buf1.readLine()) != null) {
			s1 = new Scanner(line1);
			line2 = buf2.readLine();
			System.out.println(line1 + " " + line2);
		}

		System.setOut(ot);
	}

	@SuppressWarnings("resource")
	static void getAttributes(File file, PrintStream print) throws Exception {
		BufferedReader buf = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));

		String line = buf.readLine();
		Scanner s = new Scanner(line);
		s.useDelimiter(",");
		int count = 1;
		while (s.hasNext()) {
			String t = s.next();
			if (s.hasNext()) {
				double x = Double.parseDouble(t);
				print.println("@attribute feature" + (count++) + " numeric");
			}
		}
	}
	@SuppressWarnings("resource")
	static void writeArffFile(String type, String CSVOP, String arffFileOP)
			throws Exception {
		FeatureGenerator generator = new FeatureGenerator();
		File file = new File(System.getProperty("user.dir") + "/data/Data/"
				+ CSVOP + ".txt");
		generator.getFeatures(type, file);

		PrintStream print = new PrintStream(new File(
				System.getProperty("user.dir") + "/data/Arffs/" + arffFileOP
						+ ".arff"));
		print.println("@relation " + arffFileOP + "\n");
		getAttributes(file, print);
		if (type.equalsIgnoreCase("train"))
			print.println("@attribute class{1,2,3,4,5}\n\n@data");
		else
			print.println("@attribute class{1,2,3,4,5,null}\n\n@data");
		BufferedReader buf = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		String line;
		while ((line = buf.readLine()) != null)
			print.println(line);
	}
}
//
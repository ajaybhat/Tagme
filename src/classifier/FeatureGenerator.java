package classifier;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import features.CentroidBDistanceFeatures;
import features.ColorExtraction;
import features.GaborFeatures;
import features.GrayLevelSizeZoneMatrixFeatures;
import features.HaralickFeatures;
import features.HistogramFeatures;
import features.LuminanceFeatures;
import features.MPEG7LayoutFeatures;
import features.PHOGFeatures;
import features.ReferenceColorSimilarityFeatures;

public class FeatureGenerator {

	HashMap<String, ArrayList<double[]>> featureMap = new HashMap<>();
	void getFeatures(String type, File outputFile) {
		try {
			prepareDataset(type);
			HashMap<String, Integer> classMap = getClassOfImage();
			System.out.println("Writing to file...");
			writeToFile(classMap, outputFile);
			System.out.println("--------\nDone.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void writeToFile(HashMap<String, Integer> classMap, File file) {
		// for (String key : featureMap.keySet())
		// System.out.println(key);

		PrintStream fstream = null;
		try {
			fstream = new PrintStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (String key : featureMap.keySet()) {
			for (double[] dkey : featureMap.get(key)) {
				String line = Arrays.toString(dkey);

				line = line.replace('[', ' ');
				line = line.replace(']', ' ');
				line = line.replace(" ", "");

				fstream.print(line + ",");
			}
			fstream.println(classMap.get(key));
		}
		featureMap = new HashMap<>();
	}
	void prepareDataset(String type) throws IOException {
		System.out.println("Preparing dataset...");
		int count = 0;
		File curdir = new File(System.getProperty("user.dir") + "/data/Images/"
				+ type);
		File[] filesList = curdir.listFiles();

		for (File f : filesList) {
			ArrayList<double[]> list = new ArrayList<>();
			count++;
			System.out.println("Generating features for Image #" + count);

			list.add(new HaralickFeatures(f).getFeatures());
			list.add(new GaborFeatures(f).getFeatures());
			list.add(new LuminanceFeatures(f).getFeatures());
			list.add(new HistogramFeatures(f).getFeatures());
			list.add(new MPEG7LayoutFeatures(f).getFeatures());
			// list.add(new ReferenceColorSimilarityFeatures(f).getFeatures());
			list.add(new CentroidBDistanceFeatures(f).getFeatures());
			// list.add(new ColorExtraction(f).getFeatures());
			list.add(new PHOGFeatures(f).getFeatures());
			list.add(new GrayLevelSizeZoneMatrixFeatures(f).getFeatures());

			featureMap.put(f.getName(), list);
		}

		System.out.println("----------\nDone");
	}

	@SuppressWarnings("resource")
	HashMap<String, Integer> getClassOfImage() {
		HashMap<String, Integer> classMap = new HashMap<>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(System.getProperty("user.dir")
							+ "/data/Images/labels.txt"))));
			String line;
			while ((line = reader.readLine()) != null) {

				Scanner s = new Scanner(line);
				s.useDelimiter(" ");
				String img = s.next();
				int classOfImage = s.nextInt();

				classMap.put(img, classOfImage);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classMap;
	}

}

package features;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 */
public class GaborFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	public GaborFeatures(File file) throws IOException {
		super(file);
		features = new double[0];
		run();
	}

	void run() {
		MyGabor gabor = new MyGabor();

		for (int i = 1; i < 3; i++) {
			for (int j = 1; j < 4; j++) {
				gabor.setMasks(4, i);
				gabor.setOrientation(5, j);
				gabor.run(image);
			}
		}
		ArrayList<Double> gaborList = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			double gaborFeature[] = gabor.getFeatures().get(i);
			for (int j = 0; j < gaborFeature.length; j++) {
				gaborList.add(gaborFeature[j]);
			}
		}
		features = new double[gaborList.size()];
		for (int i = 0; i < features.length; i++) {
			features[i] = gaborList.get(i);
		}
	}

}

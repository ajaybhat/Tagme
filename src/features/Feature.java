package features;

import ij.process.ColorProcessor;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Abstract class for features
 */
public class Feature {
	double[] features;
	File imageFile;
	ColorProcessor image;

	public double[] getFeatures() {
		return features;
	}
	public Feature(File file) throws IOException {
		imageFile = file;
		image = new ColorProcessor(ImageIO.read(imageFile));
	}

}

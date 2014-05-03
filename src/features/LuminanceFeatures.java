package features;

import ij.process.ColorProcessor;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.lmu.ifi.dbs.jfeaturelib.features.LuminanceLayout;

/**
 * 
 */
public class LuminanceFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	public LuminanceFeatures(File file) throws IOException {
		super(file);
		run();
	}

	void run() {
		LuminanceLayout luminance = new LuminanceLayout();
		luminance.run(image);
		features = luminance.getFeatures().get(0);
	}
}

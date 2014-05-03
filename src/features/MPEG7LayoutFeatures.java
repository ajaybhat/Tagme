package features;

import ij.process.ColorProcessor;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.lmu.ifi.dbs.jfeaturelib.features.MPEG7ColorLayout;

/**
 * 
 */
public class MPEG7LayoutFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	public MPEG7LayoutFeatures(File file) throws IOException {
		super(file);
		run();
	}
	void run() {
		MPEG7ColorLayout mpegLayout = new MPEG7ColorLayout();
		mpegLayout.run(image);
		features = mpegLayout.getFeatures().get(0);
	}
}

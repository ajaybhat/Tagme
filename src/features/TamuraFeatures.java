package features;

import ij.process.ColorProcessor;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.lmu.ifi.dbs.jfeaturelib.features.Tamura;

/**
 * 
 */
public class TamuraFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	public TamuraFeatures(File file) throws IOException {
		super(file);
		run();
	}

	void run() {
		Tamura tamura = new Tamura();
		tamura.run(image);
		features = tamura.getFeatures().get(0);

	}
}

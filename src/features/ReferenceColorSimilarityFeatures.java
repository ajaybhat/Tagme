package features;

import ij.process.ColorProcessor;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.lmu.ifi.dbs.jfeaturelib.features.ReferenceColorSimilarity;

/**
 * 
 */
public class ReferenceColorSimilarityFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	public ReferenceColorSimilarityFeatures(File file) throws IOException {
		super(file);
		run();
	}

	void run() {
		ReferenceColorSimilarity ref = new ReferenceColorSimilarity();
		ref.run(image);
		features = ref.getFeatures().get(0);

	}

}

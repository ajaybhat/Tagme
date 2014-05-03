package features;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import de.lmu.ifi.dbs.jfeaturelib.features.Haralick;
import de.lmu.ifi.dbs.jfeaturelib.features.PHOG;

/**
 * 
 */
public class HaralickFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	public HaralickFeatures(File file) throws IOException {
		super(file);
		run();
	}

	void run() throws IOException {
		Haralick haralick = new Haralick();
		haralick.run(image);
		features = haralick.getFeatures().get(0);
	}
}

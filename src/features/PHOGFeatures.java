package features;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.lmu.ifi.dbs.jfeaturelib.features.Haralick;
import de.lmu.ifi.dbs.jfeaturelib.features.PHOG;

/**
 * 
 */
public class PHOGFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	public PHOGFeatures(File file) throws IOException {
		super(file);
		run();
	}

	void run() throws IOException {
		PHOG phog = new PHOG();;
			phog.run(image);
			features = phog.getFeatures().get(0);
	}
}

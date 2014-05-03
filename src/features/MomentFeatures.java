package features;

import java.io.File;
import java.io.IOException;

import de.lmu.ifi.dbs.jfeaturelib.features.Moments;

/**
 * 
 */
public class MomentFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	/**
	 * 
	 */
	public MomentFeatures(File file) throws IOException {
		super(file);
		run();
	}

	void run() {
		Moments moments = new Moments();

		moments.run(image);
		features = moments.getFeatures().get(0);
	}

}

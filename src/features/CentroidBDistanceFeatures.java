package features;

import ij.process.ColorProcessor;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.CentroidBoundaryDistance;

/**
 * 
 */
public class CentroidBDistanceFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	public CentroidBDistanceFeatures(File file) throws IOException {
		super(file);
		run();
	}

	void run() {
		CentroidBoundaryDistance centroidBoundaryDistance = new CentroidBoundaryDistance();
		centroidBoundaryDistance.run(image);
		features = centroidBoundaryDistance.getFeatures().get(0);

	}

}

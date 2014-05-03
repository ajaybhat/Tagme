package features;

import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

import javax.imageio.ImageIO;

import de.lmu.ifi.dbs.jfeaturelib.Progress;
import de.lmu.ifi.dbs.jfeaturelib.features.AbstractFeatureDescriptor;

/**
 * Implementation of a Gabor texture features
 * 
 * @author Marko Keuschnig & Christian Penz
 */
public class MyGabor extends AbstractFeatureDescriptor {

	private final features.Gabor gabor;

	public MyGabor() {
		gabor = new features.Gabor();
	}

	@Override
	public void run(ImageProcessor ip) {
		firePropertyChange(Progress.START);
		gabor.extract(ip.getBufferedImage());
		addData(gabor.getDoubleHistogram());
		firePropertyChange(Progress.END);
	}

	@Override
	public String getDescription() {
		return gabor.getStringRepresentation();
	}
	/**
	 * 
	 * @param s
	 *            default = 4
	 * @param t
	 *            default = 4
	 */
	public void setMasks(int s, int t) {
		gabor.setMasks(s, t);
	}

	/**
	 * 
	 * @param m
	 *            default = 5
	 * @param n
	 *            default = 6
	 */
	public void setOrientation(int m, int n) {
		gabor.setOrientation(m, n);
	}
	@Override
	public EnumSet<Supports> supports() {
		EnumSet set = EnumSet.of(Supports.NoChanges, Supports.DOES_8C,
				Supports.DOES_8G, Supports.DOES_RGB);
		// set.addAll(DOES_ALL);
		return set;
	}/*
	public static void main(String[] args) throws IOException {
		MyGabor gabor = new MyGabor();
		gabor.setMasks(1, 1);
		gabor.setOrientation(5, 2);
		ColorProcessor image = new ColorProcessor(ImageIO.read(new File("test_img.jpg")));
		gabor.run(image);
		System.out.println(Arrays.toString(gabor.getFeatures().get(0)));
	}*/
}
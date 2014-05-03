package features;
import ij.process.ColorProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.imageanalysis.FuzzyColorHistogram;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

import de.lmu.ifi.dbs.jfeaturelib.features.AutoColorCorrelogram;
import de.lmu.ifi.dbs.jfeaturelib.features.FuzzyOpponentHistogram;

/**
 * 
 */
public class HistogramFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	public HistogramFeatures(File file) throws IOException {
		super(file);
		features = new double[3];
		run();
	}

	final double LUMINANCE_RED = 0.299D;
	final double LUMINANCE_GREEN = 0.587D;
	final double LUMINANCE_BLUE = 0.114D;
	final int HIST_WIDTH = 256;
	final int HIST_HEIGHT = 100;
	final int HIST_5_PCT = 10;

	void run() {
		double[] histogram = null;
		try {
			histogram = buildHistogram(image.getBufferedImage());
		} catch (Exception e) {

			e.printStackTrace();
		}
		features[0] = getPct05Pc(histogram);
		features[1] = getPct2Pk(histogram);
		features[2] = getAbsDiff(histogram);
	}

	/**
	 * Parses pixels out of an image file, converts the RGB values to its
	 * equivalent grayscale value (0-255), then constructs a histogram of the
	 * percentage of counts of grayscale values.
	 * 
	 * @param infile
	 *            - the image file.
	 * @return - a histogram of grayscale percentage counts.
	 * @throws Exception
	 */

	double[] buildHistogram(BufferedImage input) throws Exception {

		int width = input.getWidth();
		int height = input.getHeight();
		Bag<Integer> graylevels = new HashBag<Integer>();
		double maxWidth = 0.0D;
		double maxHeight = 0.0D;
		for (int row = 0; row < width; row++) {
			for (int col = 0; col < height; col++) {
				Color c = new Color(input.getRGB(row, col));
				int graylevel = (int) (LUMINANCE_RED * c.getRed()
						+ LUMINANCE_GREEN * c.getGreen() + LUMINANCE_BLUE
						* c.getBlue());
				graylevels.add(graylevel);
				maxHeight++;
				if (graylevel > maxWidth) {
					maxWidth = graylevel;
				}
			}
		}

		double[] histogram = new double[HIST_WIDTH];
		for (Integer graylevel : graylevels.uniqueSet()) {
			int idx = graylevel;
			histogram[idx] += graylevels.getCount(graylevel) * HIST_HEIGHT
					/ maxHeight;
		}
		return histogram;
	}

	double getPct05Pc(double[] histogram) {
		double numBins = 0.0D;
		for (int gl = 0; gl < histogram.length; gl++) {
			if (histogram[gl] > 0.5D) {
				numBins++;
			}
		}
		return numBins * 100 / HIST_WIDTH;
	}

	double getPct2Pk(double[] histogram) {
		double pct2pk = 0.0D;
		// find the maximum entry (first peak)
		int maxima1 = getMaxima(histogram, new int[][]{{0, histogram.length}});
		// navigate left until an inflection point is reached
		int lminima1 = getMinima(histogram, new int[]{maxima1, 0});
		int rminima1 = getMinima(histogram,
				new int[]{maxima1, histogram.length});
		for (int gl = lminima1; gl <= rminima1; gl++) {
			pct2pk += histogram[gl];
		}
		// find the second peak
		int maxima2 = getMaxima(histogram, new int[][]{{0, lminima1 - 1},
				{rminima1 + 1, histogram.length}});
		int lminima2 = 0;
		int rminima2 = 0;
		if (maxima2 > maxima1) {
			// new maxima is to the right of previous on
			lminima2 = getMinima(histogram, new int[]{maxima2, rminima1 + 1});
			rminima2 = getMinima(histogram,
					new int[]{maxima2, histogram.length});
		} else {
			// new maxima is to the left of previous one
			lminima2 = getMinima(histogram, new int[]{maxima2, 0});
			rminima2 = getMinima(histogram, new int[]{maxima2, lminima1 - 1});
		}
		for (int gl = lminima2; gl < rminima2; gl++) {
			pct2pk += histogram[gl];
		}
		return pct2pk;
	}

	double getAbsDiff(double[] histogram) {
		double absdiff = 0.0D;
		int diffSteps = 0;
		for (int i = 1; i < histogram.length; i++) {
			if (histogram[i - 1] != histogram[i]) {
				absdiff += Math.abs(histogram[i] - histogram[i - 1]);
				diffSteps++;
			}
		}
		return absdiff / diffSteps;
	}

	private int getMaxima(double[] histogram, int[][] ranges) {
		int maxima = 0;
		double maxY = 0.0D;
		for (int i = 0; i < ranges.length; i++) {
			for (int gl = ranges[i][0]; gl < ranges[i][1]; gl++) {
				if (histogram[gl] > maxY) {
					maxY = histogram[gl];
					maxima = gl;
				}
			}
		}
		return maxima;
	}

	private int getMinima(double[] histogram, int[] range) {
		int start = range[0];
		int end = range[1];
		if (start == end) {
			return start;
		}
		boolean forward = start < end;
		double prevY = histogram[start];
		double dy = 0.0D;
		double prevDy = 0.0D;
		if (forward) {
			// avoid getting trapped in local minima
			int minlookahead = start + HIST_5_PCT;
			for (int pos = start + 1; pos < end; pos++) {
				dy = histogram[pos] - prevY;
				if (signdiff(dy, prevDy) && pos >= minlookahead) {
					return pos;
				}
				prevY = histogram[pos];
				prevDy = dy;
			}
		} else {
			// avoid getting trapped in local minima
			int minlookbehind = start - HIST_5_PCT;
			for (int pos = start - 1; pos >= end; pos--) {
				dy = histogram[pos] - prevY;
				if (signdiff(dy, prevDy) && pos <= minlookbehind) {
					return pos;
				}
				prevY = histogram[pos];
				prevDy = dy;
			}
		}
		return start;
	}

	private boolean signdiff(double dy, double prevDy) {
		return ((dy < 0.0D && prevDy > 0.0D) || (dy > 0.0 && prevDy < 0.0D));
	}
}

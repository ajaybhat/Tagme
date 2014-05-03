package features;
import ij.process.ColorProcessor;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.imageanalysis.FuzzyColorHistogram;

import de.lmu.ifi.dbs.jfeaturelib.features.AutoColorCorrelogram;
import de.lmu.ifi.dbs.jfeaturelib.features.FuzzyHistogram;
import de.lmu.ifi.dbs.jfeaturelib.features.Gabor;
import de.lmu.ifi.dbs.jfeaturelib.features.JpegCoefficientHistogram;
import de.lmu.ifi.dbs.jfeaturelib.features.MPEG7ColorLayout;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.AdaptiveGridResolution;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.CentroidBoundaryDistance;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.CentroidFeature;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.Compactness;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.Eccentricity;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.PolygonEvolution;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.SquareModelShapeMatrix;

/**
 * 
 */
public class ColorFeatures extends Feature {
	/**
	 * @param file
	 * @throws IOException
	 */
	public ColorFeatures(File file) throws IOException {
		super(file);
		features = new double[5000];
		run();
	}

	void run() {
		ColorModel model = image.getDefaultColorModel();
		IndexColorModel icm = (IndexColorModel) model;

		int vector[][][], cnt = 0;

		// retrieve the lookup tables (maps) for R,G,B
		final int r = 0, g = 1, b = 2;
		int mapSize = icm.getMapSize();
		byte[] Rmap = new byte[mapSize];
		icm.getReds(Rmap);
		byte[] Gmap = new byte[mapSize];
		icm.getGreens(Gmap);
		byte[] Bmap = new byte[mapSize];
		icm.getBlues(Bmap);
		int[] RGB = new int[3];

		for (int i = 0, scale = 6; i < 40; i++) {
			vector = new int[5][5][5];
			for (int j = i * scale; j < (i + 1) * scale; j++) {
				for (int j2 = i * scale; j2 < (i + 1) * scale; j2++) {

					int pixel[] = image.getPixel(j, j2, null);
					RGB[r] = Rmap[pixel[r]];
					RGB[g] = Gmap[pixel[g]];
					RGB[b] = Bmap[pixel[b]];
					float[] HSB = Color.RGBtoHSB(RGB[r], RGB[g], RGB[b], null);
					vector = getByteArrayValues(HSB, vector);
				}
			}
			for (int j = 0; j < 5; j++) {
				for (int j2 = 0; j2 < 5; j2++) {
					for (int k = 0; k < 5; k++) {
						features[cnt++] = vector[j][j2][k];
					}
				}
			}

		}
	}

	int[][][] getByteArrayValues(float HSB[], int vector[][][]) {

		float hue, sat, bri;
		for (double x = 0.2; x <= 1; x += 0.2) {
			for (double y = 0.2; y <= 1; y += 0.2) {
				for (double z = 0.2; z <= 1; z += 0.2) {
					hue = HSB[0];
					sat = HSB[1];
					bri = HSB[2];
					if ((hue >= x - 0.2 && hue <= x)
							&& (sat >= y - 0.2 && sat <= y)
							&& (bri >= z - 0.2 && bri <= z))
						vector[(int) (x * 5 - 1)][(int) (y * 5 - 1)][(int) (z * 5 - 1)] = 1;
				}
			}
		}
		return vector;

	}

}

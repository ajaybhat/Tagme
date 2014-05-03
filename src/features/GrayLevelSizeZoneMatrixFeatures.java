package features;

import imageTiTi.reducer.ColorReducer;
import imageTiTi.reducer.GrayLevelReducer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import measures.rasemotte.Distance;
import measures.rasemotte.DistanceTools;
import patternRecognition.textures.glzm.glolzm.MGlolzm;
import patternRecognition.textures.glzm.glszm.GlszmFeatures;
import patternRecognition.textures.glzm.glszm.MGlszm;

/**
 * @article {ThibaultIJPRAI2013, Author = {Guillaume Thibault and Bernard Fertil
 *          and Claire Navarro and Sandrine Pereira and Pierre Cau and Nicolas
 *          Levy and Jean Sequeira and Jean-Luc Mari}, Journal = {International
 *          Journal of Pattern Recognition and Artificial Intelligence}, Number
 *          = {1}, Title = {Shape and Texture Indexes: Application to Cell
 *          Nuclei Classification}, Volume = {27}, Year = {2013} }
 */
public class GrayLevelSizeZoneMatrixFeatures extends Feature {

	/**
	 * @param file
	 * @throws IOException
	 */
	public GrayLevelSizeZoneMatrixFeatures(File file) throws IOException {
		super(file);
		run();
	}

	void run() {

		BufferedImage bImage = image.convertToByte(false).getBufferedImage();
		ArrayList<Double> featuresList = new ArrayList<>();

		int nbGrayLevel = 32;
		// the number of gray level after reduction. After reduction of gray
		// level, the matrix is more robust to noise.
		int nbSizes = 5;
		// coefficient to reduce the size zone. If it is equal to 1, any
		// reduction is done. This reduction reduces the matrix width and
		// improves slightly performances because zones with close sizes are now
		// in the same case.
		boolean FixedSize = false;
		// variable to know if the matrix must have a fixed size. Empirically, I
		// have never obtained better results with a fixed size, so my advice is
		// to always use this parameter as « false ».

		ColorReducer reducer = new GrayLevelReducer();
		// reduces the number of gray level.
		int ForbiddenValue = 0;
		// The value to not take into account during computations. If it is
		// equal to a negative value, the entire texture is process.
		boolean EightConnex = true;
		// Is the labeling realized with 8-connexity?
		int nbCPU = 1;
		// the number of thread to use for processing.

		GlszmFeatures glszm = new GlszmFeatures();
		glszm.Parameters(nbGrayLevel, nbSizes, FixedSize, reducer, null,
				ForbiddenValue, EightConnex, nbCPU); // Set parameters
		glszm.Compute(bImage, nbCPU); // Fills matrix and computes features.

		for (int i = 0; i < glszm.Features().length; i++) {
			featuresList.add(glszm.Features()[i]);
		}

		MGlszm mglszm = new MGlszm();
		bImage = image.convertToByte(false).getBufferedImage();
		mglszm.Parameters(nbSizes, FixedSize, reducer, null, ForbiddenValue,
				EightConnex); // Set parameters
		mglszm.Compute(bImage, nbCPU);

		for (int i = 0; i < mglszm.Features().length; i++) {
			featuresList.add(mglszm.Features()[i]);
		}

		int nbLength = 1; // the number of length to take into account.
		int nbOrientation = 90;
		// Number of orientation to take into account.
		boolean UseAverageOrientation = true;
		// Use the average orientation in order to compute the orientation of
		// each zone? If true, so the matrix is rotation invariant.
		boolean UseGapToAO = true;
		// Fill the matrix with the gap to the average orientation?
		Distance distance = DistanceTools.CreateMontanariDistance(2, 3);
		// The distance to use for the computation of the diameter. 2 is the
		// dimension and 3 the radius

		MGlolzm mglolzm = new MGlolzm();
		bImage = image.convertToByte(false).getBufferedImage();
		mglolzm.Parameters(nbLength, nbOrientation, FixedSize,
				UseAverageOrientation, UseGapToAO, reducer, null,
				ForbiddenValue, EightConnex, distance);
		mglolzm.Compute(bImage, nbCPU);
		for (int i = 0; i < mglolzm.Features().length; i++) {
			featuresList.add(mglolzm.Features()[i]);
		}

		features = new double[featuresList.size()];
		for (int i = 0; i < featuresList.size(); i++) {
			features[i] = featuresList.get(i);
		}

	}
}

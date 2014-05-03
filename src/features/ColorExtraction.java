package features;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class ColorExtraction extends Feature {

	float[][] R, G, B, H, S, V;
	/**
	 * @param file
	 * @throws IOException
	 */
	public ColorExtraction(File file) throws IOException {
		super(file);
		features = new double[18];
		run();
	}

	void run() {

		RGB_HSV_Decompose();

		double[] a = new double[3];

		a = meanStdSkew(R, image.getHeight(), image.getWidth());
		features[0] = a[0] / 255;
		features[1] = a[1] / 255;
		features[2] = a[2];

		a = meanStdSkew(G, image.getHeight(), image.getWidth());
		features[3] = a[0] / 255;
		features[4] = a[1] / 255;
		features[5] = a[2];

		a = meanStdSkew(B, image.getHeight(), image.getWidth());
		features[6] = a[0] / 255;
		features[7] = a[1] / 255;
		features[8] = a[2];

		a = meanStdSkew(H, image.getHeight(), image.getWidth());
		features[9] = a[0];
		features[10] = a[1];
		features[11] = a[2];

		a = meanStdSkew(S, image.getHeight(), image.getWidth());
		features[12] = a[0];
		features[13] = a[1];
		features[14] = a[2];

		a = meanStdSkew(V, image.getHeight(), image.getWidth());
		features[15] = a[0];
		features[16] = a[1];
		features[17] = a[2];

	}

	void RGB_HSV_Decompose() {

		R = new float[image.getHeight()][image.getWidth()];
		G = new float[image.getHeight()][image.getWidth()];
		B = new float[image.getHeight()][image.getWidth()];
		H = new float[image.getHeight()][image.getWidth()];
		S = new float[image.getHeight()][image.getWidth()];
		V = new float[image.getHeight()][image.getWidth()];
		int rgb[] = new int[3];
		float hsv[] = new float[3];
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				rgb = image.getPixel(i, j, rgb);
				R[i][j] = rgb[0];
				G[i][j] = rgb[1];
				B[i][j] = rgb[2];
				hsv = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsv);
				H[i][j] = hsv[0];
				S[i][j] = hsv[1];
				V[i][j] = hsv[2];
			}
		}
	}

	double[] meanStdSkew(float[][] data, int h, int w) {
		double mean = 0;
		double[] out = new double[3];

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				mean += data[i][j];
			}
		}
		mean /= (h * w);
		out[0] = mean;
		double sum = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				final double v = data[i][j] - mean;
				sum += v * v;
			}
		}
		out[1] = Math.sqrt(sum / (h * w - 1));

		sum = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				final double v = (data[i][j] - mean) / out[1];
				sum += v * v * v;
			}
		}
		out[2] = Math.pow(1 + sum / (h * w - 1), 1. / 3);
		return out;
	}

}
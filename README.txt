===============================================================
============TAGME 2014 IMAGE CLASSIFICATION CONTEST============
===============================================================

Eclipse is needed to run the classifier. You can download Eclipse from http://eclipse.org/

Libraries needed for the classifier (all are included in lib folder):
1. Weka 3.7
2. jFeatureLib
3. Jama and JAI
4. Apache Commons Collections
Steps
=====
1. Extract the contents of the zip file to a working directory say /TagMe. Create a directory /TagMe/data.
2. Inside data, create 3 subdirectories. /TagMe/data/Arffs, /TagMe/data/Data and /TagMe/data/Images. Inside /TagMe/data/Images, create 3 subdirectories. /TagMe/data/Images/train for the training images, /TagMe/data/Images/valid for validation set images, /TagMe/data/Images/test for the test set images. Place training image labels in /TagMe/data/Images.
3. Import the project into Eclipse. See : http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.platform.doc.user%2Ftasks%2Ftasks-importproject.htm
Add the libraries in /TagMe/lib to the build path.
4. The Main.java class in the classifier package is used for the classifier. The main function looks like this:
public static void main(String[] args) throws Exception {
		File train = new File(System.getProperty("user.dir")
				+ "/data/Arffs/train.arff");
		File test = new File(System.getProperty("user.dir")
				+ "/data/Arffs/test.arff");
		String hiddenLayers = "100,35";
		double learningRate = 0.5, momentum = 0.1;
		int epochs = 500;
		// classify(train, test, hiddenLayers, learningRate, momentum, epochs);		-----> line A
		// writeArffFile("train", "train", "train");								-----> line B
	}

5. First uncomment the line B and provide the line as :
	writeArffFile("train", "train", "train");
	and run Main.java for extracting features from the training set. The features are written into /TagMe/data/Arffs/train.arff
   Then edit the line as :
    writeArffFile("test", "test", "test");
	and run Main.java to do the same for the test set. Comment out line B.
In the FeatureGenerator class in classifier package, the writeToFile() method has the code:
void writeToFile(HashMap<String, Integer> classMap, File file) {
		// for (String key : featureMap.keySet())
		// System.out.println(key);

Uncomment the lines to get the test imagenames during test feature extraction. Save these imagenames in a file test_imagenames.txt in /TagMe/data/Data.
6. Uncomment line A. Run Main.java. You can modify the parameters of the MultiLayerPerceptron by changing the values of the arguments passed to it.
7. Uncomment 
	//perceptron.setGUI(true); 
   in the classify() function to bring up a GUI during training of neural network where you can vary the parameters of network.
8. Output is written to /TagMe/data/output.txt.


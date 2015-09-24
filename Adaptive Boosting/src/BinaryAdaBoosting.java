import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


public class BinaryAdaBoosting {
	

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		Scanner sc = new Scanner(new File("adaboost-5.txt"));
		int iterations = sc.nextInt();
		int NoOfSamples = sc.nextInt();
		double epsilon = sc.nextDouble();
		float[] x = new float[NoOfSamples];
		int[] y = new int[NoOfSamples];
		double[] probs = new double[NoOfSamples];
		Integer max = Integer.MIN_VALUE;
		FileOutputStream fout = new FileOutputStream("binary_5.txt");
		PrintStream pw = new PrintStream(fout);
		System.setOut(pw);
		double bound = 1;
		double[] f1 = new double[NoOfSamples];
		String boostedClassifier = "";
		for(int i = 0;i<NoOfSamples;i++){
			x[i] = sc.nextFloat();
		}
		
		for(int i = 0;i<NoOfSamples;i++){
			y[i] = sc.nextInt();
		}
		
		for(int i = 0;i<NoOfSamples;i++){
			probs[i] = sc.nextDouble();
		}
		
		ArrayList<double[]> thresholdsForF = new ArrayList<double[]>();
		
		for(int j = 1 ; j <= iterations; j++){
			
			System.out.println("Iteration "+j);
			double[] thresholdArray = new double[2];
			thresholdArray = calculateThreshold(x,y,probs,NoOfSamples);
			thresholdsForF.add(thresholdArray);
			double[] f2 = new double[NoOfSamples];
			if(thresholdArray[0] == 1){
				System.out.println("The selected weak classifier Ht:x < " + thresholdArray[1]);
			}
			else{
				System.out.println("The selected weak classifier Ht:x > " + thresholdArray[1]);
			}
			for(int k = 0 ;k < f2.length; k++){
				if(thresholdArray[0] == 1){
					
					if(x[k] <= thresholdArray[1]){
						f2[k] = 1;
					}
					else{
						f2[k] = -1;
					}
				}
				else{
					
					if(x[k] <= thresholdArray[1]){
						f2[k] = -1;
					}
					else{
						f2[k] = 1;
					}
				}
			}
			System.out.println("The error of Ht: "+thresholdArray[2]);
			
			double alpha = 0.50 * (Math.log((1-thresholdArray[2])/(double)thresholdArray[2]));
			
			System.out.println("The weight of Ht: "+alpha);
			
			for(int k = 0 ;k < f2.length; k++){
				f2[k] = f2[k] * alpha;
			}
			
			for(int k = 0 ;k < f2.length; k++){
				f1[k] = f1[k] + f2[k];
			}
			
			double qiForRight = 1/(Math.exp(alpha));
			double qiForWrong = Math.exp(alpha);
			
			double normalizationFactor = normalizationFactor(x,y,probs,NoOfSamples,qiForRight,qiForWrong,thresholdArray);
			
			System.out.println("The probabilities normalization factor Zt: "+normalizationFactor);
			System.out.print("The probabilities after normalization: ");
			for(int i = 0; i < probs.length;i++){
				probs[i] = probs[i]/normalizationFactor;
				
				System.out.print(probs[i]+" ");
			}
			System.out.println();
			if(j != 1){
				boostedClassifier += " + ";
			}
			
			if(thresholdArray[0] == 1){
				boostedClassifier += alpha+" * I(x < "+thresholdArray[1]+" ) ";
			}
			else{
				boostedClassifier += alpha+" * I(x > "+thresholdArray[1]+" ) ";
			}
			System.out.println("The boosted classifier: "+boostedClassifier);
			double finalError = 0;
			
			double[] classificationArray = new double[f2.length];
			for(int i = 0 ; i< f1.length;i++){
				if(f1[i] <= 0){
					classificationArray[i] = -1;
				}
				else{
					classificationArray[i] = 1;
				}
			}
			for(int i = 0 ; i< classificationArray.length;i++){
				if(classificationArray[i] != y[i]){
					finalError++;
				}
			}
			finalError = finalError / NoOfSamples;
			System.out.println("The error of the boosted classifier: " + finalError);
			bound = bound  * normalizationFactor;
			System.out.println("The bound on Et: " + bound);
			System.out.println();
		}
		
		
	}
	
	public static double normalizationFactor(float[] x,int[] y, double[] probs,int NoOfSamples,double qiForRight,double qiForWrong,double[] thresholdArray){
		
		if(thresholdArray[0] == 1){
			
			for(int i = 0; i < NoOfSamples; i++ ){
				if(x[i] <= thresholdArray[1]){
					if(y[i] == -1){
						probs[i] = probs[i] * qiForWrong;
					}
					else{
						probs[i] = probs[i] * qiForRight;
					}
					
				}
				if(x[i] > thresholdArray[1]){
					if(y[i] == 1){
						probs[i] = probs[i] * qiForWrong;;
					}
					else{
						probs[i] = probs[i] * qiForRight;
					}
				}
				
				}
		}
		else{
			
			for(int i = 0; i < NoOfSamples; i++ ){ 
				if(x[i] <= thresholdArray[1]){
					if(y[i] == 1){
						probs[i] = probs[i] * qiForWrong;
					}
					else{
						probs[i] = probs[i] * qiForRight;
					}
					
				}
				if(x[i] > thresholdArray[1]){
					if(y[i] == -1){
						probs[i] = probs[i] * qiForWrong;;
					}
					else{
						probs[i] = probs[i] * qiForRight;
					}
				}
				
				}
			
		}
		
		double normalizationFactor = 0;
		for(int i = 0; i < probs.length;i++){
			normalizationFactor = normalizationFactor + probs[i];
		}
		
		
		return normalizationFactor;
	}
	
	
	public static double[] calculateThreshold(float[] x,int[] y, double[] probs,int NoOfSamples) throws FileNotFoundException{
		
		ArrayList<Double> thresholds = new ArrayList<Double>();
		double[] array  = new double[2];
		double errorProbsFinal = 10000;
		int errorProbsIndex = 0;
		int greaterOrSmallerFinal = 0;
		double[] thresholdArray = new double[3];
		
		for(int i = 0; i<x.length-1;i++){
			if(i == 0){
				thresholds.add(x[i] - 1.0);
			}
			if(y[i] != y[i+1]){
				thresholds.add((double) ((x[i]+x[i+1])/2.0));
			}
		}
		
		thresholds.add(x[NoOfSamples-1] + 1.0);
		for(int i = 0;i<thresholds.size();i++){
			array = calculatingErrorProbs(x,y,probs,thresholds.get(i),NoOfSamples);
			
			if(array[1] < errorProbsFinal){
				errorProbsFinal = array[1];
				errorProbsIndex = i;
				greaterOrSmallerFinal = (int) array[0];
			}
		}
		thresholdArray[0] = greaterOrSmallerFinal;
		thresholdArray[1] = thresholds.get(errorProbsIndex);
		
		thresholdArray[2] = errorProbsFinal;
		return thresholdArray;
	}
	
	public static double[] calculatingErrorProbs(float[] x,int[] y,double[] probs,Double threshold,int NoOfSamples){
		
		double errorLess = 0;
		double errorGreater = 0;
		double[] thresholdArray = new double[2];
		for(int i = 0; i<NoOfSamples;i++){
			if(x[i] <= threshold){
				if(y[i] == -1){
					errorLess = errorLess + probs[i] ;
				}
			}
			
			if(x[i] > threshold){
				if(y[i] == 1){
					errorLess = errorLess + probs[i];
				}
			}
			
			if(x[i] <= threshold){
				if(y[i] == 1){
					errorGreater = errorGreater + probs[i];
				}
			}
			
			if(x[i] > threshold){
				if(y[i] == -1){
					errorGreater = errorGreater + probs[i];
				}
			}
			
		}
		
		if(errorLess <= errorGreater){
			thresholdArray[0] = 1;
			thresholdArray[1] = errorLess;
		}
		else{
			thresholdArray[0] = 0;
			thresholdArray[1] = errorGreater;
		}
		
		return thresholdArray;
	}
	

}

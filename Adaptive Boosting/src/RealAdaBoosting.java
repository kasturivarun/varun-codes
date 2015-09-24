import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


public class RealAdaBoosting {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		Scanner sc = new Scanner(new File("file2.txt"));
		int iterations = sc.nextInt();
		int NoOfSamples = sc.nextInt();
		double epsilon = sc.nextDouble();
		float[] x = new float[NoOfSamples];
		int[] y = new int[NoOfSamples];
		double[] probs = new double[NoOfSamples];
		double[] ft = new double[NoOfSamples];
		double bound = 1;
		FileOutputStream fout = new FileOutputStream("Real.txt");
		PrintStream pw = new PrintStream(fout);
		System.setOut(pw);
		for(int i = 0;i<NoOfSamples;i++){
			x[i] = sc.nextFloat();
		}
		
		for(int i = 0;i<NoOfSamples;i++){
			y[i] = sc.nextInt();
		}
		
		for(int i = 0;i<NoOfSamples;i++){
			probs[i] = sc.nextDouble();
		}
		
		for(int j = 1 ; j <= iterations; j++){
			
		System.out.println("Iteration "+j);
		double[] thresholdArray = new double[4];
		thresholdArray = calculateThreshold(x,y,probs,NoOfSamples);
		if(thresholdArray[0] == 1){
			System.out.println("The selected weak classifier Ht: x < " + thresholdArray[1]);
		}
		else{
			System.out.println("The selected weak classifier Ht: x > " + thresholdArray[1]);
		}
		double G = 	thresholdArray[3];
		System.out.println("The G error value of Ht: "+G);
		double[] probsForPosNegRightWrong = posNegRightWrongFunction(x,y,probs,NoOfSamples,thresholdArray);
		double G2 = Math.sqrt(probsForPosNegRightWrong[0]*probsForPosNegRightWrong[3]) + Math.sqrt(probsForPosNegRightWrong[1]*probsForPosNegRightWrong[2]);
		double cPositive = 0.5*(Math.log((probsForPosNegRightWrong[0]+epsilon)/(probsForPosNegRightWrong[3]+epsilon)));
		double cNegative = 0.5*(Math.log((probsForPosNegRightWrong[2]+epsilon)/(probsForPosNegRightWrong[1]+epsilon)));
		System.out.println("The weights Ct+, Ct-:"+cPositive+","+cNegative);
		double normalizationFactor = calculateNormFactor(x,y,probs,NoOfSamples,thresholdArray,cPositive,cNegative,ft);
		System.out.println("The probabilities normalization factor Zt: "+normalizationFactor);
		System.out.print("The probabilities after normalization : ");
		for(int i = 0; i < probs.length;i++){
			probs[i] = probs[i]/normalizationFactor;
			System.out.print(probs[i] + " ");
		}
		System.out.println();
		System.out.print("The values ft(xi) for each one of the examples: ");
		for(int i = 0; i < ft.length;i++){
			System.out.print(ft[i]+" ");
		}
		System.out.println();
		double finalError = 0;
		double[] classificationArray = new double[ft.length];
		for(int i = 0 ; i< ft.length;i++){
			if(ft[i] <= 0){
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
		System.out.println("The error of the boosted classifier Et : " + finalError);
		bound = bound * normalizationFactor;
		System.out.println("The bound on Et: "+ bound);
		System.out.println();
		System.out.println();
		}
		}
	
	public static double calculateNormFactor(float[] x,int[] y, double[] probs,int NoOfSamples,double[] thresholdArray,double cPositive,double cNegative,double[] ft){
		double normalizationFactor = 0;
		if(thresholdArray[0] == 1){
			for(int i = 0 ;i < NoOfSamples;i++){
				if(x[i] <= thresholdArray[1]){
					probs[i] = probs[i]/Math.exp(y[i] * cPositive);
					ft[i] = ft[i] + cPositive;
				}
				else{
					probs[i] = probs[i]/Math.exp(y[i] * cNegative);
					ft[i] = ft[i] + cNegative;
				}
			}
		}
		else{
			for(int i = 0 ;i < NoOfSamples;i++){
				if(x[i] <= thresholdArray[1]){
					probs[i] = probs[i]/Math.exp(y[i] * cNegative);
					ft[i] = ft[i] + cNegative;
				}
				else{
					probs[i] = probs[i]/Math.exp(y[i] * cPositive);
					ft[i] = ft[i] + cPositive;
				}
			}
		}
		
		for(int i = 0; i < probs.length;i++){
			normalizationFactor = normalizationFactor + probs[i];
		}
		
		
		return normalizationFactor;
	}
	
	public static double[] posNegRightWrongFunction(float[] x,int[] y, double[] probs,int NoOfSamples,double[] thresholdArray){
		double[] probabilities = new double[4];
		for(int k = 0 ; k < probabilities.length ; k++){
			probabilities[k] = 0;
		}
		if(thresholdArray[0] == 1){
			for(int i = 0; i<NoOfSamples;i++){
			if(x[i] <= thresholdArray[1]){
				if(y[i] == -1){
					probabilities[3] = probabilities[3] + probs[i];
				}
				
				if(y[i] == 1){
					probabilities[0] = probabilities[0] + probs[i];
				}
			}
			if(x[i] > thresholdArray[1]){
				if(y[i] == -1){
					probabilities[1] = probabilities[1] + probs[i];
				}
				
				if(y[i] == 1){
					probabilities[2] = probabilities[2] + probs[i];
				}
			}
			}
			
			return probabilities;
		}
		
		else{
			for(int i = 0; i<NoOfSamples;i++){
				if(x[i] <= thresholdArray[1]){
					if(y[i] == -1){
						probabilities[1] = probabilities[1] + probs[i];
					}
					
					if(y[i] == 1){
						probabilities[2] = probabilities[2] + probs[i];
					}
				}
				if(x[i] > thresholdArray[1]){
					if(y[i] == -1){
						probabilities[3] = probabilities[3] + probs[i];
					}
					
					if(y[i] == 1){
						probabilities[0] = probabilities[0] + probs[i];
					}
				}
				}
				
				return probabilities;
		}
	}
	
public static double[] calculateThreshold(float[] x,int[] y, double[] probs,int NoOfSamples){
		
		ArrayList<Double> thresholds = new ArrayList<Double>();
		double[] array  = new double[2];
		double errorProbsFinal = 10000;
		double gFinal = 10000;
		int errorProbsIndex = 0;
		int greaterOrSmallerFinal = 0;
		double[] thresholdArray = new double[4];
		
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
			
			if(array[2] < gFinal){
				errorProbsFinal = array[1];
				errorProbsIndex = i;
				greaterOrSmallerFinal = (int) array[0];
				gFinal = array[2];
			}
		}
		thresholdArray[0] = greaterOrSmallerFinal;
		thresholdArray[1] = thresholds.get(errorProbsIndex);
		thresholdArray[2] = errorProbsFinal;
		thresholdArray[3] = gFinal;
		return thresholdArray;
	}
	
	public static double[] calculatingErrorProbs(float[] x,int[] y,double[] probs,Double threshold,int NoOfSamples){
		
		double errorLess = 0;
		double errorGreater = 0;
		double[] probabilities1 = new double[4];
		for(int k = 0 ; k < probabilities1.length ; k++){
			probabilities1[k] = 0;
		}
		double[] probabilities2 = new double[4];
		for(int k = 0 ; k < probabilities1.length ; k++){
			probabilities1[k] = 0;
		}
		double[] thresholdArray = new double[3];
		for(int i = 0; i<NoOfSamples;i++){
			if(x[i] <= threshold){
				if(y[i] == -1){
					errorLess = errorLess + probs[i] ;
					probabilities1[3] = probabilities1[3] + probs[i];
					probabilities2[1] = probabilities2[1] + probs[i];
				}
				
				if(y[i] == 1){
					probabilities1[0] = probabilities1[0] + probs[i];
					errorGreater = errorGreater + probs[i];
					probabilities2[2] = probabilities2[2] + probs[i];
				}
			}
			
			if(x[i] > threshold){
				if(y[i] == 1){
					errorLess = errorLess + probs[i];
					probabilities1[2] = probabilities1[2] + probs[i];
					probabilities2[0] = probabilities2[0] + probs[i];
				}
				
				if(y[i] == -1){
					probabilities1[1] = probabilities1[1] + probs[i];
					errorGreater = errorGreater + probs[i];
					probabilities2[3] = probabilities2[3] + probs[i];

				}
			}
			
			
		}
		
		double G1 = Math.sqrt(probabilities1[0]*probabilities1[3]) + Math.sqrt(probabilities1[1]*probabilities1[2]);
		double G2 = Math.sqrt(probabilities2[0]*probabilities2[3]) + Math.sqrt(probabilities2[1]*probabilities2[2]);
		
		if(G1 <= G2){
			thresholdArray[0] = 1;
			thresholdArray[1] = errorLess;
			thresholdArray[2] = G1;

 		}
		else{
			thresholdArray[0] = 0;
			thresholdArray[1] = errorGreater;
			thresholdArray[2] = G2;
		}
		
		return thresholdArray;
	}
	


}

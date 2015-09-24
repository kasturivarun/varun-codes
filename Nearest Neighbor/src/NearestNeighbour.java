import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NearestNeighbour {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Taking the first file as input
		Scanner sc = new Scanner(new File("file1.txt"));
		int foldNumber = sc.nextInt();
		int numOfSamples = sc.nextInt();
		int numOfPerms = sc.nextInt();
		double finalError = 0;
		int arrayPerms[] = new int[numOfSamples];
		int permMatrix[][] = new int[numOfPerms][numOfSamples];// This matrix
																// has all the
																// permutations
																// row by row
		ArrayList<Integer> errorArrays = new ArrayList<Integer>();
		FileOutputStream fout = new FileOutputStream("vxk147830.txt");
		PrintStream pw = new PrintStream(fout);
		System.setOut(pw);

		for (int i = 0; i < numOfPerms; i++) {

			for (int j = 0; j < numOfSamples; j++) {
				permMatrix[i][j] = sc.nextInt();
			}
		}
		// Scanning 2nd file to just get the rows and columns of the given
		// matrix so as to determine the size of final matrix
		Scanner sc3 = new Scanner(new File("file2.txt"));
		int rowsOfTrainingData = sc3.nextInt();
		int columnsOfTrainingData = sc3.nextInt();
		int finalMatrix[][] = new int[rowsOfTrainingData][columnsOfTrainingData];
		ArrayList<int[][]> grids = new ArrayList<int[][]>();
		for (int knn = 1; knn < 6; knn++) {
			for (int i = 0; i < numOfPerms; i++) {
				for (int j = 0; j < numOfSamples; j++) {
					arrayPerms[j] = permMatrix[i][j] + 1;

				}
				// Start function determines the error for each permutation for
				// k nearest neighbours
				// The errors for all the permuations are determined and added
				// to an arraylist
				errorArrays.add(start(numOfSamples, foldNumber, arrayPerms,
						knn, finalMatrix));
				grids.add(finalMatrix);
			}
			// Determining error and variance
			for (int k = 0; k < errorArrays.size(); k++) {
				finalError = finalError
						+ (double) (errorArrays.get(k) / (float) numOfSamples);
			}

			double e = finalError / (double) numOfPerms;
			double variance = 0;

			for (int k = 0; k < errorArrays.size(); k++) {
				variance = variance
						+ ((e - ((double) (errorArrays.get(k) / (float) numOfSamples))) * (e - ((double) (errorArrays
								.get(k) / (float) numOfSamples))));
			}
			// Printing the output on to an Output file
			pw.println("k = " + knn + " e = " + e + " sigma= "
					+ Math.sqrt((double) variance / (float) (numOfPerms - 1)));

			for (int k = 0; k < rowsOfTrainingData; k++) {
				for (int j = 0; j < columnsOfTrainingData; j++) {
					if (finalMatrix[k][j] == 1) {

						pw.print("+" + " ");
					}
					if (finalMatrix[k][j] == 0) {

						pw.print("-" + " ");
					}
				}
				pw.println();
			}

			// Clearing all the variables for next iteration
			pw.println();
			grids.clear();
			errorArrays.clear();
			finalError = 0;
			variance = 0;
		}
		sc.close();
		sc3.close();
	}

	public static int start(int numOfSamples, int foldNumber, int[] arrayPerms,
			int knn, int[][] finalMatrix) throws FileNotFoundException {

		Scanner sc2 = new Scanner(new File("file2.txt"));
		int rowsOfTrainingData = sc2.nextInt();
		int columnsOfTrainingData = sc2.nextInt();
		// Scanning the second input file
		// And storing the x1 x2 values separately for '.' and '+,-'
		// Storing the + and - classification in y
		int x1[] = new int[numOfSamples];
		int x2[] = new int[numOfSamples];
		int x1ForDot[] = new int[(rowsOfTrainingData * columnsOfTrainingData)
				- numOfSamples];
		int x2ForDot[] = new int[(rowsOfTrainingData * columnsOfTrainingData)
				- numOfSamples];
		int y[] = new int[numOfSamples];
		// Variables for implementing logic
		int count1 = 0;
		int countForDot = 0;

		String[][] givenData = new String[rowsOfTrainingData][columnsOfTrainingData];

		for (int i = 0; i < rowsOfTrainingData; i++) {

			for (int j = 0; j < columnsOfTrainingData; j++) {

				givenData[i][j] = sc2.next();

				if (Character.valueOf(givenData[i][j].charAt(0)).equals('+')) {
					x1[count1] = j;
					x2[count1] = i;
					y[count1] = 1;
					count1++;
				}
				if (Character.valueOf(givenData[i][j].charAt(0)).equals('-')) {
					x1[count1] = j;
					x2[count1] = i;
					y[count1] = 0;
					count1++;
				}
				if (Character.valueOf(givenData[i][j].charAt(0)).equals('.')) {
					x1ForDot[countForDot] = j;
					x2ForDot[countForDot] = i;
					countForDot++;
				}

			}
		}

		// Partition logic start

		int minNumberInFold = numOfSamples / foldNumber;

		int extraNumberInFold = numOfSamples % foldNumber;

		int count2 = numOfSamples - 1;
		int count3 = 0;
		boolean flag = true;
		if (extraNumberInFold != 0) {
			count3 = minNumberInFold;
		} else {
			count3 = minNumberInFold - 1;
			flag = false;
		}
		int[][] partitions = new int[foldNumber][minNumberInFold + 1];

		for (int i = foldNumber - 1; i >= 0; i--) {
			for (int j = count3; j >= 0; j--) {
				if (extraNumberInFold != 0) {
					partitions[i][j] = arrayPerms[count2];
					count2--;
				}

				if (extraNumberInFold == 0) {
					partitions[i][j] = arrayPerms[count2];
					count2--;
				}
			}
			if (extraNumberInFold != 0) {
				extraNumberInFold--;
			}

			if (extraNumberInFold == 0 && flag == true) {
				count3--;
				flag = false;
			}
		}

		// Partition logics ends here
		// Now the partition matrix has the each partition row by row

		// Calculating distance for given data start here
		ArrayList<HashMap<Integer, Float>> distancesOfIndices = new ArrayList<HashMap<Integer, Float>>();
		ArrayList<Integer> sortedKeys2 = new ArrayList<Integer>();

		for (int i = 0; i < foldNumber; i++) {

			for (int l = 0; l < minNumberInFold + 1; l++) {

				if (partitions[i][l] != 0) {
					Map<Integer, Float> distancesMap = new HashMap<Integer, Float>();

					for (int j = 0; j < foldNumber; j++) {

						for (int k = 0; k < minNumberInFold + 1; k++) {

							if (i != j && partitions[j][k] != 0) {

								distancesMap.put(
										partitions[j][k],
										(float) distanceBtwTwoPoints(
												x1[partitions[i][l] - 1],
												x2[partitions[i][l] - 1],
												x1[partitions[j][k] - 1],
												x2[partitions[j][k] - 1]));

							}
						}

					}

					distancesOfIndices
							.add((HashMap<Integer, Float>) distancesMap);
				}

			}

		}

		// Calculating distance for given data ends here
		// I used a hashmap for storing the destination index as key and
		// distance to that index as value
		// Hashmap for each sample is stored in an arraylist

		// Distance MAP for dots
		ArrayList<HashMap<Integer, Float>> distancesOfIndicesForDots = new ArrayList<HashMap<Integer, Float>>();
		for (int i = 0; i < x1ForDot.length; i++) {
			Map<Integer, Float> distancesMap2 = new HashMap<Integer, Float>();
			for (int j = 0; j < x1.length; j++) {
				distancesMap2.put(
						j + 1,
						(float) distanceBtwTwoPoints(x1ForDot[i], x2ForDot[i],
								x1[j], x2[j]));
			}
			distancesOfIndicesForDots
					.add((HashMap<Integer, Float>) distancesMap2);
		}

		// Similar procedure is used for storing distances of all dots to all
		// different indices

		// Nearest neighbor for the samples start here
		int error = 0;
		int estimate = 0;

		int counter = 0;
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		ArrayList<Integer> positives = new ArrayList<Integer>();
		ArrayList<Integer> negatives = new ArrayList<Integer>();

		for (int i = 0; i < numOfSamples; i++) {

			finalMatrix[x2[i]][x1[i]] = y[i];

		}

		for (int i = 0; i < foldNumber; i++) {

			for (int j = 0; j < minNumberInFold + 1; j++) {
				if (partitions[i][j] != 0) {
					sortedKeys2 = sortHashMapByValue(distancesOfIndices
							.get(counter));
					counter++;
					for (int k = 0; k < Math.min(knn, sortedKeys2.size()); k++) {
						if (sortedKeys2.get(k) != null) {
							neighbours.add(sortedKeys2.get(k));
						}
					}

					for (int l = 0; l < neighbours.size(); l++) {
						if (y[neighbours.get(l) - 1] == 0) {
							negatives.add(neighbours.get(l));
						} else {
							positives.add(neighbours.get(l));
						}
					}

					if (positives.size() > negatives.size()) {
						estimate = 1;
					} else if (positives.size() < negatives.size()) {
						estimate = 0;
					} else {
						estimate = 0;
					}

					if (y[partitions[i][j] - 1] != estimate) {
						error++;
					}
					neighbours.clear();
					positives.clear();
					negatives.clear();
				}
			}
		}
		// Nearest neighbor for the samples ends here

		// Nearest neighbor for the dots starts here

		for (int j = 0; j < distancesOfIndicesForDots.size(); j++) {
			sortedKeys2 = sortHashMapByValue(distancesOfIndicesForDots.get(j));
			for (int k = 0; k < Math.min(knn, sortedKeys2.size()); k++) {
				if (sortedKeys2.get(k) != null) {
					neighbours.add(sortedKeys2.get(k));
				}
			}

			for (int l = 0; l < neighbours.size(); l++) {
				if (y[neighbours.get(l) - 1] == 0) {
					negatives.add(neighbours.get(l));
				} else {
					positives.add(neighbours.get(l));
				}
			}

			if (positives.size() > negatives.size()) {
				estimate = 1;
			} else if (positives.size() < negatives.size()) {
				estimate = 0;
			} else {
				estimate = 0;
			}
			finalMatrix[x2ForDot[j]][x1ForDot[j]] = estimate;

			neighbours.clear();
			positives.clear();
			negatives.clear();
		}

		// Nearest neighbor for the dots ends here

		sc2.close();
		return error;
	}

	// Comparator for sorting the elements in a hashmap by distance as value
	public static ArrayList<Integer> sortHashMapByValue(
			final HashMap<Integer, Float> map) {
		ArrayList<Integer> keys = new ArrayList<Integer>();
		keys.addAll(map.keySet());
		ArrayList<Integer> sortedKeys = new ArrayList<Integer>();
		Collections.sort(keys, new Comparator<Integer>() {

			@Override
			public int compare(Integer arg0, Integer arg1) {
				// TODO Auto-generated method stub
				Float f1 = map.get(arg0);
				Float f2 = map.get(arg1);
				if (f1 == null) {
					return (f2 != null) ? 1 : 0;
				} else if ((f1 != null) && (f2 != null)) {
					return f1.compareTo(f2);
				} else {
					return 0;
				}
			}
		});

		for (Integer key : keys) {
			sortedKeys.add(key);

		}
		return sortedKeys;
	}

	private static double distanceBtwTwoPoints(int x11, int x12, int x21,
			int x22) {

		return Math.sqrt(((x11 - x21) * (x11 - x21))
				+ ((x12 - x22) * (x12 - x22)));
	}

}

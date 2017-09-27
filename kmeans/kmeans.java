package kmeans;

import java.util.ArrayList;

public class kmeans {

	private double[][] dataset;
	private int numOfPoints;
	private int numOfDimension;
	private int numOfClusters;
	private double[][] centroids;
	private double threshold;
	private int[] point;
	
	
	public kmeans(double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
	}
		
	
	
	public void kMeanClustering(int nClusters, int nIterations, double iniThreshold, double[][] iniCentroids) {
		numOfClusters = nClusters;
		threshold = iniThreshold;
				
		// 1. generate pseudo random centroids
		centroids = generateCentroids(); 			//initial centroid
		
		double[][] updatedCentroids = centroids; 	//new initial centroid (updated)
		int iterationConverges = 0; 				//numbers of iteration before converging
		double newSSE = 0;							//sum of squared errors of prediction (SSE) 
		
		//repeat until convergence
		while (true) {
			centroids = updatedCentroids;			
			
			point = new int[numOfPoints];			
			for (int i = 0; i < numOfPoints; i++) {
				//2. find points which are the nearest to a centroid
				point[i] = nearest(dataset[i]);
			}	
			
			//display Points of Dataset
			displayUpdatedPoints(point);
			
			//count number of points in clusters
			clustersSize(point);

			//3. update claster's centroid
			updatedCentroids = updateClusterCentroid();				
		
			//4. calculation of SSE improvement
			double SSE = newSSE;				
			newSSE = findConverge(updatedCentroids);
			iterationConverges++;	
				
				if(SSE==newSSE){
					System.out.println("\nIteration " + iterationConverges + ": " + SSE);
					//display updatedCentroids - Final
					displayUpdatedCentroids(updatedCentroids);
					
					//display Points of Dataset - Final
					displayUpdatedPoints(point);
					
					//count number of points in clusters - Final
					clustersSize(point);
					System.out.println("\nFinal SSE: " + SSE);
					break;
				}else{
					System.out.println("\nIteration " + iterationConverges + ": " + newSSE);
					//display updatedCentroids
					displayUpdatedCentroids(updatedCentroids);	
					

				}			
				if ((newSSE < threshold) || ((nIterations > 0) && (iterationConverges >= nIterations))) {
					break;
				}
		}
		System.out.println("Converges at iteration " + iterationConverges + "\n");			
	}




	// 1. generate pseudo random centroids
	private double[][] generateCentroids() {
		centroids = new double[numOfClusters][];
		ArrayList<Integer> centrdData = new ArrayList<Integer>();
					
		for (int i = 0; i < numOfClusters; i++) {
			int randomCntrd;
			//generate unique random centroids without duplicates
			do {
				randomCntrd = (int) (Math.random() * numOfPoints);
			} while (centrdData.contains(randomCntrd));
			
			centrdData.add(randomCntrd);
			
			// copy centroid's attributes to the generated centroid
			centroids[i] = new double[numOfDimension];			
			for (int j = 0; j < numOfDimension; j++) {
				centroids[i][j] = dataset[randomCntrd][j];
//				System.out.print(centroids[i][j] +" "); //display cendtroids' data
			}
//			System.out.println();
		}
		System.out.println("Random Centroids: " + centrdData);
		return centroids;
	}
	
	
	
	// 2. find points which are the nearest to a centroid
	private int nearest(double[] datasetRecords) {
		//calculate the distance to centroid 0.
		double minDistance = distance(datasetRecords, centroids[0]);
		int pointIndex = 0;
		
		for (int i = 1; i < numOfClusters; i++) {
			//calculate the distance to centroid i.
			double minDistance2 = distance(datasetRecords, centroids[i]);
			//find minimal distances
			if (minDistance2 < minDistance) {
				minDistance = minDistance2;
				pointIndex = i;
			}
		}
		return pointIndex;
	}
	
	
	
/*********** Distances (Euclidean) ***********/	
	// compute Euclidean distance
	private double distance(double[] vectors1, double[] vectors2) {
		double sum = 0;
		
		for (int i = 0; i < numOfDimension; i++) {
			double euclideanDist = vectors1[i] - vectors2[i];
			sum += Math.pow(euclideanDist, 2);
			// System.out.println(Math.sqrt(sum));
		}			
		// System.out.println("sum" + sum);
		return sum;
//		return Math.sqrt(sum);
	}
/*********** END Distances ***********/		
	
	
	
		//3. update claster's centroid
		private double[][] updateClusterCentroid() {
			int[] sizeOfCluster = new int[numOfClusters];
			double[][] newCentroids = new double[numOfClusters][];			

			for (int i = 0; i < numOfClusters; i++) {				
				newCentroids[i] = new double[numOfDimension];				
				for (int j = 0; j < numOfDimension; j++) {
					newCentroids[i][j] = 0;					
				}				
				sizeOfCluster[i] = 0; // set to 0
			}
			

			//assign points to a centroid and sum their attributes	
			for (int i = 0; i < numOfPoints; i++) {				
				int cluster = point[i];
				
				//add attributes to points and sum them
				for (int j = 0; j < numOfDimension; j++) {					
					newCentroids[cluster][j] = newCentroids[cluster][j] + dataset[i][j]; 
				}
				sizeOfCluster[cluster]++;
			}

			
			// find average of the centroids' attributes 
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					newCentroids[i][j] = newCentroids[i][j] / sizeOfCluster[i]; 
//					System.out.print(newCentroids[i][j] +" ");
				}
//				System.out.println();
			}			
			
			return newCentroids;
		}
		

		
		//4. find convergence
		private double findConverge(double[][] center_new) {
			double sse = 0.0;
			for (int i = 0; i < numOfPoints; i++) {
				//assign points to a cluster				
				int cluster = point[i];
				sse += distance ( dataset[i], center_new[cluster] );
			}
			return sse;
		}
		
		
		
		//display updatedCentroids attributes
		private void displayUpdatedCentroids(double[][] updatedCentroidsF) {
			System.out.println("Updated Centroids Attr:");
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					System.out.print(updatedCentroidsF[i][j] +" ");//							
				}
				System.out.println();
			}		
		}
		
		
		
		//display UpdatedPoints
		private void displayUpdatedPoints(int[] point2) {			
			System.out.print("Points: ");
			for (int i = 0; i < numOfPoints; i++) {
				System.out.print(point[i]+", ");
			}
			
		}
		
		
		
		//count number of points in clusters
		private void clustersSize(int[] point2) {
			int[] cSize = new int[numOfClusters];
			for (int i = 0; i < numOfPoints; i++) {
				int cSizeCount = -1;
				while(point[i] != cSizeCount){
					cSizeCount++;
				}
				cSize[cSizeCount]++;
			}
			
			System.out.print("\nClusters Size: ");
			for (int i = 0; i < numOfClusters; i++) {
				System.out.print(cSize[i] +" ");
			}
		}


}

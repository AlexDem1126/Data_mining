package FP_AD_Fall2017;

import java.util.ArrayList;

public class Calinski_Harabasz {
	private double[][] dataset;
	private int numOfPoints;
	private int numOfDimension;
	private int numOfClusters;
	private double[][] centroids;
	private double threshold;
	private int[] point;
	private double[][] centroidsAttrAverage;
	private double[] meanOfAverage;
	private double sSW;
	private double sSB;

	
	
	public Calinski_Harabasz(double[][] datasetMF, int numOfPointsMF, int numOfDimensionMF) {
		dataset = datasetMF;
		numOfPoints = numOfPointsMF;
		numOfDimension = numOfDimensionMF;
	}
	
	
	
	public double getsSB() {
		return sSB;
	}
	public void setsSB(double sSB) {
		this.sSB = sSB;
	}
	
	

	public void kMeanClusteringForCH(int nClusters, int nIterations, double iniThreshold, double[][] iniCentroids) {
		numOfClusters = nClusters;
		threshold = iniThreshold;
				
		// 1. generate pseudo random centroids
		centroids = generateCentroids(); 			//initial centroid
		
		double[][] updatedCentroids = centroids; 	//new initial centroid (updated)
		int iterationConverges = 0; 				//numbers of iteration before converging
		double newSSW = 0;							//sum of squared within-cluster scatter matrix (SSW) 
//		double sSB = 0; 							//sum of squared between-cluster scatter matrix (SSB) 
		
		//repeat until convergence
		while (true) {
			centroids = updatedCentroids;			
			
			point = new int[numOfPoints];			
			for (int i = 0; i < numOfPoints; i++) {
				//2. find points which are the nearest to a centroid
				point[i] = nearest(dataset[i]);
			}		


			//3. update claster's centroid
			updatedCentroids = updateClusterCentroid();	
			
			//Find SSB (Total sum between)
//			sSB = findSSB(updatedCentroids);
		
			//4. calculation of SSE improvement
			sSW = newSSW;				
//			newSSE = findConverge(centroidsAttrAverage, threshold);
			newSSW = findConverge(centroids, updatedCentroids, threshold);
			iterationConverges++;	
				
				if(sSW==newSSW){
					System.out.println("\n\nIteration " + iterationConverges + ": " + sSW +", "+ sSB);
					//display updatedCentroids - Final
					displayUpdatedCentroidsAttr(updatedCentroids);
					
					//display Points of Dataset - Final
					displayUpdatedPoints(point);
					
					//count number of points in clusters - Final
					clustersSize(point);
					System.out.println("\n\nFinal SSW: " + sSW);
					System.out.println("\n\nFinal SSB: " + sSB);
					break;
				}else{
					System.out.println("\n\nIteration " + iterationConverges + ": " + newSSW +", "+ sSB);					
					//display updatedCentroids
					displayUpdatedCentroidsAttr(updatedCentroids);	
					

				}			
				if ((newSSW < threshold) || ((nIterations > 0) && (iterationConverges >= nIterations))) {
					break;
				}
		}
		System.out.println("Converges at iteration " + iterationConverges + "\n");			
	}




	// 1. generate pseudo random centroids
	private double[][] generateCentroids() {
		centroids = new double[numOfClusters][];
		ArrayList centrdData = new ArrayList();
					
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
			}
		}
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
	
	
	
/*********** Distances (Jaccard, Simple matching coefficient (SMC)) ***********/	
	//Jaccard
	private double distance(double[] vectors1, double[] vectors2) {
		double Jaccard = 0;
		double M01 = 0, M10 = 0, M11 = 0;
		for (int i = 0; i < numOfDimension; i++) {
			if((vectors1[i] == 0) && (vectors2[i] == 1)){
				M01++;
			}
			if((vectors1[i] == 1) && (vectors2[i] == 0)){
				M10++;
			}
			if((vectors1[i] == 1) && (vectors2[i] == 1)){
				M11++;
			}
	}
		Jaccard += (M01+M10)/(M01+M10+M11);
		return Jaccard;
	}
	
	
//	//Simple matching coefficient (SMC)
//	private double distance(double[] vectors1, double[] vectors2) {
//		double SMC = 0;
//		double M00 = 0, M01 = 0, M10 = 0, M11 = 0;
//		for (int i = 0; i < numOfDimension; i++) {
//			if(vectors1[i] == 0 && vectors2[i] == 0){
//				M00++;
//			}
//			if(vectors1[i] == 0 && vectors2[i] == 1){
//				M01++;
//			}
//			if(vectors1[i] == 1 && vectors2[i] == 0){
//				M10++;
//			}
//			if(vectors1[i] == 1 && vectors2[i] == 1){
//				M11++;
//			}
//	}
//		SMC += (M00+M11)/(M00+M01+M10+M11);
//		return SMC;
//	}	
/*********** END Distances ***********/		
	
	
	
		//3. update claster's centroid
		private double[][] updateClusterCentroid() {
			int[] sizeOfCluster = new int[numOfClusters];
			double[][] centroidsAttrSum = new double[numOfClusters][]; //****
			centroidsAttrAverage = new double[numOfClusters][];
			meanOfAverage = new double[numOfClusters];
			double[][] newCentroidsAttr = new double[numOfClusters][];

			for (int i = 0; i < numOfClusters; i++) {
				centroidsAttrSum[i] = new double[numOfDimension];
				centroidsAttrAverage[i] = new double[numOfDimension];
				newCentroidsAttr[i] = new double[numOfDimension];
				for (int j = 0; j < numOfDimension; j++) {
					centroidsAttrSum[i][j] = 0; 	// set to 0
					centroidsAttrAverage[i][j] = 0; // set to 0
					newCentroidsAttr[i][j] = 0; 	// set to 0
				}				
				sizeOfCluster[i] = 0; // set to 0
				meanOfAverage[i] = 0; // set to 0
			}
			

			//3.1 assign points to a centroid and sum their attributes
			centroidsAttrSum = findCentroidsAttrSum(centroidsAttrSum, sizeOfCluster);
//			for (int i = 0; i < numOfPoints; i++) {				
//				int cluster = point[i];
//				
//				//add attributes to points and sum them
//				for (int j = 0; j < numOfDimension; j++) {	
//					centroidsAttrSum[cluster][j] = centroidsAttrSum[cluster][j] + dataset[i][j];
//				}
//				sizeOfCluster[cluster]++;
//			}

			
			//3.2 find average of the centroids' attributes 
			centroidsAttrAverage = findCentroidsAttrAverage(centroidsAttrSum, sizeOfCluster);
//			for (int i = 0; i < numOfClusters; i++) {
//				for (int j = 0; j < numOfDimension; j++) {
//					centroidsAttrAverage[i][j] = centroidsAttrSum[i][j] / sizeOfCluster[i]; 
//				}
//			}
			
			
			//3.3 find mean of the average of the centroids' attributes			
//			double[] meanOfAverage = new double[numOfClusters];
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					meanOfAverage[i] = meanOfAverage[i] + centroidsAttrAverage[i][j];					
				}
				meanOfAverage[i] = meanOfAverage[i] / numOfDimension;
			}			
			
			
			double sB = 0;
			//Find SSB (Total sum of squares between)
			for (int i = 0; i < numOfClusters; i++) {				
				for (int j = 0; j < numOfDimension; j++) {
					//MM = Mean - MeanOfMean
					double MM = centroidsAttrAverage[i][j] - meanOfAverage[i];
					double MM2 = MM*MM;
					//sB = sizeOfCluster*MM2
					sB = sizeOfCluster[i] * MM2;
				}
				sSB += sB;							
			}
			
			
						
			//find new attributes for centroids (Convert attributes values to 0 and 1)
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					if(centroidsAttrAverage[i][j] < meanOfAverage[i]){
						newCentroidsAttr[i][j] = 0; 
					}
					else {
						newCentroidsAttr[i][j] = 1; 
					}					
				}				
			}
			
			
			return newCentroidsAttr;
		}
		
		
		
		//3.1 assign points to a centroid and sum their attributes
		private double[][] findCentroidsAttrSum(double[][] centroidsAttrSum_F, int[] sizeOfCluster_F){
			//assign points to a centroid and sum their attributes	
			for (int i = 0; i < numOfPoints; i++) {				
				int cluster = point[i];
				
				//add attributes to points and sum them
				for (int j = 0; j < numOfDimension; j++) {	
					centroidsAttrSum_F[cluster][j] = centroidsAttrSum_F[cluster][j] + dataset[i][j];
				}
				sizeOfCluster_F[cluster]++;
			}
			return centroidsAttrSum_F;
		}
		
		
		
		//3.2 find average of the centroids' attributes 
		private double[][] findCentroidsAttrAverage(double[][] centroidsAttrSum_F, int[] sizeOfCluster_F){
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = 0; j < numOfDimension; j++) {
					centroidsAttrAverage [i][j] = centroidsAttrSum_F[i][j] / sizeOfCluster_F[i]; 
				}
			}
			return centroidsAttrAverage;
		}


		
		//4. find convergence
		private double findConverge(double[][] center_old, double[][] center_new, double threshold) {
		double temp_sSW = 0.0;
		double d = 0.0;
		for (int i = 0; i < numOfPoints; i++) {
			//assign points to a cluster				
			int cluster = point[i];
			d = distance ( dataset[i], center_new[cluster] );
			double dd = d*d;
			temp_sSW += dd; //SSW == SSE						
		}
		return temp_sSW;
	}


		
//		//find SSB
//		private double findSSB(double[][] center_old, double[][] center_new, double threshold) {
//			double sSB = 0.0;
//			
////			point = new int[numOfPoints];			
////			for (int i = 0; i < numOfPoints; i++) {
////				//2. find points which are the nearest to a centroid
////				point[i] = nearest(dataset[i]);
////			}
//			
//			for (int i = 0; i < numOfClusters; i++) {
//				//assign points to a cluster				
//				int cluster = point[i];
//				int[] AttrAverage = centroidsAttrAverage[i][j];
//				for (int j = 0; j < numOfDimension; j++) {
//				sSB += distance(centroidsAttrAverage, center_new[cluster] );
//			}
//		return sSB; //sSB/numOfClusters
//		}
		
				
		
		//display updatedCentroids attributes
		private void displayUpdatedCentroidsAttr(double[][] updatedCentroidsF) {
			System.out.println("Updated Centroids:");
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

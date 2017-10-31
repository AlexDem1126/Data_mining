package FP_AD_Fall2017;

public class myMain {

	public static void main(String[] args) {

		if (args.length > 0 && args.length < 6) {															
			try {
				
				//N-fold cross validation
				int numOfFolds = 10;				
				for (int nfolds = 0; nfolds < numOfFolds; nfolds++) {					
					
					String F = args[0]; 					// F is a file name
					int K = Integer.parseInt(args[1]); 		// K is a number of clusters
					int I = Integer.parseInt(args[2]); 		// I is a max number of iterations in any run
					double T = Double.parseDouble(args[3]); // T is a convergence threshold
					int R = Integer.parseInt(args[4]); 		// R is a number of runs

					System.out.println("F=" + F + " K=" + K + " I=" + I + " T=" + T + " R=" + R);	
					System.out.println("******************************************************************");
					System.out.println("##### "+ nfolds + " - fold cross validation #####");
					System.out.println("******************************************************************");
					
					//get data from the file
					manageFile_A objMF = new manageFile_A(F, nfolds);
					
//					String tr = "Training data set";
//					String ts = "Testing data set";					
//					objMF.writeDatasetToFile(objMF.getDataset(), tr);
//					objMF.writeDatasetToFile(objMF.getDatasetTest(), ts);
			
				
					
					//******************************************************************
					/*********** Naive Bayes Classifier (NB) ***********/
					//******************************************************************
					System.out.println("\n***** Naive Bayes Classifier (NB) *****");
					Naive_Bayes objNB = new Naive_Bayes(objMF.getTrueGrade(), objMF.getDataset(), objMF.getNumOfPoints(), objMF.getNumOfDimension());
					objNB.Naive_Bayes_Test(objMF.getDatasetTest(), objMF.getTrueGradeTest(), objMF.getNumOfPointsTest());
					
					/*********** END Naive Bayes Classifier (NB) ***********/
					
										
				}//End N-fold cross validation		

								
				
			} catch (NumberFormatException e) {
				System.out.println(
						"Input format should be: <file name.data> or <file name.txt> or <file name.csv>");
				System.exit(1);
			}
		} else {
			System.out.println(
					"Input format should be: <file name.data> or <file name.txt> or <file name.csv>");
		}	

	}

}

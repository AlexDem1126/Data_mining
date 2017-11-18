package FP_AD_Fall2017;

import java.io.File;

public class myMain {

	public static void main(String[] args) {

		if (args.length > 0 && args.length < 2) {															
			try {
				
				//N-fold cross validation
				int numOfFolds = 5;				
				for (int nfolds = 0; nfolds < numOfFolds; nfolds++) {					
					
					String F = args[0]; // F is a file name


					System.out.println("F=" + F);	
					System.out.println("******************************************************************");
					System.out.println("##### "+ nfolds + " - fold cross validation #####");
					System.out.println("******************************************************************");
					
					//get data from the file.
					manageFile_A objMF = new manageFile_A(F, nfolds);
					
//					String tr = "Training data set";
//					String ts = "Testing data set";					
//					objMF.writeDatasetToFile(objMF.getDataset(), tr, nfolds, objMF.getNumOfPoints());
//					objMF.writeDatasetToFile(objMF.getDatasetTest(), ts, nfolds, objMF.getNumOfPointsTest());
					
					
					//******************************************************************
					/*********** Naive Bayes Classifier (NB) WITH Laplace Smoothing ***********/
					//******************************************************************
					System.out.println("\n***** Naive Bayes Classifier (NB) WITH Laplace Smoothing *****");
					Naive_Bayes_with_Laplace_Smoothing objNB_wLS = new Naive_Bayes_with_Laplace_Smoothing(objMF.getTrueGrade(), objMF.getDataset(), objMF.getNumOfPoints(), objMF.getNumOfDimension());
					objNB_wLS.Naive_Bayes_Test(objMF.getDatasetTest(), objMF.getTrueGradeTest(), objMF.getNumOfPointsTest());
					
					/*********** END Naive Bayes Classifier (NB) WITH Laplace Smoothing***********/
							

					
					
										
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

package calinski_harabasz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class manageFile_A {
	public String fileName;
	private int numOfPoints;
	private int numOfDimension;
	
	private int numOfTrueClusters;
	private int numOfDimensionLable;
	
	private String[][] sequenceName;
	private double[][] dataset;
	private String[][] classDistribution;
	
	
	
	public double[][] getDataset() {
		return dataset;
	}

	public void setDataset(double[][] dataset0) {
		this.dataset = dataset0;
	}	
	
	public int getNumOfTrueClusters() {
		return numOfTrueClusters;
	}

	public void setNumOfTrueClusters(int numOfTrueClusters) {
		this.numOfTrueClusters = numOfTrueClusters;
	}

	public int getNumOfDimensionLable() {
		return numOfDimensionLable;
	}

	public void setNumOfDimensionLable(int numOfDimensionLable) {
		this.numOfDimensionLable = numOfDimensionLable;
	}

	public int getNumOfPoints() {
		return numOfPoints;
	}

	public void setNumOfPoints(int numOfPoints0) {
		this.numOfPoints = numOfPoints0;
	}

	public int getNumOfDimension() {
		return numOfDimension;
	}

	public void setNumOfDimension(int numOfDimension0) {
		this.numOfDimension = numOfDimension0;
	}

	
	
public manageFile_A(String fileName2) {
	fileName = fileName2;
	BufferedReader br;
	ArrayList<String> fileData_L_temp;

		// read data from a file
		try {
			//get a number of rows and columns
			br = new BufferedReader(new FileReader(fileName));
            String line = "";
            line = br.readLine();
            int numOfColumns=0, numOfRows=1;
            
            while (br.readLine() != null) {
            	String[] line_temp = line.split("  ");            	
            	numOfColumns = line_temp.length;				
				numOfRows++;	
			}
                        
            System.out.println("numOfRows: " + numOfRows);
            System.out.println("numOfColumns: " + numOfColumns);
            System.out.println(numOfRows + numOfColumns);
            br.close();
            
            
            
         // read whole file and store names in the sequenceName, data attributes in the dataset, labels in the classDistribution
         			br = new BufferedReader(new FileReader(fileName));      
         			fileData_L_temp =  new ArrayList<String>();
         			
         			sequenceName = new String[numOfRows][];
         			for (int i = 0; i < numOfRows; i++) {
         				sequenceName[i] = new String[numOfColumns-8];				
        			}	
         			
         			dataset = new double[numOfRows][];
         			for (int i = 0; i < numOfRows; i++) {
         				dataset[i] = new double[numOfColumns-2];				
        			}	
         			numOfPoints = numOfRows;
         			numOfDimension = numOfColumns-2;
	
         			
         			classDistribution = new String[numOfRows][];
         			for (int i = 0; i < numOfRows; i++) {
         				classDistribution[i] = new String[numOfColumns-8];				
        			}	
         			
         			int nrow = 0;
         			int ncol = 0;
         			
         			line = "";
         			double[] line2;
                    while ((line = br.readLine()) != null) {
                    	String[] line_temp2 = line.split("\\s+");
                    	sequenceName[nrow][ncol] = line_temp2[0];
                    	
                    	line2 = new double[line_temp2.length-2];
                    	
                    	Pattern p = Pattern.compile("\\d\\.\\d+");
                    	Matcher m = p.matcher(line);
                    	int i2 = 0;
                    	while(m.find()){
                    		line2[i2] = Double.parseDouble(m.group());   
                    		i2++;
                    	}
                    	dataset[nrow] = line2;

                    	classDistribution[nrow][ncol] = line_temp2[8];
//                    	System.out.println(sequenceName[nrow][ncol]+ "   "+classDistribution[nrow][ncol]);
//                    	System.out.println(nrow);
                    nrow++;
                    }
        			//print data from file
//        			printFile(dataset);
                    br.close();
                    fileData_L_temp.clear();
           
	
		} catch (FileNotFoundException e) {
			System.out.println("Can not find file " + fileName);
			System.exit(0);
			
		} catch (IOException e) {
			System.out.println("Can not read file " + fileName);
			System.exit(0);
			
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}

	}



public void printFile(double[][] datasetF){
	// print multi-dimensional array
	for (int i = 0; i < numOfPoints; i++) {
		for (int j = 0; j < numOfDimension; j++) {
			System.out.print(datasetF[i][j] + " ");
		}
		System.out.println();
	}
}

}

/*
 * This program will test the rate at which individuals are infected with COVID-19, as well as the rate that they recover
 * from COVID-19, with a determined amount of time steps that is provided by the user.
 * 
 * @author -  Gerardo Solis, Eric Castaneda
 * @Class - CS 1400 Introduction to Java Programming and Problem Solving 
 * @Professor - Dr. John Korah 
 * @Date - October 15, 2021
 */

package atHomeCode;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;
import java.util.Random;

public class CellularAutomataModel {
	/*
	 * Accept the number of individuals that from the user 
	 * 
	 * @param scnr - used to obtain an integer input from the user
	 * @return - the number of individuals the user inputted
	 */
	public static double userInputIndividuals(Scanner scnr)
	{
		double numOfIndividualsInput;
		
		System.out.println("Please enter the number of individuals, the input must be a perfect square: ");
		numOfIndividualsInput = scnr.nextDouble();
		
		if(perfectSquareCheck(numOfIndividualsInput) == false)
		{
			do
			{
				System.out.println("Invalid input.\nPlease enter the number of individuals, must be a perfect square: ");
				numOfIndividualsInput = scnr.nextDouble();
			} while(perfectSquareCheck(numOfIndividualsInput) == false);
		}
		
		return numOfIndividualsInput; 
	}
	
	/*
	 * Check whether the user inputed a value that is a perfect square 
	 * 
	 * @param numOfIndividuals - the number of individuals that will be tested for an infection status 
	 * @return - true or false depending on whether the user input a perfect square or not 
	 */
	public static boolean perfectSquareCheck(double potentialNumOfIndividuals)
	{
		boolean flag = false;
		double sqrt = Math.sqrt(potentialNumOfIndividuals);
		
		if((sqrt - Math.floor(sqrt)) == 0)
		{
			flag = true;
			if(potentialNumOfIndividuals == 0) {
				flag = false;
			}
		}
		
		return flag;
	}
	
	//prompts the user to input the rate for infection. Prompts again if invalid input. 
	public static double userInputAlpha(Scanner scnr)
	{
		double alphaInput;
		
		System.out.println("Please enter an infection rate between 0 and 0.25, inclusive: ");
		alphaInput = scnr.nextDouble();
		
		if(alphaInput < 0 || alphaInput > 0.25)
		{
			do 
			{
				System.out.println("Invalid Input.\nPlease enter an infection rate between 0 and 0.25, inclusive: ");
				alphaInput = scnr.nextDouble();
			} while (alphaInput < 0 || alphaInput > 0.25);
		}
		
		return alphaInput;
	}
	
	//prompts the user to input the rate for recovery. Prompts again if invalid input. 
	public static double userInputBeta(Scanner scnr)
	{
		double betaInput;
		
		System.out.println("Please enter a recovery rate between zero and one, inclusive: ");
		betaInput = scnr.nextDouble();
		
		if(betaInput < 0 || betaInput > 1)
		{
			do 
			{
				System.out.println("Invalid Input.\nPlease enter an infection rate between zero and one, inclusive: ");
				betaInput = scnr.nextDouble();
			} while (betaInput < 0 || betaInput > 1);
		}
		
		return betaInput;
	}
	
	//prompts the user to input the number of timesteps. Prompts again if invalid input. 
	public static int timeSteps(Scanner scnr)
    {
        int timeSteps;

        System.out.println("Please enter the number of time steps. ");
        timeSteps = scnr.nextInt();

        if(timeSteps < 0)
        {
            do
            {
                System.out.println("Invalid input.\nPlease enter the number of time steps. ");
                timeSteps = scnr.nextInt();
            } while(timeSteps < 0);
        }

        return timeSteps; 
    }
	
	public static PrintWriter fileReader() throws IOException
	{
		// Pass the name of the file as an argument to the PrintWriter class constructor
		// PrintWriter class allows you to write data to a file using the print methods 
		// Note: if the file already exists it will be erased and replaced with a new file 
		
		String strFile = "C:\\Users\\jsolis\\Desktop\\TimeStep.txt";
	
		FileWriter file = new FileWriter(strFile);
		PrintWriter printFile = new PrintWriter(file);

		return printFile;
	}
	
	public static String individualsPlacementGrid(double individualsAmount, PrintWriter outFile)
	{
		int endOfRow = (int)Math.sqrt(individualsAmount);
		int endOfColumn = (int)Math.sqrt(individualsAmount);
		
		// Class objects to be able to use a reference to an object and random number generator
		Random randGen = new Random();
		char patientZero = 'I';
		int patientZeroXCoord = randGen.nextInt(endOfRow); // 0 to user input
		int patientZeroYCoord = randGen.nextInt(endOfColumn); // 0 to user input
		
		// Infected individuals infection status
		char susceptibleInd = 'S';		
	
		String stringFile = "";

		
		// Grid 
		outFile.println("Grid placement");
		//creates the first grid and initializes a random coord x,y for patient zero to be. 
		for(int row = 0; row < endOfRow; ++row) 
		{ 
			for(int column = 0; column < endOfColumn; ++column) 
			{
				if(row == patientZeroXCoord && column == patientZeroYCoord)
				{
					stringFile += "(" + row + ", " + column + "): " + patientZero;
					
				}
				
				else
				{
					stringFile += "(" + row + ", " + column + "): " + susceptibleInd;
				}
				
				
				if(column < endOfColumn - 1)
				{
					stringFile += ", ";
				} 
			}
			stringFile += "\n";
		}
		System.out.println(stringFile);
		
		return stringFile;
	}
	
	//main method to go through each time step and run the simulation of infected, susceptible, or recovered. 
	public static String timeStepCycle (double numOfIndividuals, double alphaInput, double betaInput, String strGrid1, int timeSteps, PrintWriter outFile) throws IOException 
	{
		int sqrt = (int)Math.sqrt(numOfIndividuals);
		String strGrid2 = strGrid1;
		double totalInfected = 0;
		double totalRecovered = 0;
		double totalSusceptible = 0;
		double ratio = 0.0;
		double percentageInfected = 0.0;
		double percentageRecovered = 0.0;
		String filePath = "C:\\Users\\jsolis\\Desktop\\TimeStep";
		FileWriter file;
		PrintWriter printFile;
		
		//for loop to go as many time steps as user inputed and traverses every x,y coord each time.
		for (int i = 0; i < timeSteps; i++) 
		{	
			file = new FileWriter(filePath + (i + 1) + ".txt");
			printFile = new PrintWriter(file);
			
			System.out.println("Timestep: " + (i + 1));
			// Print to the file
			printFile.print("\n" + "Timestep: " + (i + 1) + "\n");
			//double for loop for x and y to traverse through every x and y coordinate left to right. range is set to always be exactly in bounds based on user input numOfIndividuals 
			for(int x = 0; x < sqrt ; x++) 
			{ 
				for(int y = 0; y < sqrt ; y++)
				{ 
					//checks if individual at coord x, y is infected and runs the probability if it will recover. if recovered it replaces the string at coord x, y into a new string for the next timestep.
					if(checkStatus(strGrid1, x, y).equals("I")) 
					{
						if(!("".equals(chanceRecover(numOfIndividuals, betaInput, x, y)))) 
						{
							String tempX = String.valueOf(x);
							String tempY = String.valueOf(y);
							strGrid2 = strGrid2.replace("(" + tempX + ", " + tempY + "): I", chanceRecover (numOfIndividuals, betaInput, x, y));
						}		
					}
					//if not infected checks if individual at coord x, y is susceptible and runs the probability if it will be infected. if infected it replaces the string at coord x, y into a new string for the next timestep.
					else if(checkStatus(strGrid1, x, y).equals("S")) 
					{
						if(!("".equals(chanceInfected(numOfIndividuals, alphaInput, strGrid1, x, y)))) 
						{
							String tempX = String.valueOf(x);
							String tempY = String.valueOf(y);
							strGrid2 = strGrid2.replace("(" + tempX + ", " + tempY + "): S",chanceInfected (numOfIndividuals, alphaInput, strGrid1, x, y));
						}
					}
				}
			}
			
			//finishes scanning all individuals here and is the end of that timestep. then updates the new string grid to be stringFile1, prints the grid, and prints how many infected there are.
			System.out.print(strGrid2);
			printFile.print(strGrid2);
			strGrid1 = strGrid2;
			totalInfected = totalInfected(strGrid1);
			totalRecovered = totalRecovered(strGrid1);
			totalSusceptible = totalSusceptible(strGrid1);
			System.out.println("Total infected at the end of this timestep: " + (int)totalInfected);
			printFile.print("Total infected at the end of this timestep: " + (int)totalInfected + "\n");
			if(totalRecovered != 0)
			{
				ratio = totalInfected / totalRecovered;
			}
			percentageInfected = (totalInfected / (totalInfected + totalRecovered)) * 100;
			percentageRecovered = (totalRecovered / (totalInfected + totalRecovered)) * 100;
			System.out.printf("Total of susceptible individuals: %.0f, %nTotal of recovered individuals: %.0f %nRatio: %.2f, %.0f:%.0f, Infected: %.2f%% Recovered: %.2f%% %n%n", totalSusceptible, totalRecovered, ratio, totalInfected, totalRecovered, percentageInfected, percentageRecovered);
			printFile.printf("Total of susceptible individuals: %.0f, %nTotal of recovered individuals: %.0f %nRatio: %.2f, %.0f:%.0f, Infected: %.2f%% Recovered: %.2f%% %n%n", totalSusceptible, totalRecovered, ratio, totalInfected, totalRecovered, percentageInfected, percentageRecovered);
			printFile.close();
		}
		
		
		return strGrid2;
	}

	// checks the string file at coord (x, y) and returns whether the coord is infected, susceptible, or recovered. 
	public static String checkStatus (String strGrid1, int x, int y) {
		String status = "";
		String tempX = String.valueOf(x);
		String tempY = String.valueOf(y);
		if(strGrid1.contains("(" + tempX + ", " + tempY + "): I")) {
			status = "I";
		}
		if(strGrid1.contains("(" + tempX + ", " + tempY + "): R")) {
			status = "R";
		}
		if(strGrid1.contains("(" + tempX + ", " + tempY + "): S")) {
			status = "S";
		}
		return status;
	}
	
	//calculates whether coord (x,y) recovers if susceptible. Compares a random  num gen to the userinputted alphaInput * the number of infected neighbors that the method checks.
	public static String chanceInfected (double numOfIndividuals, double alphaInput, String stringFile1, int x, int y) {  
		
		double rate = alphaInput * infectedNeighbors(numOfIndividuals, stringFile1, x, y); 
		double a;
		String tempX = String.valueOf(x);
		String tempY = String.valueOf(y);
		String stringFile2 = "(" + tempX + ", " + tempY + "): S";
		Random randGen = new Random();
		a = randGen.nextDouble();
		if (a < rate ) { // less than rate is infected greater than rate is susceptible still 
			stringFile2 = updateGridInfected(x, y);
	}
		return stringFile2;
	}
	
	//references file to see if coord (x,y) is infected. Returns a double if infected to be passed on to other methods to calculate rate 
	public static double isNeighborInfected (String stringFile1, int x, int y, double infected) {
		String tempX = String.valueOf(x);
		String tempY = String.valueOf(y);
		if(stringFile1.contains("(" + tempX + ", " + tempY + "): I")) {
			infected += 1.0;
		}
		return infected;
	}
	
	//when called checks how many neighbors are infected based off coordinates, Returns that num in double infected to calc rate 
		public static double infectedNeighbors (double numOfIndividuals, String stringFile1, int x, int y) { //checks the number of neighbors infected (done except for infected)
			int sqrt = (int)Math.sqrt(numOfIndividuals);
			double infected = 0.0;	
			if (sqrt != 2 ) { 
				
			if ((x == 0) && (y == 0)) { // top left corner
				infected +=isNeighborInfected(stringFile1, 0 , 1, infected);
				infected +=isNeighborInfected(stringFile1, 1, 0, infected);
			}
			if((x == 0) && (y == sqrt - 1)) { // top right corner 
				infected +=isNeighborInfected(stringFile1, 1 , sqrt - 1, infected);
				infected +=isNeighborInfected(stringFile1, 0, sqrt -2, infected);
			}
			if ((x == (sqrt - 1)) && (y == 0)) { // bottom left corner 
				infected +=isNeighborInfected(stringFile1, sqrt - 2 , 0, infected);
				infected +=isNeighborInfected(stringFile1, sqrt - 1, 1, infected);
			}
			if ((x == (sqrt - 1)) && (y == (sqrt - 1))) { // bottom right corner 
				infected +=isNeighborInfected(stringFile1, sqrt - 2, sqrt - 1, infected);
				infected +=isNeighborInfected(stringFile1, sqrt - 1, sqrt - 2, infected);
			}
			
			//interior individuals 
			if( ( (x >= 1) && (x <= (sqrt - 2) ) ) && ( (y >= 1) && (y <= (sqrt - 2) ) )) {
				infected +=isNeighborInfected(stringFile1, x + 1 , y, infected );
				infected +=isNeighborInfected(stringFile1, x - 1 , y, infected );
				infected +=isNeighborInfected(stringFile1, x , y + 1, infected );
				infected +=isNeighborInfected(stringFile1, x , y - 1, infected );
			}
			
			//edge individuals maybe add if sqrt is greater than 2 because then no center individuals 
			
			if ( ( x == 0) && ( (y > 0) && (y < (sqrt - 1) ) ) ) { // top boundary 
				infected +=isNeighborInfected(stringFile1, x + 1 , y, infected );
				infected +=isNeighborInfected(stringFile1, x , y + 1, infected );
				infected +=isNeighborInfected(stringFile1, x , y - 1, infected );
			}
			if ( ( x == (sqrt - 1) ) && ( (y > 0) && (y < (sqrt - 1) ) ) ) { // bottom boundary 
				infected +=isNeighborInfected(stringFile1, x - 1 , y, infected);
				infected +=isNeighborInfected(stringFile1, x , y + 1, infected);
				infected +=isNeighborInfected(stringFile1, x , y - 1, infected);
			}
			if ( ( (x > 0) && (x < (sqrt - 1) ) )  && ( y == (sqrt - 1) ) ) { // right boundary 
				infected +=isNeighborInfected(stringFile1, x , y - 1 , infected);
				infected +=isNeighborInfected(stringFile1, x - 1 , y , infected);
				infected +=isNeighborInfected(stringFile1, x + 1 , y , infected);
			}
			if ( ( (x > 0) && (x < (sqrt - 1) ) ) && ( y == 0) ) { // left boundary 
				infected +=isNeighborInfected(stringFile1, x , y + 1 , infected);
				infected +=isNeighborInfected(stringFile1, x + 1 , y, infected);
				infected +=isNeighborInfected(stringFile1, x - 1 , y, infected);
			}
			}
			else {
				if ((x == 0) && (y == 0)) { // top left corner
					infected +=isNeighborInfected(stringFile1, 0 , 1, infected);
					infected +=isNeighborInfected(stringFile1, 1, 0, infected);
				}
				if((x == 0) && (y == sqrt - 1)) { // top right corner 
					infected +=isNeighborInfected(stringFile1, 1 , sqrt - 1, infected);
					infected +=isNeighborInfected(stringFile1, 2, sqrt -2, infected);
				}
				if ((x == (sqrt - 1)) && (y == 0)) { // bottom left corner 
					infected +=isNeighborInfected(stringFile1, sqrt - 2 , 0, infected);
					infected +=isNeighborInfected(stringFile1, sqrt - 1, 1, infected);
				}
				if ((x == (sqrt - 1)) && (y == (sqrt - 1))) { // bottom right corner 
					infected +=isNeighborInfected(stringFile1, sqrt - 2, sqrt - 1, infected);
					infected +=isNeighborInfected(stringFile1, sqrt - 1, sqrt - 2, infected);
				}
		}
			return infected;
		}
		
		//finds the number of total infected by removing all other characters besides 'I' then gets total by finding string length()
		public static int totalInfected (String stringFile2) { 
			int total = 0;
			String fileTemp = stringFile2;
			
			fileTemp = fileTemp.replace("1", "");
			fileTemp = fileTemp.replace("2", "");
			fileTemp = fileTemp.replace("3", "");
			fileTemp = fileTemp.replace("4", "");
			fileTemp = fileTemp.replace("5", "");
			fileTemp = fileTemp.replace("6", "");
			fileTemp = fileTemp.replace("7", "");
			fileTemp = fileTemp.replace("8", "");
			fileTemp = fileTemp.replace("9", "");
			fileTemp = fileTemp.replace("0", "");
			fileTemp = fileTemp.replace("(", "");
			fileTemp = fileTemp.replace(")", "");
			fileTemp = fileTemp.replace(",", "");
			fileTemp = fileTemp.replace(":", "");
			fileTemp = fileTemp.replace(" ", "");
			fileTemp = fileTemp.replace("S", "");
			fileTemp = fileTemp.replace("R", "");
			fileTemp = fileTemp.replace("\n", "");
			
			total = fileTemp.length();
			return total;
		}
		
	//returns a new string that replaces that specific string from coord (x,y) from status S to status I.
	public static String updateGridInfected (int x, int y ) { 
		
		String tempX = String.valueOf(x);
		String tempY = String.valueOf(y);
		String stringFile2 = "(" + tempX + ", " + tempY + "): S";
		stringFile2 = stringFile2.replace("(" + tempX + ", " + tempY + "): S", "(" + tempX + ", " + tempY + "): I" );
		
		return stringFile2;
	}

	//calculates whether coord (x,y) recovers if infected. Using random num gen compared to the user inputted rate for recovery.
	public static String chanceRecover (double numOfIndividuals, double betaInput, int x, int y) {  //(done just insert rate)
		
		double rate = betaInput;
		double b;
		String tempX = String.valueOf(x);
		String tempY = String.valueOf(y);
		String stringFile2 = "(" + tempX + ", " + tempY + "): I";
		Random randGen = new Random();
		b = randGen.nextDouble();
		if (b < rate) 
		{ // less than rate is recover greater than rate is infected still 
			stringFile2 = updateGridRecovered(x, y);	
		}
		return stringFile2;
	}
	
	//finds the number of total recovered by removing all other characters besides 'I' then gets total by finding string length()
	public static int totalRecovered (String stringFile2) { 
		int total = 0;
		String fileTemp = stringFile2;
		
		fileTemp = fileTemp.replace("1", "");
		fileTemp = fileTemp.replace("2", "");
		fileTemp = fileTemp.replace("3", "");
		fileTemp = fileTemp.replace("4", "");
		fileTemp = fileTemp.replace("5", "");
		fileTemp = fileTemp.replace("6", "");
		fileTemp = fileTemp.replace("7", "");
		fileTemp = fileTemp.replace("8", "");
		fileTemp = fileTemp.replace("9", "");
		fileTemp = fileTemp.replace("0", "");
		fileTemp = fileTemp.replace("(", "");
		fileTemp = fileTemp.replace(")", "");
		fileTemp = fileTemp.replace(",", "");
		fileTemp = fileTemp.replace(":", "");
		fileTemp = fileTemp.replace(" ", "");
		fileTemp = fileTemp.replace("S", "");
		fileTemp = fileTemp.replace("I", "");
		fileTemp = fileTemp.replace("\n", "");
		
		total = fileTemp.length();
		return total;
	}
	
	//returns a new string that replaces that specific string from coord (x,y) from status I to status R.
	public static String updateGridRecovered (int x, int y ) { // goes to coord (x,y) and updates to infected (done)
		
		String tempX = String.valueOf(x);
		String tempY = String.valueOf(y);
		String stringFile2 = "(" + tempX + ", " + tempY + "): I";
		stringFile2 = stringFile2.replace("(" + tempX + ", " + tempY + "): I", "(" + tempX + ", " + tempY + "): R" );
		
		return stringFile2;
	}
	
	public static int totalSusceptible(String stringFile2) { 
		int total = 0;
		String fileTemp = stringFile2;
		
		fileTemp = fileTemp.replace("1", "");
		fileTemp = fileTemp.replace("2", "");
		fileTemp = fileTemp.replace("3", "");
		fileTemp = fileTemp.replace("4", "");
		fileTemp = fileTemp.replace("5", "");
		fileTemp = fileTemp.replace("6", "");
		fileTemp = fileTemp.replace("7", "");
		fileTemp = fileTemp.replace("8", "");
		fileTemp = fileTemp.replace("9", "");
		fileTemp = fileTemp.replace("0", "");
		fileTemp = fileTemp.replace("(", "");
		fileTemp = fileTemp.replace(")", "");
		fileTemp = fileTemp.replace(",", "");
		fileTemp = fileTemp.replace(":", "");
		fileTemp = fileTemp.replace(" ", "");
		fileTemp = fileTemp.replace("R", "");
		fileTemp = fileTemp.replace("I", "");
		fileTemp = fileTemp.replace("\n", "");
		
		total = fileTemp.length();
		return total;
	}
	
	public static void main(String[] args) throws IOException
	{
		// TODO Auto-generated method stub
		Scanner scnr = new Scanner (System.in);
		PrintWriter outFile;
		String strGrid1; // Grid to place individuals 
		String strGrid2;
		
		double numOfIndividuals;
		double alpha;
		double beta;
		int timeSteps;
		
		System.out.println("HELLO THIS IS MY PROJECT0 ASSIGNMENT MODIFICATION");
		// Call methods to accept the user input
		outFile = fileReader();
		numOfIndividuals = userInputIndividuals(scnr);
		alpha = userInputAlpha(scnr);
		beta = userInputBeta(scnr);
		timeSteps = timeSteps(scnr);
		
		System.out.println();
		
		strGrid1 = individualsPlacementGrid(numOfIndividuals, outFile);
		outFile.print(strGrid1);

		strGrid2 = timeStepCycle (numOfIndividuals, alpha, beta, strGrid1, timeSteps, outFile);
		
		scnr.close();
		outFile.close();
	}	
}
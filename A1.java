// Programmer: Stephen Mitchell
// Student ID: c3394182
// Last modified: 24 Aug 2022
// Course: COMP2240
// Assignment 1

// This file reads in a datafile input and implements 
// the main dispatcher program

import java.util.*;
import java.io.*;


class A1
{  
	//static Scanner console = new Scanner(System.in);	

	public static void main(String args[]) 		// Run main program
	{
    	Scanner inputStream;
    	
    	try
    	{
            inputStream = new Scanner (new File (args[0]));   	// Take input text filename as argument
        	
        	int DISP = 0;									
        	String id = "";										// Intialise task variables
			int arrive = 0;
			int execSize = 0;

			ArrayList<Task> tasks = new ArrayList<>();		// Create new ArrayList for all tasks 


        	while (inputStream.hasNextLine())					// Evaluate input file line by line
			{
				String line = inputStream.nextLine();			// Initialise variable for each line
				if (line.contains("DISP"))						// Check line for Dispatcher run time
				{
					String[] getInt = line.split("DISP: ", 2);	// If found, isolate integer
					DISP = Integer.parseInt(getInt[1]);		// Convert string to int
				}


				if (line.contains("ID"))							// Check line for task ID
				{
					String [] getID = line.split("ID: ", 2);		// If found, isolate id name
					id = getID[1];									// Set id variable

				}	
				if (line.contains("Arrive"))							// Check Line for Arrival Time
				{
					String [] getArrival = line.split("Arrive: ", 2);	// If found, isolate integer
					arrive = Integer.parseInt(getArrival[1]);			// Convert string to int
					
				}
				if (line.contains("ExecSize"))								// Check Line for Service Time
				{
					String [] getExecSize = line.split("ExecSize: ", 2);	// If found, isolate Service Time
					execSize = Integer.parseInt(getExecSize[1]);			// Convert string to int
					Task currentTask = new Task(id, arrive, execSize);		// Create instance of task to add to ArrayList 
					
					tasks.add(currentTask);		// add new task to task list				
				}

			}

			SchedulingInfo data = new SchedulingInfo(DISP, tasks);			// Store input data

			FCFS fcfsData = new FCFS(data.getDispatch(), data.getTasks());		// Run First Come First Serve algorithm using input data
			RR rrData  = new RR(data.getDispatch(), data.getTasks());			// Run Round Robin algorithm using input data
			NRR nrrData  = new NRR(data.getDispatch(), data.getTasks());		// Run Narrow Round Robin algorithm using input data
			FB fbData = new FB(data.getDispatch(), data.getTasks());			// Run Feedback (constant) algorithm using input data
			
			// Print Averages for all Scheduling Algorithms

			System.out.println("\nSummary");
			System.out.println("Algorithm	Average TurnAround	Time Average Waiting Time");
			System.out.println("FCFS" + "		" + String.format("%.2f", fcfsData.avgTurn()) + "		" + String.format("%.2f", fcfsData.avgWait()));
			System.out.println("RR  " + "		" + String.format("%.2f", rrData.avgTurn()) + "		" + String.format("%.2f", rrData.avgWait()));
			System.out.println("NRR" + "		" + String.format("%.2f", nrrData.avgTurn()) + "		" + String.format("%.2f", nrrData.avgWait()));
			System.out.println("FB (constant)" + "	" + String.format("%.2f", fbData.avgTurn()) + "		" + String.format("%.2f", fbData.avgWait()));			

			inputStream.close(); 		// Close input file
        }

    	catch(FileNotFoundException e)		// If file not found
    	{
            System.out.println("Unable to locate file ");  // Print Error notice
            return;
        }
    }
}
// Programmer: Stephen Mitchell
// Student ID: c3394182
// Last modified: 24 Aug 2022
// Course: COMP2240
// Assignment 1

// This file implements the Round Robin Scheduling Algorithm
// using a given quantum time of 4 milliseconds

import java.util.*;		// Import utilities package
import java.io.*;		// Import input/ output package

public class RR
{
	private String id = "";			// Create and initialise variable for task ID
	private int arrive = 0;			// Create and initialise variable for task Arrival Time
	private int execSize = 0;		// Create and initialise variable for task Service Time
	private int runTime = 0;		// Create and initialise variable to track the runtime of the program
	private int finishTime = 0;		// Create and initialise variable for task Finish Time
	private int quantumTime = 4;	// Create and initialise variable for the given Quantum Time
	private int DISP;				// Create and initialise variable for the given Dispatch Time
   	private int totalTasks;

   	private	ArrayList<Task> readyTasks = new ArrayList<>();			// Create new Arraylist to store tasks that are ready to run
	private Task currentTask = new Task(id, arrive, execSize);		// Create new Task to represent the current running task
	private ArrayList<Task> waitingTasks = new ArrayList<>();		// Create new Arraylist to store tasks that are waiting to run
	private ArrayList<Task> finishedTasks = new ArrayList<>();		// Create new Arraylist to store tasks that have been completed
	
	private double totalTurnTime;		// Create variable to store total turnaround time
	private double totalWaitTime;		// Create variable to store total wait time

	public RR(int d, ArrayList<Task> t)
	{
		totalTasks = t.size();		// Create and initialise variable to track the amount of incoming tasks
		DISP = d;						// Set dispatch time to given dispatch time (d)
		runTime = DISP;					// Set initial runtime to match the dispatch time

		System.out.println("\nRR: ");	// Print line 1 of the output

		for (int i = 0; i < totalTasks; i++)		// Loop through each of the tasks provided by the file
		{		
			t.get(i).setTempExecSize(t.get(i).getExecSize());	// Set the temporary service time for each task
			if (t.get(i).getArrive() == 0)						// Check for immediate ready tasks
			{
				readyTasks.add(t.get(i));						// Add tasks to ready task queue
			}
			else   												// If task not ready yet
			{
				waitingTasks.add(t.get(i));						// Add tasks to waiting queue
			}
		}

		while (finishedTasks.size() < totalTasks)
		{
			if (readyTasks.size() > 0)		// Check for any tasks that are ready to run
			{
				for (int i = 0; i < readyTasks.size(); i++)		// Loop through ready tasks
				{	
					System.out.println("T" + runTime + ":" + " " + readyTasks.get(i).getID());	// Print out next part of output
					
					currentTask = readyTasks.get(i);		// Set the current task
						
					if (readyTasks.size() == 1 && waitingTasks.size() == 0)		// Allow tasks to run longer than quantum time if no other programs are waiting 
					{
						runTime += currentTask.getTempExecSize() + DISP;				// Update runtime
						finishTime = runTime;											// Set task finish time
						
						currentTask.setTurnTime(finishTime - DISP);						// Set task Turnaround time
						currentTask.setWaitTime(currentTask.getTurnTime() - DISP);		// Set task Wait time
						
						finishedTasks.add(currentTask);									// Add task to completed list
						readyTasks.remove(readyTasks.get(0));							// Remove the task from ready list
						i--;															// Decrement loop count variable
					}

					else if (readyTasks.get(i).getTempExecSize() > quantumTime)		// Check for any tasks that will need to run more than once
					{
						readyTasks.remove(readyTasks.get(i));			// Remove task from ready queue
						i--;											// Decrement loop count variable
						
						runTime += quantumTime + DISP;					// Update run time
						
						currentTask.setTempExecSize(currentTask.getTempExecSize() - quantumTime);		// Set temporary service time of current task
						
						checkTime();		// Check for new ready Tasks or gaps between tasks

						readyTasks.add(currentTask);	// Add the current task back into the ready queue to execute remaining service time
					}
					
					else 	// If current task is LESS than quantum time
					{
						runTime += currentTask.getTempExecSize() + DISP;				// Update runtime
						finishTime = runTime;											// Set finsh time for the task
						
						currentTask.setTurnTime(finishTime - DISP);						// Set turnaround time for the current task
						currentTask.setWaitTime(currentTask.getTurnTime() - DISP);		// Set wait time for current task
						
						finishedTasks.add(currentTask);									// Add task to completed list	
						readyTasks.remove(readyTasks.get(i));							// Remove the task from the ready task
						i--;															// Decrement loop count variable
						
						checkTime();		// Check for new ready tasks or gaps between tasks
					}
				}	
			}
			else 
			{
				checkTime();
			}
		}
		System.out.println("\nProcess Turnaround Time Waiting Time");		// Print next section of the output

		for (int i = 0; i < finishedTasks.size(); i++) 		// Loop through completed tasks
		{		
			Collections.sort(finishedTasks, new IDComparator()); //Sort finished tasks by id
		
			// Output id, turnaround time, and waitime for each task

			System.out.println(finishedTasks.get(i).getID() + "	" + finishedTasks.get(i).getTurnTime() + "		" + finishedTasks.get(i).getWaitTime()); 	// Output Task data
			
			totalTurnTime += finishedTasks.get(i).getTurnTime();		// Calculate total turnaround time
			totalWaitTime += finishedTasks.get(i).getWaitTime();		// Calculate total wait time
		}
	}

	
	// Method to calculate average turnaround time

	public double avgTurn()
	{
		double totalTasks = finishedTasks.size();		// Set total number of tasks
		double avgTurnTime = 0;							// Initialise result variable
		avgTurnTime = totalTurnTime / totalTasks;		// Divide total turnaround time by number of finished tasks 
		return avgTurnTime;								// Return resulting average
	}

	// Method to calculate average wait time

	public double avgWait()
	{
		double totalTasks = finishedTasks.size();		// Set total number of tasks
		double avgWaitTime = 0;							// Initialise result variable
		avgWaitTime = totalWaitTime / totalTasks;		// Divide total wait time by number of finished tasks
		return avgWaitTime;								// Return resulting average
	}

	public void checkReady()
	{
		if (waitingTasks.size() > 0)
		{	
			for (int j = 0; j < waitingTasks.size(); j++)			// Loop through waiting tasks
			{
				if (runTime > waitingTasks.get(j).getArrive())		// Check the waiting task's arrival times against the current runtime
				{
					readyTasks.add(waitingTasks.get(j));			// Add any tasks that are ready to the ready list
					waitingTasks.remove(waitingTasks.get(j));		// Remove those tasks from the waiting list
					j--;											// Decrement loop count variable
				} 
			}
		}			
	}

	// Method to check for gaps in between tasks

	public void checkTime()
	{
		checkReady();		// Check for new ready tasks first

		// Check if there are no ready tasks, but there are still tasks waiting to arrive

		if (waitingTasks.size() != 0 && readyTasks.size() == 0 && finishedTasks.size() < totalTasks)
		{
			runTime++;		// Increment runtime by 1 millisecond
		}
	}
}


// Programmer: Stephen Mitchell
// Student ID: c3394182
// Last modified: 24 Aug 2022
// Course: COMP2240
// Assignment 1

// This file implements the Round Robin Scheduling Algorithm
// using a given quantum time of 4 milliseconds

import java.util.*;
import java.io.*;

public class FCFS
{

   	private int DISP;			// Create and initialise variable for the given Dispatch Time
	private int runTime = 0;     // Create and initialise variable to track the runtime of the program	

   	private	ArrayList<Task> readyTasks = new ArrayList<>();			// Create new Arraylist to store tasks that are ready to run
	private ArrayList<Task> currentTask;							// Create new Task to represent the current running task
	private ArrayList<Task> waitingTasks = new ArrayList<>();		// Create new Arraylist to store tasks that are waiting to run
	private ArrayList<Task> finishedTasks = new ArrayList<>();		// Create new Arraylist to store tasks that have been completed

	private double totalTurnTime;		// Create variable to store total turnaround time
	private double totalWaitTime;		// Create variable to store total wait time

	public FCFS(int d, ArrayList<Task> t)
	{
		int totalTasks = t.size();		// Create and initialise variable to track the amount of incoming tasks
		DISP = d;						// Set dispatch time to given dispatch time (d)
		runTime = DISP;					// Set initial runtime to match the dispatch time

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
		System.out.println("FCFS: ");		// Print first line of the output
		
		checkReady();		// Check for any tasks that have arrived after adding dispatch time

		if (readyTasks.size() > 0)		// Check for any ready tasks
		{
			for (int i = 0; i < readyTasks.size(); i++)			// Loop through ready tasks
			{	

				System.out.println("T" + runTime + ":" + " " + readyTasks.get(i).getID());		// Print out next part of output
				runTime += readyTasks.get(i).getExecSize() + DISP;								// Update runtime
				readyTasks.get(i).setTurnTime(runTime - DISP);									// Set task turnaround time
				readyTasks.get(i).setWaitTime(readyTasks.get(i).getTurnTime() - DISP);			// Set task wait time
				finishedTasks.add(readyTasks.get(i));											// Add task to completed list
				readyTasks.remove(readyTasks.get(i));											// Remove task form the ready task list
				i--;
				checkReady();
			}
		}

		System.out.println("\nProcess Turnaround Time Waiting Time");	// Print next line of outpur
		for (int i = 0; i < totalTasks; i++)
		{
			// Print task data

			System.out.println(finishedTasks.get(i).getID() + "	" + finishedTasks.get(i).getTurnTime() + "		" + finishedTasks.get(i).getWaitTime());	
		
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
}
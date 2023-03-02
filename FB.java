// Programmer: Stephen Mitchell
// Student ID: c3394182
// Last modified: 24 Aug 2022
// Course: COMP2240

// This file implements the standard Feedback (constant) Scheduling Algorithm
// using a given quantum time of 4 milliseconds and a level 6 priority 
// (0 = highest priority, 5 = lowest priority). No priority boosting is used.


import java.util.*;		
import java.io.*;		

public class FB
{
	// Set task attributes for currentTask variable

	private String id = "";				// Create and initialise variable for task ID
	private int arrive = 0;				// Create and initialise variable for task Arrival Time
	private int execSize = 0;			// Create and initialise variable for task Service Time
	
	// Timing variables

	private int runTime = 0;			// Create and initialise variable to track the runtime of the program
	private int finishTime = 0;			// Create and initialise variable for task Finish Time
	private int quantumTime = 4;		// Create and initialise variable for the given Quantum Time
	private int DISP;					// Create variable for the given Dispatch Time
	private double totalTurnTime;		// Create variable to store total turnaround time
	private double totalWaitTime;		// Create variable to store total wait time	

	private Task currentTask = new Task(id, arrive, execSize);		// Create new Task to represent the current running task   	
   	
   	private	ArrayList<ArrayList<Task>> priorityLevels = new ArrayList<ArrayList<Task>>(6);			// Create new Arraylist to store priority queues (max 6)
   	
   	// Priority Queues

   	private	ArrayList<Task> priority0 = new ArrayList<>();			// Create new Arraylist to store tasks that are highest priority
   	private	ArrayList<Task> priority1 = new ArrayList<>();			// Create new Arraylist to store tasks that are 2nd highest priority
   	private	ArrayList<Task> priority2 = new ArrayList<>();			// Create new Arraylist to store tasks that are 3rd highest priority
   	private	ArrayList<Task> priority3 = new ArrayList<>();			// Create new Arraylist to store tasks that are 4th highest priority
   	private	ArrayList<Task> priority4 = new ArrayList<>();			// Create new Arraylist to store tasks that are 5th highest priority
   	private	ArrayList<Task> priority5 = new ArrayList<>();			// Create new Arraylist to store tasks that are lowest priority	
	private ArrayList<Task> waitingTasks = new ArrayList<>();		// Create new Arraylist to store tasks that are waiting to run	
	private ArrayList<Task> finishedTasks = new ArrayList<>();		// Create new Arraylist to store tasks that have been completed

	public FB(int d, ArrayList<Task> t)
	{
		int totalTasks = t.size();		// Create and initialise variable to track the amount of incoming tasks
		DISP = d;				// Set dispatch time to given dispatch time (d)
		runTime = DISP;			// Set initial runtime to match the dispatch time

		System.out.println("\nFB (constant): ");	// Print line 1 of the output
		
		// Add all priority queues to list

		priorityLevels.add(0, priority0);
		priorityLevels.add(1, priority1);
		priorityLevels.add(2, priority2);
		priorityLevels.add(3, priority3);
		priorityLevels.add(4, priority4);
		priorityLevels.add(5, priority5);				

		for (int i = 0; i < totalTasks; i++)					// Loop through each of the tasks provided by the file
		{		
			t.get(i).setTempExecSize(t.get(i).getExecSize());	// Set the temporary service time for each task
			if (t.get(i).getArrive() == 0)						// Check for immediate ready tasks
			{
				priority0.add(t.get(i));						// Add tasks to highest priority queue
			}
			else   												// If task not ready yet
			{
				waitingTasks.add(t.get(i));						// Add tasks to waiting queue
			}
		}

		// Loop through all tasks until they have finished processing

		while (finishedTasks.size() < totalTasks)				
		{
			for (int j = 0; j < priorityLevels.size(); j++) 		// Loop through each priority queue
			{

				// Check all priority levels to ensure highest priority tasks run first

				if (priority0.size() != 0)
				{
					j = 0;
				}
				else if (priority0.size() == 0 && priority1.size() > 1)
				{
					j = 1;
				}
				else if (priority0.size() == 0 && priority1.size() == 0 && priority2.size() > 0)
				{
					j = 2;
				}
				else if (priority0.size() == 0 && priority1.size() == 0 && priority2.size() == 0 && priority3.size() > 0)
				{
					j = 3;
				}
				else if (priority0.size() == 0 && priority1.size() == 0 && priority2.size() == 0 
						&& priority3.size() == 0 && priority4.size() > 0)
				{
					j = 4;
				}
				else if (priority0.size() == 0 && priority1.size() == 0 && priority2.size() == 0 
						&& priority3.size() == 0 && priority4.size() == 0 && priority5.size() > 0)
				{
					j = 5;
				}

				// Loop through the tasks of the current priority queue

				if (priorityLevels.get(j).size() > 0)
				{
					for (int i = 0; i < priorityLevels.get(j).size(); i++)
					{
						System.out.println("T" + runTime + ":" + " " + priorityLevels.get(j).get(i).getID());	// Print out next part of output
						
						checkReady();	// Check for ready tasks

						currentTask = priorityLevels.get(j).get(i);		// Set the current task

						if (priorityLevels.size() == 1 && waitingTasks.size() == 0)			// Allow tasks to run longer than quantum time if no other programs are waiting 
						{
							runTime += currentTask.getTempExecSize() + DISP;					// Update runtime
							finishTime = runTime;												// Set task finish time
							
							currentTask.setTurnTime(finishTime - DISP);							// Set Turnaround time
							currentTask.setWaitTime(currentTask.getTurnTime() - DISP);			// Set Wait time
							
							finishedTasks.add(currentTask);										// Add task to completed list
							priorityLevels.get(j).remove(currentTask);							// Remove the task from ready list
							i--;																// Decrement loop count variable
						}

						else if	(currentTask.getTempExecSize() < quantumTime)				// If current task is LESS than quantum time					
						{
							runTime += currentTask.getTempExecSize() + DISP;				// Update runtime
							checkReady();													// Check for ready tasks after runtime update
							finishTime = runTime;											// Set finsh time for the task
							
							currentTask.setTurnTime(finishTime - DISP);						// Set turnaround time for the current task
							currentTask.setWaitTime(currentTask.getTurnTime() - DISP);		// Set wait time for current task
							
							finishedTasks.add(currentTask);									// Add task to completed list		
							priorityLevels.get(j).remove(priorityLevels.get(j).get(i));		// Remove task from priority level queue	
							i--;															// Decrement counter
						}			
						
						else 		// Check for any tasks that will need to run more than once
						{
						
							runTime += quantumTime + DISP;												// Update run time
							
							checkReady();																// Check for new ready Tasks
							
							currentTask.setTempExecSize(currentTask.getTempExecSize() - quantumTime);	// Set temporary service time of current task
							
							priorityLevels.get(j).remove(priorityLevels.get(j).get(i));					// Remove task from priority level queue	

							if (j == 5)																	// Check if task is already in lowest priority queue
							{
								priorityLevels.get(j).add(currentTask);									// Place task back into lowest priority queue to execute remaining service time
							}
							else
							{
								priorityLevels.get(j+1).add(currentTask);								// Relegate task to next lowest priority queue to execute remaining service time
							}

							i--;

						}
					}
				}
			}
		}

		System.out.println("\nProcess Turnaround Time Waiting Time");		// Print next section of the output
			
		for (int i = 0; i < finishedTasks.size(); i++) 						// Loop through completed tasks
		{		
			Collections.sort(finishedTasks, new IDComparator()); 			//Sort finished tasks by id
		
			// Output id, turnaround time, and waitime for each task

			System.out.println(finishedTasks.get(i).getID() + "	" + finishedTasks.get(i).getTurnTime() + "		" + finishedTasks.get(i).getWaitTime());

			totalTurnTime += finishedTasks.get(i).getTurnTime();		// Calculate total turnaround time
			totalWaitTime += finishedTasks.get(i).getWaitTime();		// Calculate total wait time			
		}	


	}

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

	// Method to check for task that are ready to dispatch

	public void checkReady()
	{
		if (waitingTasks.size() > 0)									// Check if any tasks are currently waiting
		{	
			for (int k = 0; k < waitingTasks.size(); k++)				// Loop through waiting tasks
			{
				if (runTime >= waitingTasks.get(k).getArrive())			// Check the waiting task's arrival times against the current runtime
				{
					priority0.add(waitingTasks.get(k));		// Relegate task to next lowest priority queue 
					waitingTasks.remove(waitingTasks.get(k));			// Remove those tasks from the waiting list
					k--;												// Decrement loop count variable
				} 
			}
		}
	}
}
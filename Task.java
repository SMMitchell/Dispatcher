// Programmer: Stephen Mitchell
// Student ID: c3394182
// Last modified: 24 Aug 2022
// Course: COMP2240
// Assignment 1

// This file implements the Task class to store relevant
// data for each task waiting to be run 

import java.util.*;
import java.io.*;

public class Task //implements Comparator<Task>
{
	private String id;			// task ID
	private int arrive;			// task arrival time
	private int execSize;		// task service time
	private int turnTime;		// task turnaround time
	private int waitTime;		// task wait time
	private int totalTime;		// task total run time
	private int tempExecSize;	// temporary task service time
	private	int quantum;		// task's individual quantum for NRR

   // Initialise task variables

    public Task(String taskID, int a, int e)
    {
   		id = taskID;
   		arrive = a;
   		execSize = e;
   		waitTime = 0;
   		turnTime = 0;
   		totalTime = 0;
   		quantum = 0;
   	}

   	// Setter and getter for the task ID
   	
   	public void setID(String id)
   	{
   		this.id = id;
   	}
   	public String getID()
   	{
   		return id;
   	}
   	
	// Setter and getter for the task arrival time

   	public void setArrive(int arrive)
   	{
   		this.arrive = arrive;
   	}
   	public int getArrive()
   	{
   		return arrive;
   	}
   	
   	// Setter and getter for the task service time

   	public void setExecSize(int execSize)
   	{
   		this.execSize = execSize;
   	}
   	public int getExecSize()
   	{
   		return execSize;
   	}
   	
   	// Setter and getter for the turnaround time

   	public void setTurnTime(int completionTime)
   	{
   		turnTime = completionTime - arrive;
   	}
   	public int getTurnTime()
   	{
   		return turnTime;
   	}
   	
  	// Setter and getter for the task wait time

   	public void setWaitTime(int tt)
   	{
   		waitTime = turnTime - execSize;
   	}
   	public int getWaitTime()
   	{
   		return waitTime;
   	}
   	
   	// Setter and getter for the task total run time

   	public void setTotalTime(int completionTime)
   	{
   		totalTime += completionTime;
   	}
   	public int getTotalTime()
   	{
   		return totalTime;
   	}
   	
   	// Setter and getter for the task wait time

   	public void setTempExecSize(int execSize)
   	{
   		tempExecSize = execSize;
   	}
   	public int getTempExecSize()
   	{
   		return tempExecSize;
   	}
   	
   	// Setter and getter for the task wait time

   	/*public void setTempArrive(int execSize)
   	{
   		tempExecSize = execSize;
   	}
   	public int getTempArrive()
   	{
   		return tempExecSize;
   	}*/
   	
   	// Setter and getter for the individual task quantum time

   	public void setQuantum(int q)
   	{
   		quantum = q;
   	}
   	public int getQuantum()
   	{
   		return quantum;
   	}
}

class IDComparator implements Comparator<Task>
{
   	public int compare(Task id1, Task id2)
	{
		String idOne = id1.getID();
		String idTwo = id2.getID();

		return idOne.compareTo(idTwo);
	}
}

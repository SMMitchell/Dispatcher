// Programmer: Stephen Mitchell
// Student ID: c3394182
// Last modified: 24 Aug 2022
// Course: COMP2240
// Assignment 1

// This file implements the Task class to store relevant
// data for each task waiting to be run 

import java.util.*;
import java.io.*;

public class SchedulingInfo
{
   	private int DISP;					// Program dispatch time provided by input file
	private ArrayList<Task> tasks;		// All tasks to be processed

   	// Task data received from the input file

   	public SchedulingInfo(int d, ArrayList<Task> t)
   	{
   		DISP = d;		// Dispatch time
   		tasks = t;		// All tasks
   	}

   	// Setters and getters for dispatch time

   	public void setDispatch(int DISP)
   	{
   		this.DISP = DISP;
   	}
   	public int getDispatch()
   	{
   		return DISP;
   	}

   	// Setters and getters for task list

   	public void setTasks(ArrayList<Task> tasks)
   	{
   		this.tasks = tasks;
   	}
   	public ArrayList<Task> getTasks()
   	{
   		return tasks;
   	}
}
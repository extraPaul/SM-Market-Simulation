package simModel;

import java.util.ArrayList;

public class EmployeesInfo {

	// Attributes
	protected ArrayList<ArrayList<Integer>> schedule;
	protected int[] uTotalEmployees;
	
	protected int numEmpCleaning;
	protected int halfHourNumServed;
	protected int halfHourNumDissatisfied;
	
	// Constructor
	public EmployeesInfo(ArrayList<ArrayList<Integer>> schedule) {
		
		this.schedule = schedule;
		this.numEmpCleaning = 0;
		this.halfHourNumServed = 0;
		this.halfHourNumDissatisfied = 0;
		
		
		
	}
	
	// Required methods to manipulate the objects
	protected void incrementNumEmpCleaning() {
		numEmpCleaning ++;
	}
	protected void decrementNumEmpCleaning() {
		numEmpCleaning --;
	}
	protected void incrementHalfHourNumServed() {
		halfHourNumServed ++;
	}
	protected void incrementHalfHourNumDissatisfied() {
		halfHourNumDissatisfied ++;
	}
	
	
}

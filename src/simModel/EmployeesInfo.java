package simModel;

import java.util.ArrayList;

public class EmployeesInfo {

	// Attributes
	protected ArrayList<int[]> schedule;
	protected int[] halfHourSchedule;
	
	protected int numEmpCleaning;
	protected int halfHourNumServed;
	protected int halfHourNumDissatisfied;
	
	// TODO: check this?
	// are the schedule and halfHourSchedule passed in? 
	// or is the parameter passed in and the schedule and halfHourSchedule derived?
	public EmployeesInfo(ArrayList<int[]> schedule, int[] halfHourSchedule) {
		this.schedule = schedule;
		this.halfHourSchedule = halfHourSchedule;
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

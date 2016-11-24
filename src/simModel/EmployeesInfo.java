package simModel;

public class EmployeesInfo {

	// Attributes
	protected int[][] schedule;
	protected int[] uTotalEmployees;
	
	protected int numEmpCleaning;
	protected int halfHourNumServed;
	protected int halfHourNumDissatisfied;
	
	// Constructor
	public EmployeesInfo(int[][] schedule) {
		
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

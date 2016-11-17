package simModel;

public class EmployeesInfo {

	// Attributes
	protected int[][] schedule;
	protected int[] halfHourSchedule;
	
	protected int numEmpCleaning;
	protected int halfHourNumServed;
	protected int halfHourNumDissatisfied;
	
	// Constructor
	public EmployeesInfo(int[][] schedule) {
		
		this.schedule = schedule;
		this.numEmpCleaning = 0;
		this.halfHourNumServed = 0;
		this.halfHourNumDissatisfied = 0;
		
		// derive halfHourSchedule from schedule
		this.halfHourSchedule = new int[18];
		
		for (int i = 0; i < schedule.length; i++) {
			int startTime = schedule[i][0];
			int durationOfShift = schedule[i][1];
			
			halfHourSchedule[startTime/30]++;
			
			int tempCounter = 1;
			while (durationOfShift > 0) {
				halfHourSchedule[(startTime/30) + tempCounter]++;
				tempCounter++;
				durationOfShift -= 30;
			}
		}
		
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

package smmSimModel;

import java.util.ArrayList;

public class Output 
{
	SMMarket model;
	
	/* SSOVs */
	protected double numServed;
	protected double numDissatisfied;
	protected double[] halfHourPercentDissatisfied = new double[Constants.NUM_HALF_HOUR];
	protected double overallPercentDissatisfied;
	public int numMnFCustomers;
	public int numDeliCustomers;
	public int numBothCustomers;
	public int numBalking; //Represents people who come in and walk out right away.
	protected double totalDurationOfShifts;
	protected double sumOfSrvAndCleaningTime;
	public double employeesIdleTimeRatio;
	protected double scheduleCost;
	
	protected int halfHourNumServed;
	protected int halfHourNumDissatisfied;
	
	//TEST
	//public ArrayList<Double> mnfWaitTimes = new ArrayList<Double>();
	//public ArrayList<Double> deliWaitTimes = new ArrayList<Double>();
	//public ArrayList<Double> bothWaitTimes = new ArrayList<Double>();
	
	// constructor
	protected Output(SMMarket md) { 
		model = md; 
		numServed = 0;
		numDissatisfied = 0;
		overallPercentDissatisfied = 0;
		numMnFCustomers = 0;
		numDeliCustomers = 0;
		numBothCustomers = 0;
		numBalking = 0;
		halfHourNumServed = 0;
		halfHourNumDissatisfied = 0;
		sumOfSrvAndCleaningTime = 9 * 60; // There is 9 hours of cleaning and prep work in total. We add it to the sum right here.
		
	}
	
	/* Helper methods */
	protected void calculateOverallPercentageDissatisfied() {
		overallPercentDissatisfied = numDissatisfied/numServed;
	}
	
	protected double getOverallPercentageDissatisfied() {
		calculateOverallPercentageDissatisfied();
		return overallPercentDissatisfied;
	}
	
	protected void calculateTotalDurationOfShifts() {
		totalDurationOfShifts = 4 * 6.5 * 60; // total duration of shifts for 4 full-time employees
		for (int i = 0; i < model.rEmployeesInfo.schedule.size(); i++) {
			int shiftDuration = model.rEmployeesInfo.schedule.get(i).get(1);
			totalDurationOfShifts += shiftDuration;
		}
	}
	
	protected void calculateEmployeesIdleTimeRatio() {
		calculateTotalDurationOfShifts();
		employeesIdleTimeRatio = 1 - sumOfSrvAndCleaningTime / totalDurationOfShifts;
	}
	
	protected void calculateScheduleCost()
	{
		scheduleCost = 0;
		for (int i = 0; i < model.rEmployeesInfo.schedule.size(); i ++)
		{
			int shiftDuration = model.rEmployeesInfo.schedule.get(i).get(1);
			double paySlip = ((double)shiftDuration/60.0) * Constants.PTIMERATE;
			scheduleCost += paySlip;
		}
		//Total pay for full time employees for one day's work
		scheduleCost +=  (6.5 * Constants.FTIMERATE) * 4;
	}
	
	public double getTotalDurationOfShifts() {
		calculateTotalDurationOfShifts();
		return totalDurationOfShifts;
	}
	
	public double getEmployeesIdleTimeRatio() {
		calculateEmployeesIdleTimeRatio();
		return employeesIdleTimeRatio;
	}
	
	protected double getScheduleCost(){
		calculateScheduleCost();
		return scheduleCost;
	}
	
	protected void addToSumOfSrvAndCleaningTime(double value) {
		sumOfSrvAndCleaningTime += value;
	}
	
	protected void incrementHalfHourNumServed() {
		halfHourNumServed ++;
	}
	protected void incrementHalfHourNumDissatisfied() {
		halfHourNumDissatisfied ++;
	}
	
	protected void resetHalfHourStats() {
		halfHourNumServed = 0;
		halfHourNumDissatisfied = 0;
	}

}

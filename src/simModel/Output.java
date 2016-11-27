package simModel;

public class Output 
{
	SMMarket model;
	
	/* SSOVs */
	protected double numServed;
	protected double numDissatisfied;
	protected double[] halfHourPercentDissatisfied = new double[18];
	protected double overallPercentDissatisfied;
	public int numMnFCustomers;
	public int numDeliCustomers;
	public int numBothCustomers;
	public int numBalking; //Represents people who come in and walk out right away.
	protected double scheduleCost;
	
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
	}
	
	/* Helper methods */
	protected void calculateOverallPercentageDissatisfied() {
		overallPercentDissatisfied = numDissatisfied/numServed;
	}
	
	protected double getOverallPercentageDissatisfied() {
		calculateOverallPercentageDissatisfied();
		return overallPercentDissatisfied;
	}
	
	protected void calculateScheduleCost()
	{
		for (int i = 0; i < model.rEmployeesInfo.schedule.size(); i ++)
		{
			int shiftDuration = model.rEmployeesInfo.schedule.get(i).get(1);
			double paySlip = shiftDuration * Constants.PTIMERATE;
			scheduleCost += paySlip;
		}
		//Total pay for full time employees for one day's work
		scheduleCost +=  (6.5 * Constants.FTIMERATE) * 4;
	}
	
	protected double getScheduleCost(){
		calculateScheduleCost();
		return scheduleCost;
	}
	
}

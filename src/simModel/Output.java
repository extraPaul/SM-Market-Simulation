package simModel;

class Output 
{
	SMMarket model;
	
	/* SSOVs */
	protected int numServed;
	protected int numDissatisfied;
	protected double[] halfHourPercentDissatisfied = new double[18];
	protected double overallPercentDissatisfied;
	
	// constructor
	protected Output(SMMarket md) { 
		model = md; 
		numServed = 0;
		numDissatisfied = 0;
		overallPercentDissatisfied = 0;
	}
	
    // Use OutputSequence class to define Trajectory and Sample Sequences
    // Trajectory Sequences

    // Sample Sequences

    // DSOVs available in the OutputSequence objects
    // If separate methods required to process Trajectory or Sample
    // Sequences - add them here
	
	
	/* Helper methods */
	protected void calculateOverallPercentageDissatisfied() {
		overallPercentDissatisfied = numDissatisfied/numServed;
	}
	
	protected double getOverallPercentageDissatisfied() {
		
		calculateOverallPercentageDissatisfied();
		return overallPercentDissatisfied;
	}
	
}

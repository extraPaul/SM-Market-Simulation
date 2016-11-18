package simModel;

class Output 
{
	SMMarket model;
	
	protected Output(SMMarket md) { model = md; }
    // Use OutputSequence class to define Trajectory and Sample Sequences
    // Trajectory Sequences

    // Sample Sequences

    // DSOVs available in the OutputSequence objects
    // If seperate methods required to process Trajectory or Sample
    // Sequences - add them here

	/* SSOVs */
	protected int numServed;
	protected int numDissatisfied;
	protected double[] halfHourPercentDissatisfied = new double[18];
	
	/* DSOVs */
	protected double overallPercentDissatisfied;
	
}

package simModel;

import simulationModelling.ConditionalActivity;

public class Serving extends ConditionalActivity{

	private Customer icCustomer;
	SMMarket model;
	
	// Constructor
	public Serving(SMMarket model) {
		this.model = model;
	}
	
	protected static boolean precondition(SMMarket simModel) {
		//TODO: implement this
		return true;
	}
	
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		
	}

	
	
}
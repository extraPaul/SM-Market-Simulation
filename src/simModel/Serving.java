package simModel;

import simulationModelling.ConditionalActivity;

public class Serving extends ConditionalActivity{

	SMMarket model;
	private Customer icCustomer;
	int id; //identifier for RG.Counters and Q.CustomerLines
	
	// Constructor
	public Serving(SMMarket model) {
		this.model = model;
	}
	
	protected static boolean precondition(SMMarket simModel) {
		//TODO: implement this
		boolean ready = false;
		if (simModel.udp.counterReadyToServe() != Constants.NONE) {
			ready = true;
		}
		
		return ready;
		
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

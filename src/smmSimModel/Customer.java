package smmSimModel;

/**
 * @author Saman
 *
 */
class Customer {
	// Time a customer enters the store
	double startWaitTime;
	// M = Meat & Fish; D = Deli; MD = Both counters
	enum Type {M, D, MD};
	// Type of a customer
	Type uType;
	/* canLeave indicates whether a customer will leave after being served at a counter
	 * hence, canLeave will only be false when a customer is of type MD (visits both counters), and
	 * hasn't visited any counters yet.
	 */
	boolean canLeave = true;
	// The wait time at which the customer will be dissatisfied
	double dissatisfactionThreshold;
	
	public Customer(SMMarket model){
		uType = model.rvp.uCustomerType();
		startWaitTime = model.getClock();
		dissatisfactionThreshold = model.rvp.uDissatisfactionTime(uType);
	}
}

package simModel;

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
	// canLeave indicates whether a customer will leave after being served at a counter
	boolean canLeave = true;
	// The wait time at which the customer will be dissatisfied
	double dissatisfactionThreshold;
}

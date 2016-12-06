package smmSimModel;

import java.util.HashSet;

public class Counter {
	
	// Attributes
	protected int uNumEmp; //Number of employees - input variable
	protected HashSet<Customer> list = new HashSet<Customer>(); // The group of customer entities being served concurrently at the counter
	//For testing
	public int[] dailyNumEmp = new int[18];
	//Keeps track of employees that need to change counters when they're done serving.
	//Prevents employees from switching counters while they're serving someone.
	protected int scheduledEmpChange = 0;
	
	// Required methods
	protected void insertList(Customer icCustomer) {
		list.add(icCustomer);
	}
	
	protected boolean removeList(Customer icCustomer) {
		return (list.remove(icCustomer));
	}
	
	protected int getN() {
		return list.size();
	}
	
	
}

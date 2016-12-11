/* CSI4124/SYS5110 – Foundations of Modeling and Simulation
 * SM Market - Simulation Project
 * Fall 2016
 * 
 * Team Members: 
 * Paul Laplante
 * Saman Daneshvar
 * Matthew Gordon Yaraskavitch
 * Toluwalase Olufowobi
 * Ekomabasi Ukpong
 * Qufei Chen
 */

package smmSimModel;

import java.util.HashSet;

public class Counter {
	
	// Attributes
	protected int uNumEmp; //Number of employees - input variable
	protected HashSet<Customer> list = new HashSet<Customer>(); // The group of customer entities being served concurrently at the counter
	//For testing
	public int[] dailyNumEmp = new int[Constants.NUM_HALF_HOUR];
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

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

import java.util.ArrayList;

public class EmployeesInfo {

	// Attributes
	protected ArrayList<ArrayList<Integer>> schedule;
	//Public for testing
	public int[] uTotalEmployees;
	
	protected int numEmpCleaning;
	
	// Constructor
	public EmployeesInfo(ArrayList<ArrayList<Integer>> schedule, SMMarket model) {
		
		this.schedule = schedule;
		this.numEmpCleaning = 0;
		this.uTotalEmployees = new int[Constants.NUM_HALF_HOUR];
		
	}
	
	// Required methods to manipulate the objects
	protected void incrementNumEmpCleaning() {
		numEmpCleaning ++;
	}
	protected void decrementNumEmpCleaning() {
		numEmpCleaning --;
	}
	
	
}

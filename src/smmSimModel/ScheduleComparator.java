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
import java.util.Comparator;

/*
 * Custom comparator object used to compare the start time of shifts
 * Used to sort the schedule 
 * 
 */

public class ScheduleComparator implements Comparator<ArrayList<Integer>> {

	@Override
	public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
		// return 0 if objects are equal
		// returns positive number if o1 is larger than o2
		// else returns neg number
		
		if (o1.get(0) > o2.get(0)){
			return 1;
		} else if (o2.get(0) > o1.get(0)) {
			return -1; 
		} else {
			return 0;
		}
	}
	
}
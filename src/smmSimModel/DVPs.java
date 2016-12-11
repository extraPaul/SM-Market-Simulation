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

class DVPs 
{
	SMMarket model;  // for accessing the clock
	
	// Constructor
	protected DVPs(SMMarket model) { this.model = model; }
	private int nxtTime = 0;

	protected double RuTotalEmployees() {
		double time = nxtTime;
		nxtTime += 30;
		if (time >= 540 || model.implicitStopCondition()) {
			time = -1;
		}
		return(time);
	}
	
}

package smmSimModel;

import simulationModelling.ScheduledAction;

public class StaffRearrange extends ScheduledAction {

	SMMarket model;
	DVPs dvp;
	
	public StaffRearrange(SMMarket model) {
		this.model = model;
		this.dvp = new DVPs(model);
	}
	
	@Override
	protected double timeSequence() {
		return dvp.RuTotalEmployees();
	}

	@Override
	protected void actionEvent() {
		
		//TEST
 	   //System.out.println("\nSTAFF REARRANGE STARTED \n\n");
		
		if(model.getClock() == 0){
			model.rEmployeesInfo.numEmpCleaning += 3;
			int numEmp = model.rEmployeesInfo.uTotalEmployees[0] - 3;
			model.rgCounters.get(Constants.DELI).uNumEmp = numEmp/2;
			model.rgCounters.get(Constants.MNF).uNumEmp = numEmp - numEmp/2;
		} else {
			int numExtraEmp = model.rEmployeesInfo.uTotalEmployees[(int)(model.getClock()/30)] - model.rEmployeesInfo.uTotalEmployees[(int)(model.getClock()/30) - 1];
			if(numExtraEmp > 0 || Math.abs(numExtraEmp) <= model.rgCounters.get(Constants.MNF).uNumEmp){
				model.rgCounters.get(Constants.MNF).uNumEmp += numExtraEmp;
			} else if(numExtraEmp < 0){
				//numExtraEmp is negative
				model.rgCounters.get(Constants.DELI).uNumEmp += numExtraEmp + model.rgCounters.get(Constants.MNF).uNumEmp;
				model.rgCounters.get(Constants.MNF).uNumEmp = 0;
			}
		}
		
		//Take away employees for cleaning/prep work/restocking, or put them back on counters.
		model.udp.assignCleaningDuty();
		
		// Rebalance the employees between two counters, based on the length of the lines
		model.udp.rebalanceEmployees();
		
		// update the updateOutputOnHalfHour()
		model.udp.updateOutputOnHalfHour();
		
	}
}

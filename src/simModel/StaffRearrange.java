package simModel;

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
		if(model.getClock() == 0){
			int numEmp = model.rEmployeesInfo.uTotalEmployees[0] - 3;
			model.rEmployeesInfo.numEmpCleaning += 3;
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
		
		//This might need to be changed depending on how
		//time is incremented.
		//Using t >= 120 won't work because then this will
		//be repeated multiple times.
		if(model.getClock() == 120){
			//put prep employees back to counters
			model.rEmployeesInfo.numEmpCleaning = 0;
			model.rgCounters.get(Constants.MNF).uNumEmp++;
			model.rgCounters.get(Constants.DELI).uNumEmp += 2;
		}
		
		if(model.getClock() == 270){
			//put remove employee from counter for restocking.
			if(model.rgCounters.get(Constants.DELI).uNumEmp > 0)
				model.rgCounters.get(Constants.DELI).uNumEmp--;
			else
				model.rgCounters.get(Constants.MNF).uNumEmp--;
			model.rEmployeesInfo.incrementNumEmpCleaning();
		}
		
		if(model.getClock() == 450){
			//put prep employee back to counter
			model.rEmployeesInfo.numEmpCleaning = 0;
			model.rgCounters.get(Constants.MNF).uNumEmp++;
		}
		
		// Rebalance the employees between two counters, based on the length of the lines
		model.udp.rebalanceEmployees();
		
		// update the updateOutputOnHalfHour()
		model.udp.updateOutputOnHalfHour();
		
	}
}

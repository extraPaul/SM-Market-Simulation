package simModel;

class DVPs 
{
	SMMarket model;  // for accessing the clock
	
	// Constructor
	protected DVPs(SMMarket model) { this.model = model; }

	// Translate deterministic value procedures into methods
        /* -------------------------------------------------
	                       Example
	protected double getEmpNum()  // for getting next value of EmpNum(t)
	{
	   double nextTime;
	   if(model.clock == 0.0) nextTime = 90.0;
	   else if(model.clock == 90.0) nextTime = 210.0;
	   else if(model.clock == 210.0) nextTime = 420.0;
	   else if(model.clock == 420.0) nextTime = 540.0;
	   else nextTime = -1.0;  // stop scheduling
	   return(nextTime);
	}
	------------------------------------------------------------*/
	
	protected int RuTotalEmployees(){
		//devide curent time by 18, because there are 18 slots.
		int index = (int)(model.getClock()/18);
		return model.rEmployeesInfo.uTotalEmployees[index];
	}
	
}

package simModel;

class DVPs 
{
	SMMarket model;  // for accessing the clock
	
	// Constructor
	protected DVPs(SMMarket model) { this.model = model; }
	private double[] staffRearangeTimeSeq = {0,30,60,90,120,150,180,210,240,270,300,330,360,390,420,450,480,510,540,-1};
	private int sctIx = 0;

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
	
	protected double RuTotalEmployees(){
		double nxtTime = staffRearangeTimeSeq[sctIx];
		sctIx++;
		return(nxtTime);
	}
	
}

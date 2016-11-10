package simModel;

import cern.jet.random.engine.RandomSeedGenerator;

public class Seeds 
{
	int seed1;   // comment 1
	int seed2;   // comment 2
	int seed3;   // comment 3
	int seed4;   // comment 4

	public Seeds(RandomSeedGenerator rsg)
	{
		seed1=rsg.nextSeed();
		seed2=rsg.nextSeed();
		seed3=rsg.nextSeed();
		seed4=rsg.nextSeed();
	}
}

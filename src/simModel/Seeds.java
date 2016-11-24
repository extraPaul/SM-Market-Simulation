package simModel;

import cern.jet.random.engine.RandomSeedGenerator;

public class Seeds 
{
	int interArrivSd;   // comment 1
	int deliSd;   // comment 2
	int mChooserSd;   // comment 3
	int mDist1;   // comment 4
	int mDist2;

	public Seeds(RandomSeedGenerator rsg)
	{
		interArrivSd=rsg.nextSeed();
		deliSd=rsg.nextSeed();
		mChooserSd=rsg.nextSeed();
		mDist1=rsg.nextSeed();
		mDist2=rsg.nextSeed();
	}
}

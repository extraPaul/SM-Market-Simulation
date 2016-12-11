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

import cern.jet.random.engine.RandomSeedGenerator;

public class Seeds 
{
	int interArrivSd;
	int type;
	int deliSd;
	int mChooserSd;
	int mDist1;
	int mDist2;

	public Seeds(RandomSeedGenerator rsg)
	{
		interArrivSd=rsg.nextSeed();
		type=rsg.nextSeed();
		deliSd=rsg.nextSeed();
		mChooserSd=rsg.nextSeed();
		mDist1=rsg.nextSeed();
		mDist2=rsg.nextSeed();
	}
}

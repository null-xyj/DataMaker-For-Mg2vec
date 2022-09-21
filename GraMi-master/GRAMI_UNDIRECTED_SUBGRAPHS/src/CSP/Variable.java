/**
 * Copyright 2014 Mohammed Elseidy, Ehab Abdelhamid

This file is part of Grami.

Grami is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 2 of the License, or
(at your option) any later version.

Grami is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with Grami.  If not, see <http://www.gnu.org/licenses/>.
 */

package CSP;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import search.Searcher;
import utilities.MyPair;

import dataStructures.myNode;

public class Variable 
{
	private HashMap<Integer, myNode> list;
	private int label;
	private int ID;  //represents patternNodeID
	private ArrayList<MyPair<Integer, Double>> distanceConstrainedWith;
	private boolean newVar = false;
	
	private HashSet<Integer> labelDistanceConstrainedWith;
	
	public void setDistanceConstrainedWith(
			ArrayList<MyPair<Integer, Double>> distanceConstrainedWith) {
		this.distanceConstrainedWith = distanceConstrainedWith;
	}
	
	public Variable(int ID, int label,HashMap<Integer, myNode> list,ArrayList<MyPair<Integer, Double>> cons) 
	{
		this.list=list;
		this.label=label;
		this.ID=ID;
		if(cons==null)
			distanceConstrainedWith= new ArrayList<MyPair<Integer, Double>>();
		else
			distanceConstrainedWith=cons;
	}
	
	public int getConstraintDegree()
	{
		return distanceConstrainedWith.size();
	}
	
	public void setList(HashMap<Integer, myNode> list)
	{
		this.list=list;
	}
	
	public int getListSize()
	{
		return list.size();
	}

	public void addConstraintWith(int vb, double edgeLabel)
	{
		distanceConstrainedWith.add(new MyPair<Integer, Double>(vb, edgeLabel));
	}
	
	public ArrayList<MyPair<Integer, Double>> getDistanceConstrainedWith() {
		return distanceConstrainedWith;
	}

	public HashMap<Integer, myNode> getList() {
		return list;
	}

	public int getLabel() {
		return label;
	}

	public int getID() {
		return ID;
	}
	
	public HashSet<Integer> getLabelsDistanceConstrainedWith()
	{
		if(labelDistanceConstrainedWith==null)
		{
			labelDistanceConstrainedWith= new HashSet<Integer>();
			labelDistanceConstrainedWith.addAll(Searcher.neighborLabels.get(this.getLabel()));
		}
		return labelDistanceConstrainedWith;
	}
	
	public void setNew()
	{
		this.newVar = true;
	}
	
	public void setOld()
	{
		this.newVar = false;
	}
	
	public boolean isNew()
	{
		return this.newVar;
	}

}

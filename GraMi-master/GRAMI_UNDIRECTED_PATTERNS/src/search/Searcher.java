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

package search;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import statistics.Statistics;
import utilities.Settings;
import utilities.StopWatch;
import Dijkstra.*;
import dataStructures.DFSCode;
import dataStructures.DFScodeSerializer;
import dataStructures.GSpanEdge;
import dataStructures.Graph;
import dataStructures.HPListGraph;
import dataStructures.IntFrequency;
import dataStructures.gEdgeComparator;
import dataStructures.myNode;

public class Searcher<NodeType, EdgeType> 
{
	private Graph singleGraph;
	private IntFrequency freqThreshold;
	private int distanceThreshold;
	private ArrayList<Integer> sortedFrequentLabels;
	Map<GSpanEdge<NodeType, EdgeType>, DFSCode<NodeType, EdgeType>> initials;
	private int type;
	public double dijkestraTime;
	public ArrayList<HPListGraph<NodeType, EdgeType>> result;
	public static Hashtable<Integer, Vector<Integer>> neighborLabels;//a map of label to a list of neighboring labels
	
	public Searcher(String path, int freqThreshold,int shortestDistance) throws Exception
	{
		this.freqThreshold= new IntFrequency(freqThreshold);
		this.distanceThreshold=shortestDistance;
		singleGraph = new Graph(1,freqThreshold);
		singleGraph.loadFromFile(path);
		sortedFrequentLabels=singleGraph.getSortedFreqLabels();
		if(distanceThreshold==1)
		{
			singleGraph.printFreqNodes();
        	singleGraph.setShortestPaths_1hop();
		}
		else
		{
			DenseRoutesMap x = new DenseRoutesMap(singleGraph);
			DijkstraEngine d = new DijkstraEngine(x,shortestDistance);
			singleGraph.printFreqNodes();
			singleGraph.setShortestPaths(d, shortestDistance);
		}
	}
	
	public void initialize()
	{
		initials= new TreeMap<GSpanEdge<NodeType, EdgeType>, DFSCode<NodeType, EdgeType>>(new gEdgeComparator<NodeType, EdgeType>());
		HashMap<Integer, HashMap<Integer,myNode>> freqNodesByLabel=  singleGraph.getFreqNodesByLabel();
		HashSet<Integer> contains= new HashSet<Integer>();
		for (Iterator<  java.util.Map.Entry< Integer, HashMap<Integer,myNode> > >  it= freqNodesByLabel.entrySet().iterator(); it.hasNext();) 
		{
			
			java.util.Map.Entry< Integer, HashMap<Integer,myNode> > ar =  it.next();
			int firstLabel=ar.getKey();
			contains.clear();
			HashMap<Integer,myNode> tmp = ar.getValue();
			for (Iterator<myNode> iterator = tmp.values().iterator(); iterator.hasNext();) 
			{
				myNode node =  iterator.next();
				HashMap<Integer, ArrayList<Integer>> neighbours=node.getReachableWithNodes();
				if(neighbours!=null)
				for (Iterator<Integer>  iter= neighbours.keySet().iterator(); iter.hasNext();) 
				{
					int secondLabel = iter.next();
					int labelA=sortedFrequentLabels.indexOf(firstLabel);
					int labelB=sortedFrequentLabels.indexOf(secondLabel);
					final GSpanEdge<NodeType, EdgeType> gedge = new GSpanEdge <NodeType, EdgeType>().set(0, 1, labelA, 0, labelB, 0, firstLabel, secondLabel);
					
					if(!initials.containsKey(gedge))
					{
						final ArrayList<GSpanEdge<NodeType, EdgeType>> parents = new ArrayList<GSpanEdge<NodeType, EdgeType>>(2);
						parents.add(gedge);
						parents.add(gedge);
						
						HPListGraph<NodeType, EdgeType> lg = new HPListGraph<NodeType, EdgeType>();
						gedge.addTo(lg);
						DFSCode<NodeType, EdgeType> code = new DFSCode<NodeType,EdgeType>(sortedFrequentLabels,singleGraph,null).set(lg, gedge, gedge, parents);
						
						initials.put(gedge, code);
					}
				}
			}
		}
		
		for (final Iterator<Map.Entry<GSpanEdge<NodeType, EdgeType>, DFSCode<NodeType, EdgeType>>> eit = initials
				.entrySet().iterator(); eit.hasNext();) {
			final DFSCode<NodeType, EdgeType> code = eit.next().getValue();
			if (freqThreshold.compareTo(code.frequency()) > 0) {
				eit.remove();
			}
			else
				;
		}
		
		neighborLabels = new Hashtable();
		
		for (final Iterator<Map.Entry<GSpanEdge<NodeType, EdgeType>, DFSCode<NodeType, EdgeType>>> eit = initials
				.entrySet().iterator(); eit.hasNext();) 
		{
			final DFSCode<NodeType, EdgeType> code = eit.next().getValue();
			
			int labelA;
			int labelB;
			GSpanEdge<NodeType, EdgeType> edge = code.getFirst();
			labelA = edge.getThelabelA();
			labelB = edge.getThelabelB();
			
			//add to labels
			Vector temp = neighborLabels.get(labelA);
			if(temp==null)
			{
				temp = new Vector();
				neighborLabels.put(labelA, temp);
			}
			temp.addElement(labelB);
			
			//now the reverse
			temp = neighborLabels.get(labelB);
			if(temp==null)
			{
				temp = new Vector();
				neighborLabels.put(labelB, temp);
			}
			temp.addElement(labelA);
		}
		
		
	}
	
	public void search()
	{
		Algorithm<NodeType, EdgeType> algo = new Algorithm<NodeType, EdgeType>();
		algo.setInitials(initials);
		RecursiveStrategy<NodeType, EdgeType> rs = new RecursiveStrategy<NodeType, EdgeType>();
		result= (ArrayList<HPListGraph<NodeType, EdgeType>>)rs.search(algo,this.freqThreshold.intValue());
	}
	
	private int getNumOfDistinctLabels(HPListGraph<NodeType, EdgeType> list)
    {
        HashSet<Integer> difflabels= new HashSet<Integer>();
        for (int i = 0; i < list.getNodeCount(); i++) 
        {
            int label= (Integer)list.getNodeLabel(i);
            if(!difflabels.contains(label))
                difflabels.add(label);
        }
        
        return difflabels.size();
    }
	
	public Graph getSingleGraph()
	{
		return singleGraph;
	}
	
}

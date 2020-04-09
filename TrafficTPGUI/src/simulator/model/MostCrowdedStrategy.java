package simulator.model;

import java.util.Iterator;
import java.util.List;

import simulator.model.simulatedOBJ.Road;
import simulator.model.simulatedOBJ.Vehicle;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	private int timeSlot;
	public MostCrowdedStrategy(int timeSlot){
		this.timeSlot=timeSlot;
	};
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, 
			int lastSwitchingTime,int currTime) {
		if(roads.isEmpty()) return -1; //empty
		if(currGreen==-1) { //all red
			Iterator<List<Vehicle>> it= qs.iterator();
			int max= -1;
			while(it.hasNext()) {
				int aux= it.next().size();
				if(aux>max) max= aux;
			}
			return max;
		}
		if(currTime-lastSwitchingTime<timeSlot) return currGreen;
		int i= (currGreen +1)%roads.size(),initial =i, max=-1;
		boolean finish=false;
		while(!finish) {
			int aux= qs.get(i).size();
			if(aux>max) max= aux;
			++i;
			if(i==initial) finish=true;
			if(i==qs.size()) i=0;
		}
		return max;
	}

}

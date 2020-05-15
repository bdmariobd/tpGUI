package simulator.model;
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
			int maxSize=-1,pos=0;
			for(int i=0;i<qs.size();++i) {
				if(qs.get(i).size()>maxSize) {
					pos=i;
					maxSize=qs.get(i).size();
				}
			}
			return pos;
		}
		if(currTime-lastSwitchingTime<timeSlot) return currGreen;
		int i= (currGreen +1)%roads.size(),initial =i, maxSize=-1,pos=i;
		boolean finish=false;
		while(!finish) {
			int aux= qs.get(i).size();
			if(aux>maxSize) {maxSize= aux;pos=i;}
			i= (i+1)%roads.size();
			if(i==initial) finish=true;
			
		}
		return pos;
	}

}

package simulator.model;

import simulator.exceptions.IncorrectValues;
import simulator.model.simulatedOBJ.InterCityRoad;
import simulator.model.simulatedOBJ.Road;

public class NewInterCityRoadEvent extends NewRoadEvent {
	
	
	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	Road createRoadObject() throws IncorrectValues {
		return new InterCityRoad(id, src, dest,maxSpeed, co2Limit, length, weather);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "New Inter City Road '" + id + "'" ;
	}
}

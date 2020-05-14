package simulator.model.simulatedOBJ;

import simulator.exceptions.IncorrectValues;
import simulator.model.Weather;

public class CityRoad extends Road {

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather)
			throws IncorrectValues {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		setTotalCont(getTotalCont()-getWeather().getContCity());
		if(getTotalCont()<0) setTotalCont(0); 
	}

	@Override
	void updateSpeedLimit() {
		//cityRoad, nunca cambia
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		double p2 =(11.0-v.getContClass())/11.0*getActualMaxSpeed();
		if(p2>v.getMaxSpeed()) return v.getMaxSpeed();
		return (int)p2;
		
	}

}

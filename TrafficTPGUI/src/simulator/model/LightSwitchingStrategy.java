package simulator.model;

import java.util.List;

import simulator.model.simulatedOBJ.Road;
import simulator.model.simulatedOBJ.Vehicle;

public interface LightSwitchingStrategy {
	int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, 
			int currGreen, int lastSwitchingTime, int currTime);
}

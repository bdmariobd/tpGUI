package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.model.simulatedOBJ.Vehicle;

public class MoveAllStrategy implements DequeuingStrategy {
	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> v = new ArrayList<Vehicle>();
		v.addAll(q);
		return v;
	}

}

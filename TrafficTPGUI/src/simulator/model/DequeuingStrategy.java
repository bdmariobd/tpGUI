package simulator.model;

import java.util.List;

import simulator.model.simulatedOBJ.Vehicle;

public interface DequeuingStrategy {
	List<Vehicle> dequeue(List<Vehicle> q);

}

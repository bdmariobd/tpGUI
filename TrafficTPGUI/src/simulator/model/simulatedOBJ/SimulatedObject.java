package simulator.model.simulatedOBJ;

import org.json.JSONObject;

import simulator.exceptions.IncorrectValues;

public abstract class SimulatedObject  {

	protected String _id;

	SimulatedObject(String id) {
		_id = id;
	}

	public String getId() {
		return _id;
	}
	@Override
	public String toString() {
		return _id;
	}

	abstract void advance(int time) throws IncorrectValues;

	abstract public JSONObject report();

	public int compareTo(SimulatedObject o) {
		return 0;
	}
}

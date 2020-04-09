package simulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import simulator.exceptions.IncorrectValues;
import simulator.model.simulatedOBJ.Junction;
import simulator.model.simulatedOBJ.Vehicle;


public class NewVehicleEvent extends Event {
	private String id;
	private List<String> itinerary;
	private int maxSpeed,contClass;
	private List<Junction> itineraryJunc;
	
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id=id;
		this.maxSpeed=maxSpeed;
		this.contClass=contClass;
		this.itinerary=itinerary;
		itineraryJunc= new ArrayList<Junction>();
	}
	@Override
	void execute(RoadMap map) throws IncorrectValues {
		Iterator <String> i = itinerary.iterator();
		while(i.hasNext()) {
			String aux= i.next();
			itineraryJunc.add(map.getJunction(aux));
		}
		Vehicle v=new Vehicle(id, maxSpeed, contClass, itineraryJunc);
		map.addVehicle(v);
		v.moveToNextRoad();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "New Vehicle '" + id + "'" ;
	}
}

package simulator.model.simulatedOBJ;



import org.json.JSONObject;
import simulator.exceptions.IncorrectValues;
import simulator.model.VehicleStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle> { 
	private List<Junction> itinerary;
	private int maxSpeed,Speed, location,contClass,totalContClass,totalDistance;
	private VehicleStatus status;
	private Road road;
	private int itineraryIndex;
	
	public Vehicle(String id, int maxSpeed, int contClass,	List<Junction> itinerary) throws IncorrectValues{
		super(id);
		correctValues(maxSpeed, contClass, itinerary);
		this.maxSpeed= maxSpeed;
		this.contClass=contClass;
		totalContClass=0;
		this.itinerary=Collections.unmodifiableList(new ArrayList<>(itinerary));
		status= VehicleStatus.PENDING;
		itineraryIndex=0;
	}
	void correctValues(int maxSpeed, int contClass,	List<Junction> itinerary) throws IncorrectValues {
		if(maxSpeed<0) throw new IncorrectValues("Negative Speed");
		if(contClass>10 && contClass<0) throw new IncorrectValues("Incorrect contClass");
		if(itinerary.size()<2) throw new IncorrectValues("Not enought Junctions");
	}
	Boolean initial() {
		return location==0 && Speed==0;
	}
	void enterARoad() throws IncorrectValues {
		road.enter(this);
	}
	public Road getRoad() {
		return road;
	}
	public void setRoad(Road road) {
		this.road = road;
	}
	void setSpeed(int speed) throws IncorrectValues {
		if(speed<0) throw new IncorrectValues("Negative Speed");
		if(status==VehicleStatus.TRAVELING) Speed = speed;
	}
	public void setContClass(int contClass) throws IncorrectValues {
		if(contClass>10 && contClass<0) throw new IncorrectValues("Incorrect contClass");
		this.contClass = contClass;
	}
	@Override
	void advance(int time) throws IncorrectValues {
		if(status==VehicleStatus.TRAVELING) {
			int aux= location;
			location= Integer.min(location+Speed, road.getLength()); //distancia de road
			totalDistance+=(location-aux);
			int c = contClass * (location-aux);
			totalContClass+= c;
			road.addContamination(c);
			if(location == road.getLength()) {
				road.getDestJunc().enter(this);
				status=VehicleStatus.WAITING;
				Speed=0;
				++itineraryIndex;
			}
		}
	}
	public void moveToNextRoad() throws IncorrectValues {		
		if(status ==VehicleStatus.PENDING) { //primera vez
			Junction j = itinerary.get(itineraryIndex);
			road = j.roadTo(itinerary.get(itineraryIndex+1));
			road.enter(this);
			status =VehicleStatus.TRAVELING;
			location=0;
		}
		else if(status ==VehicleStatus.WAITING) { //resto de veces
			Junction j = itinerary.get(itineraryIndex);
			Road ant= road;
			if(itineraryIndex+1>=itinerary.size()) {
				status =VehicleStatus.ARRIVED; 
				ant.exit(this);
			}
			else {
				road = j.roadTo(itinerary.get(itineraryIndex+1));
				location=0;
				status =VehicleStatus.TRAVELING;
				road.enter(this);
				ant.exit(this);
			}
		}
		else throw new IncorrectValues("Vehicle cant move");
	}
	@Override
	public JSONObject report() {
		JSONObject jo= new JSONObject();
		jo.put("id", this._id);
		if(status!=VehicleStatus.TRAVELING && Speed!=0) jo.put("speed",0); 
		else jo.put("speed",this.Speed);
		jo.put("distance",this.totalDistance);
		jo.put("co2", this.totalContClass);
		jo.put("class", this.contClass);
		jo.put("status",this.status.toString());
		jo.put("road", this.road.getId());
		jo.put("location", this.location);
		return jo;
	}
	@Override
	public int compareTo(Vehicle v) {
		if(this.location==v.location) return 0;
		if(this.location<v.location) return -1;
		else return 1;
	}
	public int getContClass() {
		return contClass;
	}
	public boolean validItinerary() {
		Iterator<Junction> i = itinerary.iterator();
		while(i.hasNext()) {
			Junction aux = i.next();
			if(aux.roadTo(i.next())==null) return false;
		}
		return true;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public String toString() {
		return this._id;
	}
	public VehicleStatus getStatus() {
		// TODO Auto-generated method stub
		return status;
	}
	public int getLocation() {
		// TODO Auto-generated method stub
		return location;
	}
	public String getIterinary() {
		String r = "[";
		for(int i=0;i<itinerary.size()-1;++i) {
			r+= itinerary.get(i).getId()+",";
		}
		r+=itinerary.get(itinerary.size()-1)+"]";
		return r;
	}
	public int getSpeed() {
		// TODO Auto-generated method stub
		return Speed;
	}
	public int getTotalCO2() {
		// TODO Auto-generated method stub
		return totalContClass;
	}
	public int getDistance() {
		// TODO Auto-generated method stub
		return totalDistance; 
	}
	
	
}

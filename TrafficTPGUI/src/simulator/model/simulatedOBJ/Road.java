package simulator.model.simulatedOBJ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.IncorrectValues;
import simulator.model.Weather;

public abstract class Road extends SimulatedObject {
	private Junction srcJunc, destJunc;
	private int length,maxSpeed,actualMaxSpeed, contLimit,totalCont;
	private Weather weather;
	private List<Vehicle> vehicleList;
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws IncorrectValues {
		super(id);
		if(maxSpeed<0) throw new IncorrectValues("Negative Speed");
		if(length<0) throw new IncorrectValues("Negative length");
		if(contLimit<0) throw new IncorrectValues("Negative contLimit");
		if(srcJunc==null || destJunc==null) throw new IncorrectValues("Null junction");
		if(weather==null)throw new IncorrectValues("Null weather");
		this.srcJunc=srcJunc;
		this.destJunc=destJunc;
		destJunc.addIncommingRoad(this);
		srcJunc.addOutGoingRoad(this);
		this.maxSpeed=maxSpeed;
		this.actualMaxSpeed= maxSpeed;
		this.contLimit=contLimit;
		this.length=length;
		this.weather=weather;
		vehicleList= new ArrayList<Vehicle>();
		totalCont=0;
	}
	void enter(Vehicle v) throws IncorrectValues {
		if(!v.initial()) throw new IncorrectValues("Unable to join the road");
		vehicleList.add(v);
	}
	@Override
	public void advance(int time) throws IncorrectValues {
		reduceTotalContamination();
		updateSpeedLimit();
		for(Vehicle v : vehicleList) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
		Collections.sort(vehicleList);
	}
	public Junction getSrcJunc() {
		return srcJunc;
	}
	void exit(Vehicle v) {
		vehicleList.remove(v);
	}
	public void setWeather(Weather w)throws IncorrectValues {
		if(w==null) throw new IncorrectValues("Weather null");
		weather=w;
	}
	void addContamination(int c) throws IncorrectValues {
		if(c<0) throw new IncorrectValues("C is negative");
		totalCont+=c;
	}

	@Override
	public JSONObject report() {
		JSONObject jo= new JSONObject();
		jo.put("id", _id);
		jo.put("speedlimit",this.actualMaxSpeed);
		jo.put("weather", weather);
		jo.put("co2", this.totalCont);
		JSONArray ja=new JSONArray();
		for(Vehicle v: vehicleList) {
			ja.put(v.getId());
		}
		jo.put("vehicles", ja);
		return jo;
	}
	public void setTotalCont(int totalCont) {
		this.totalCont = totalCont;
	}
	public int getTotalCont() {
		return totalCont;
	}
	public Weather getWeather() {
		return weather;
	}
	@Override
	public int compareTo(SimulatedObject o) {
		return 0;
	}
	public int getContLimit() {
		return contLimit;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public int getActualMaxSpeed() {
		return actualMaxSpeed;
	}
	public void setActualMaxSpeed(int actualMaxSpeed) {
		this.actualMaxSpeed = actualMaxSpeed;
	}
	public List<Vehicle> getVehicleList() {
		return vehicleList;
	}
	boolean checkEntryRoad(Junction j) {
		return destJunc==j;
	}
	public Junction getDestJunc() {
		return destJunc;
	}
	public int getLength() {
		return length;
	}
	public double getCO2Limit() {
		// TODO Auto-generated method stub
		return contLimit;
	}
}

package simulator.model;

import simulator.exceptions.IncorrectValues;
import simulator.model.simulatedOBJ.Junction;
import simulator.model.simulatedOBJ.Road;


public abstract class NewRoadEvent extends Event {
	protected String srcJunc, destJunc;
	protected int length,maxSpeed,actualMaxSpeed, co2Limit;
	protected Weather weather;
	protected String id;
	protected Junction src,dest;
	NewRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		this.srcJunc=srcJun;
		this.destJunc=destJunc;
		this.maxSpeed=maxSpeed;
		this.co2Limit=co2Limit;
		this.length=length;
		this.weather=weather;
		this.id=id;
	}

	@Override
	void execute(RoadMap map) throws IncorrectValues {
		src= map.getJunction(srcJunc);
		dest= map.getJunction(destJunc);
		if(src==null && dest ==null) throw new IncorrectValues("No existen los cruces");
		map.addRoad(createRoadObject());

	}
	abstract Road createRoadObject() throws IncorrectValues;
	
}

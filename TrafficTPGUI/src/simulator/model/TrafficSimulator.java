package simulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import simulator.exceptions.IncorrectValues;
import simulator.misc.SortedArrayList;
import simulator.model.simulatedOBJ.Junction;
import simulator.model.simulatedOBJ.Road;
import simulator.model.simulatedOBJ.Vehicle;

public class TrafficSimulator implements Observable<TrafficSimObserver> {

	private RoadMap roadsMap;
	private List<Event> eventList;
	private int timeTick=0;
	private List<TrafficSimObserver> tsObsList;
	public TrafficSimulator() {
		eventList = new SortedArrayList<Event>();
		roadsMap = new RoadMap();
		tsObsList = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		eventList.add(e);
		for(TrafficSimObserver i : tsObsList) i.onEventAdded(roadsMap, eventList,e,timeTick);
	}
	

	public void advance() throws IncorrectValues {
		++timeTick;
		for(TrafficSimObserver i : tsObsList) i.onAdvanceStart(roadsMap, eventList,timeTick);
		for(Iterator<Event> i= eventList.iterator(); i.hasNext();  ) {
			Event e=i.next();
			if(e._time==timeTick) {
				i.remove();  // Borrar elementos de una lista es borrar el iterador.
				try {e.execute(roadsMap);}
				catch(IncorrectValues err) {for(TrafficSimObserver obs : tsObsList) obs.onError(err.getMessage());}
			}
		}
		List<Junction> juncs=roadsMap.getJunctions();
		Iterator<Junction> j= juncs.iterator();
		while(j.hasNext()) {
			try {
				Junction aux=j.next();
				aux.advance(timeTick);
			}
			catch(IncorrectValues err) {for(TrafficSimObserver obs : tsObsList) obs.onError(err.getMessage());throw err;}
			
		}
		List<Road> roads=roadsMap.getRoads();
		Iterator<Road> r= roads.iterator();
		while(r.hasNext()) {
			try {
				Road aux=r.next();
				aux.advance(timeTick);
			}
			catch(IncorrectValues err) {for(TrafficSimObserver obs : tsObsList) obs.onError(err.getMessage());throw err;}
		}	
		for(TrafficSimObserver i : tsObsList) i.onAdvanceEnd(roadsMap, eventList,timeTick);
	}
	public void reset() {
		roadsMap.reset();
		eventList.clear();
		timeTick=0;
		for(TrafficSimObserver i : tsObsList) i.onReset(roadsMap, eventList,timeTick);
	}
	
	public JSONObject report() {
		JSONObject jo= new JSONObject();
		jo.put("time", timeTick);
		jo.put("state", roadsMap.report());
		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		// TODO Auto-generated method stub
		tsObsList.add(o);
		for(TrafficSimObserver i : tsObsList) i.onRegister(roadsMap, eventList,timeTick);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		// TODO Auto-generated method stub
		tsObsList.remove(o);
	}
	
}

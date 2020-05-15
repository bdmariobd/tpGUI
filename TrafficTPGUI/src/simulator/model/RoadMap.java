package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.simulatedOBJ.*;

public class RoadMap {
	
	private List<Junction> lJunc;
	private List<Road> lRoad;
	private List<Vehicle> lVeh;
	private Map<String,Junction> juncMap;
	private Map<String,Road> roadMap;
	private Map<String,Vehicle> vehMap;
	
	//Recuerda tener actualizadas las listas y los mapas para usar los mapas en 
	//pro de la eficiencia de la búsqueda de un objeto;
	//y usar las listas para recorrer los objetos en 
	// mismo orden en el cual han sido añadidos
	
	RoadMap(){
		lJunc= new ArrayList<Junction>();
		lRoad= new ArrayList<Road>();
		lVeh=new ArrayList<Vehicle>();
		juncMap= new HashMap<String,Junction>();
		roadMap= new HashMap<String,Road>();
		vehMap=new HashMap<String,Vehicle>();;
	}	
	void addJunction(Junction j) {
		lJunc.add(j);
		if(getJunction(j.getId()) == null) {
			juncMap.put(j.getId(), j);
		}
	}
	void addRoad(Road r) {
		lRoad.add(r);
		if(getRoad(r.getId())==null && juncMap.containsValue(r.getSrcJunc()) && 
				juncMap.containsValue(r.getDestJunc())) {
			roadMap.put(r.getId(),r);
		}
	}
	void addVehicle(Vehicle v) {
		lVeh.add(v);
		if(getVehicle(v.getId())==null) {
			vehMap.put(v.getId(),v);
		}
	}
	public Junction getJunction(String id) {
		return juncMap.get(id);
	};
	public Road getRoad(String id) {
		return roadMap.get(id);
	};
	public Vehicle getVehicle(String id) {
		return vehMap.get(id);
	};
	public List<Junction>getJunctions(){
		return Collections.unmodifiableList(lJunc);	
	}
	public List<Road>getRoads(){
		return Collections.unmodifiableList(lRoad);
	};
	public List<Vehicle>getVehicles(){
		return Collections.unmodifiableList(lVeh);
	};
	void reset() {
		lJunc.clear();
		lRoad.clear();
		lVeh.clear();
		juncMap.clear();
		roadMap.clear();
		vehMap.clear();
	};
	
	public JSONObject report() {
		JSONObject jo= new JSONObject();
		JSONArray junctions= new JSONArray();
		for(Junction j: lJunc) junctions.put(j.report());
		jo.put("junctions",junctions);
		JSONArray roads= new JSONArray();
		for(Road r: lRoad) roads.put(r.report());
		jo.put("roads",roads);
		JSONArray vehicles= new JSONArray();
		for(Vehicle v: lVeh) vehicles.put(v.report());
		jo.put("vehicles",vehicles);
		return jo;
	}
	
	
}


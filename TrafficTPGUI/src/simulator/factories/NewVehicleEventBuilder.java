package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		 List<String> listdata = new ArrayList<String>();     
		 JSONArray jArray = data.getJSONArray("itinerary"); 
		 if (jArray != null) { 
		    for (int i=0;i<jArray.length();i++){ 
		     listdata.add(jArray.getString(i));
		    } 
		 } 
		return new NewVehicleEvent(data.getInt("time"), 
				data.getString("id"), data.getInt("maxspeed")
				, data.getInt("class"), listdata);
	}

}

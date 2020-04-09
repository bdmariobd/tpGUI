package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.exceptions.IncorrectValues;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws JSONException, IncorrectValues {
		 List<Pair<String,Integer>> listdata = new ArrayList<Pair<String,Integer>>();     
		 JSONArray jArray = data.getJSONArray("info"); 
		    for (int i=0;i<jArray.length();i++) {
		    	JSONObject pair = jArray.getJSONObject(i);
			    listdata.add(new Pair<String,Integer>(pair.getString("vehicle"), pair.getInt("class")));
		    } 
		return new NewSetContClassEvent(data.getInt("time"), listdata);
	}

	

}

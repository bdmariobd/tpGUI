package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.exceptions.IncorrectValues;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws JSONException, IncorrectValues {
		
		 List<Pair<String,Weather>> listdata = new ArrayList<Pair<String,Weather>>();     
		 JSONArray jArray = data.getJSONArray("info"); 
		
		    for (int i=0;i<jArray.length();i++){ 
		    JSONObject pair = jArray.getJSONObject(i);
		     listdata.add(new Pair<String,Weather> (pair.getString("road"), 
		    		 Weather.getWeatherByString(pair.getString("weather"))));
		    } 
		 
		
		return new SetWeatherEvent(data.getInt("time"),listdata);
	}

}

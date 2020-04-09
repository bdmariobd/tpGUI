package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {

	NewRoadEventBuilder(String type) {
		super(type);
	}
	protected Event createTheInstance(JSONObject data) {
		return createTheRoad(data.getInt("time"),data.getString("id"),data.getString("src"),data.getString("dest")
				,data.getInt("length"),data.getInt("co2limit"),data.getInt("maxspeed"),
				Weather.getWeatherByString(data.getString("weather")));
	}

	protected abstract Event createTheRoad(int time, String id, String src, String dest, int length, 
			int co2Limit, int maxspeed, Weather weather);
	
}

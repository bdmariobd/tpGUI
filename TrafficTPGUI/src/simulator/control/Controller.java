package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.exceptions.IncorrectValues;
import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;
public class Controller {
	
	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	private boolean checkLoad=false;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws IncorrectValues{
		if(sim==null) throw new IncorrectValues("Null simulator");
		if(eventsFactory==null) throw new IncorrectValues("Null events Factory");
		this.sim=sim;
		this.eventsFactory= eventsFactory;
	}
	
	public void loadEvents(InputStream in) throws IncorrectValues {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		if(!jo.has("events")) throw new IncorrectValues("Incorrect Json bruh");
		JSONArray ja = jo.getJSONArray("events"); 
		for(int i=0;i<ja.length();++i) {
			Event e = eventsFactory.createInstance(ja.getJSONObject(i));
			if(e==null) throw new IncorrectValues("Incorrect event");
			sim.addEvent(e);	
		}
		checkLoad=true;
	}
	public void run(int n, OutputStream out) throws IncorrectValues {
		if (out == null) {
			out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {}
			};
		}
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println(" \"states\": [");
		for(int i=0;i<n-1;++i) {
			sim.advance();
			p.print(sim.report()); 
			p.println(",");
		}
		//ultimo paso
		sim.advance();
		p.print(sim.report());
		p.println("]");
		p.println("}");
	}
	
	public void reset() {
		sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}
	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}
	public void addEvent(Event e){
		sim.addEvent(e);
	}

	public void run(int n) throws IncorrectValues {
		for(int i=0;i<n;i++) sim.advance();
	}

	public boolean isCheckLoad() {
		return checkLoad;
	}
}

package simulator.model;

import java.util.Iterator;
import java.util.List;

import simulator.exceptions.IncorrectValues;
import simulator.misc.Pair;
import simulator.model.simulatedOBJ.Vehicle;

public class NewSetContClassEvent extends Event {
	List<Pair<String,Integer>> cs;
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) throws IncorrectValues {
		super(time);
		if(cs==null) throw new IncorrectValues("Lista nula");
		this.cs=cs;
	}
	@Override
	void execute(RoadMap map) throws IncorrectValues {
		Iterator <Pair<String,Integer>> i= cs.iterator();
		while(i.hasNext()) {
			Pair<String,Integer> aux= i.next();
			Vehicle v = map.getVehicle(aux.getFirst());
			if(v==null) throw new IncorrectValues("No existe una carretera con id " + aux.getFirst());
			v.setContClass(aux.getSecond());
		}
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String ret="Change CO2 class: [";
		for(Pair<String,Integer> i : cs) ret+="(" + i.getFirst()+","+i.getSecond()+")";
		ret+="]";
		return ret;
	}
}

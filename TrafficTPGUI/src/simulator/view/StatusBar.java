package simulator.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

// TODO rayita de sote en una fiesta

public class StatusBar extends JPanel implements TrafficSimObserver{

	private Controller c;
	private JLabel tbTicks, tbEvents;
	public StatusBar(Controller c) {
		this.c=c;
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		tbTicks=new JLabel();
		tbEvents=new JLabel();
		tbTicks.setVisible(true);
		tbEvents.setVisible(true);
		this.add(tbTicks);
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(tbEvents);		
		c.addObserver(this);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		tbTicks.setText("Time: " + time);
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		tbEvents.setText("Event added ("+e.toString()+")");
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		tbTicks.setText("Time: " + 0);
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}

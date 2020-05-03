package simulator.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.control.Controller;
import simulator.exceptions.IncorrectValues;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;
import simulator.model.simulatedOBJ.Road;
import simulator.model.simulatedOBJ.Vehicle;

public class ChangeRoadWeatherClass extends JDialog implements ActionListener,TrafficSimObserver{

	
	private JButton btnOk, btnCancel;
	private Controller ctrl;
	private JComboBox cbRoads;
	private JComboBox cbWeathers;
	private JSpinner spinbox;
	private JPanel p, pBotonera, pCentro;
	private List<Road>rd;
	
	public ChangeRoadWeatherClass(Controller ctrl) {
		// dsp de meterlista
		ctrl.addObserver(this);
		p= new JPanel(new BorderLayout());
		p.setVisible(true);
		this.setContentPane(p);
		JLabel enunciao=new JLabel("Schedule an event to change the weather of a road after given a number of simulation ticks from now.");
		enunciao.setVisible(true);
		p.add(enunciao,BorderLayout.NORTH);
		this.ctrl=ctrl;
		pCentro= new JPanel(new FlowLayout());
		initComboBoxRoads();
		initComboBoxWeathers();
		initSpinner();
		initBotonera();
		p.add(pCentro, BorderLayout.CENTER);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
	public void initBotonera() {
		pBotonera=new JPanel(new FlowLayout());
		pBotonera.setVisible(true);
		btnCancel= new JButton("Cancel");
		btnOk=new JButton("OK");
		pBotonera.add(btnCancel);
		pBotonera.add(btnOk);
		btnCancel.setVisible(true); 
		btnOk.setVisible(true);
		p.add(pBotonera,BorderLayout.SOUTH);
		btnOk.addActionListener(this);
		btnCancel.addActionListener(this);
	}
	
	public void initComboBoxRoads() {
		JPanel pa= new JPanel();
		JLabel j= new JLabel("Road: ");
		j.setLabelFor(cbRoads);
		j.setVisible(true);
		List<String>r= new ArrayList<String>();
		for(Road i: rd) r.add(i.getId());
		cbRoads=new JComboBox<Object>(r.toArray());
		pa.add(j);
		pa.add(cbRoads);
		pCentro.add(pa,FlowLayout.LEFT);
	}
	public void initComboBoxWeathers() {
		JLabel j=new JLabel("Weather:");
		j.setLabelFor(cbWeathers);
		j.setVisible(true);
		cbWeathers=new JComboBox(Weather.values());
		JPanel pComboBoxCont= new JPanel(new FlowLayout());
		pComboBoxCont.setVisible(true);
		pComboBoxCont.add(j);
		pComboBoxCont.add(cbWeathers);
		pCentro.add(pComboBoxCont,FlowLayout.CENTER);
	}
	public void initSpinner() {
		JLabel j=new JLabel("Ticks:");
		SpinnerNumberModel smDialog= new SpinnerNumberModel(1,1,999,1);
		spinbox = new JSpinner(smDialog);
		j.setLabelFor(spinbox);
		spinbox.setVisible(true);
		JPanel pLabelSpin = new JPanel(new FlowLayout());
		pLabelSpin.setVisible(true);
		pLabelSpin.add(j);
		pLabelSpin.add(spinbox);
		pCentro.add(pLabelSpin);

	}
	String getRoad() {
		return cbRoads.getSelectedItem().toString();
	}
	Weather getWeather() {
		return Weather.valueOf(cbWeathers.getSelectedItem().toString());
	}
	int getTime() {
		return (Integer) spinbox.getValue();
	}
	boolean getResponse() {
		return true;
	}
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand()=="OK") {
			List<Pair<String,Weather>> cs= new ArrayList<Pair<String,Weather>>();
			cs.add(new Pair<String,Weather> (this.getRoad(),this.getWeather()));
			try {
				SetWeatherEvent e = new SetWeatherEvent(this.getTime()+ ctrl.getTimeTick(),cs);
				ctrl.addEvent(e);
				JOptionPane.showMessageDialog(null,"Success","Info",JOptionPane.INFORMATION_MESSAGE); // TODO Este mensaje no tira, comprobar carga del ficheiro
				
			} catch (IncorrectValues e) {
				JOptionPane.showMessageDialog(null, "No ha habido carga de fichero", "ERROR ", JOptionPane.WARNING_MESSAGE); // TODO Comprobar la carguita
			}
			finally {
				this.dispose();
			}
		}
		else if (arg0.getActionCommand()=="Cancel") {
			this.dispose();
		}
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
		
	}
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		rd=map.getRoads();
		
	}
	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		rd=map.getRoads();
	}
	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
}


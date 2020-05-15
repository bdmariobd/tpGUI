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

import simulator.model.NewSetContClassEvent;

import simulator.model.simulatedOBJ.Vehicle;

public class ChangeCO2ClassDialog extends JDialog implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnOk, btnCancel;
	private Controller ctrl;
	private JComboBox<Object> cbV;
	private JComboBox<Integer> cbCO2;
	private JSpinner spinbox;
	private JPanel p, pBotonera, pCentro;
	private List<Vehicle> ve;
	private int time;
	
	public ChangeCO2ClassDialog(Controller ctrl, List<Vehicle> ve, int time) {
		p= new JPanel(new BorderLayout());
		p.setVisible(true);
		this.setContentPane(p);
		JLabel enunciao=new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		enunciao.setVisible(true);
		p.add(enunciao,BorderLayout.NORTH);
		this.time=time;
		this.ctrl=ctrl;
		this.ve=ve;
		pCentro= new JPanel(new FlowLayout());
		initComboBoxVehicleSelect();
		initComboBox();
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
	
	public void initComboBoxVehicleSelect() {
		JPanel pa= new JPanel();
		JLabel j= new JLabel("Vehicles");
		j.setLabelFor(cbV);
		j.setVisible(true);
		List<String>l= new ArrayList<String>();
		for(Vehicle v: ve) l.add(v.getId());
		cbV=new JComboBox<Object>(l.toArray());
		pa.add(j);
		pa.add(cbV);
		pCentro.add(pa,FlowLayout.LEFT);
	}
	public void initComboBox() {
		JLabel j=new JLabel("CO2 Class:");
		j.setLabelFor(cbCO2);
		j.setVisible(true);
		Integer [] types = {1,2,3,4,5,6,7,8,9,10};
		cbCO2=new JComboBox<Integer>(types);
		JPanel pComboBoxCont= new JPanel(new FlowLayout());
		pComboBoxCont.setVisible(true);
		pComboBoxCont.add(j);
		pComboBoxCont.add(cbCO2);
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
	String getVehicle() {
		return cbV.getSelectedItem().toString();
	}
	int getContClass() {
		return Integer.parseInt(cbCO2.getSelectedItem().toString());
	}
	int getTime() {
		return (Integer) spinbox.getValue();
	}
	boolean getResponse() {
		return true;
	}
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand()=="OK") {
			List<Pair<String,Integer>> cs= new ArrayList<Pair<String,Integer>>();
			cs.add(new Pair<String,Integer> (this.getVehicle(),this.getContClass()));
			try {
				NewSetContClassEvent e = new NewSetContClassEvent(this.getTime()+ time,cs);
				ctrl.addEvent(e);
				JOptionPane.showMessageDialog(null,"Añadido con éxito","Info",JOptionPane.INFORMATION_MESSAGE); 
				
			} catch (IncorrectValues e) {
				JOptionPane.showMessageDialog(null, "No ha habido carga de fichero", "ERROR", JOptionPane.WARNING_MESSAGE); 
			}
			finally {
				this.dispose();
			}
		}
		else if (arg0.getActionCommand()=="Cancel") {
			this.dispose();
		}
	}
	
}
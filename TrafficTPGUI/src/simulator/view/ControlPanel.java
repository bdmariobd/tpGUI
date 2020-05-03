package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.exceptions.IncorrectValues;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver {
	//mirar toolbar
	private Controller cpControl;
	private JButton carga, changeContClass, runButton,stopButton, exitButton, wButton;
	private JSpinner ticks;
	private JToolBar jtb;
	private boolean _stopped = false;
	private boolean eventosCarg=false;
	public ControlPanel(Controller _ctrl) {
		cpControl=_ctrl;
		this.setLayout(new BorderLayout());
		//p1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jtb=new JToolBar();
		jtb.setVisible(true);
		
		initChargeButton();
		initChangeContClass();
		initWeatherEventButton();
		initRunButton();
		initStopButton();
		initTicksBox();
		jtb.add(Box.createGlue());
		initExitButton();
		this.add(jtb,BorderLayout.PAGE_START);
		setVisible(true);
		
		
	}
	private void initWeatherEventButton() {
		ImageIcon icon = new ImageIcon("resources/icons/weather.png");
		wButton = new JButton();
		wButton.setIcon(icon);
		wButton.setVisible(true);
		wButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ChangeRoadWeatherClass(cpControl);
			}
		});	
		jtb.add(wButton);
		
	}
	private void initExitButton() {
		ImageIcon icon = new ImageIcon("resources/icons/exit.png");
		exitButton = new JButton();
		exitButton.setIcon(icon);
		exitButton.setVisible(true);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 int input = JOptionPane.showConfirmDialog(null,"Are you sure?", "!!",JOptionPane.YES_NO_OPTION);
				 if(input==0) System.exit(0);
			}
		});	
		jtb.add(exitButton);
		
	}
	private void initStopButton() {
		ImageIcon icon = new ImageIcon("resources/icons/stop.png");
		stopButton = new JButton();
		stopButton.setIcon(icon);
		stopButton.setVisible(true); 
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ControlPanel.this.stop();
			}
		});	
		
		jtb.add(stopButton);
		
	}
	private void initTicksBox() {
		JLabel TicksLabel = new JLabel("Ticks: ");
		TicksLabel.setVisible(true);
		SpinnerNumberModel sm= new SpinnerNumberModel(1,1,999,1);
		ticks = new JSpinner(sm);
		ticks.setMaximumSize(new Dimension(40,40));
		jtb.add(TicksLabel);
		jtb.add(ticks);
	
	}
	private void initRunButton() {
		ImageIcon icon = new ImageIcon("resources/icons/run.png");
		runButton = new JButton();
		runButton.setIcon(icon);
		runButton.setVisible(true); 
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int value= Integer.parseInt(ticks.getValue().toString());
				enableToolBar(false);
				_stopped=false;
				run_sim(value);
			}
		});	
		jtb.add(runButton);
		
	}
	private void initChargeButton() {
		ImageIcon carpeta = new ImageIcon("resources/icons/open.png");
		carga = new JButton();
		carga.setIcon(carpeta);
		carga.setVisible(true); 
		carga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc=new JFileChooser();
				fc.setCurrentDirectory(new File("resources/examples"));
				int response=fc.showOpenDialog(ControlPanel.this);
				if(response==JFileChooser.APPROVE_OPTION) {
					File selectedFile = fc.getSelectedFile();
					cpControl.reset();
					try {
						InputStream targetStream = new FileInputStream(selectedFile);
						cpControl.loadEvents(targetStream);
						eventosCarg=true;
					}
				    catch (Exception e) {
				    	JOptionPane.showMessageDialog(null, "Carga de eventos fallida (Error:"+e.toString() + ")", "ERROR",  JOptionPane.WARNING_MESSAGE);
				    }
				}
			}
		});	
		jtb.add(carga);
	}
	private void run_sim( int n ) {
		if (n > 0  && !_stopped ) {
			try {
				cpControl.run(1);
			} catch (Exception e) {
				// TODO show error message
				JOptionPane.showMessageDialog(null, "Run fallido (Error:"+e.toString() + ")", "ERROR",  JOptionPane.WARNING_MESSAGE);
				_stopped = true ;
				enableToolBar(true);
				return ;
			}
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
					//TODO capturar excepcion
				    try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} 
		else {
			enableToolBar(true);
			_stopped = true ;
		}
	}
	private void stop() {
		_stopped = true ;
	}
	private void initChangeContClass() {
		ImageIcon hojita = new ImageIcon("resources/icons/co2class.png");
		changeContClass = new JButton();
		changeContClass.setIcon(hojita);
		changeContClass.setVisible(true);
		changeContClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cpControl.isCheckLoad()) {
					new ChangeCO2ClassDialog(cpControl);
				}
			}
		});
		jtb.add(changeContClass);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
	}
	private void enableToolBar(boolean b) {
		carga.setEnabled(b);
		changeContClass.setEnabled(b);
		wButton.setEnabled(b);
		runButton.setEnabled(b);
	}

}

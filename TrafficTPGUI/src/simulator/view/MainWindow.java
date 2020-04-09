package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class MainWindow extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8510398476167315606L;
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.black, 1);
	private Controller _ctrl;
	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGUI();
	}
	private void initGUI() {
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		mainPanel.add( new ControlPanel( _ctrl ), BorderLayout. PAGE_START );
		mainPanel.add( new StatusBar( _ctrl ),BorderLayout. PAGE_END );
		JPanel viewsPanel = new JPanel( new GridLayout(1, 2));
		mainPanel.add( viewsPanel , BorderLayout. CENTER );
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout( new BoxLayout( tablesPanel , BoxLayout. Y_AXIS ));
		viewsPanel.add( tablesPanel );
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout( new BoxLayout( mapsPanel , BoxLayout. Y_AXIS ));
		viewsPanel.add( mapsPanel );
		// tables
		JPanel eventsView =
		createViewPanel( new JTable( new EventsTableModel( _ctrl )), "Events" );
		eventsView.setPreferredSize( new Dimension(500, 200));
		tablesPanel.add( eventsView );
		eventsView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Events", TitledBorder.LEFT,TitledBorder.TOP));
		// TODO add other tables
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		vehiclesView.setPreferredSize( new Dimension(500, 200));
		tablesPanel.add(vehiclesView );
		vehiclesView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Vehicles", TitledBorder.LEFT,TitledBorder.TOP));
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		roadsView.setPreferredSize( new Dimension(500, 200));
		tablesPanel.add( roadsView );
		roadsView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Roads", TitledBorder.LEFT,TitledBorder.TOP));
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		junctionsView.setPreferredSize( new Dimension(500, 200));
		tablesPanel.add( junctionsView );
		junctionsView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Junctions", TitledBorder.LEFT,TitledBorder.TOP));
		
		
		// maps
		JPanel mapView = createViewPanel( new MapComponent( _ctrl ), "Map" );
		mapView.setPreferredSize( new Dimension(500, 400));
		mapView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Map", TitledBorder.LEFT,TitledBorder.TOP));
		mapsPanel.add( mapView );
		JPanel mapRoadView = createViewPanel( new MapByRoadComponent( _ctrl ), "Map by Road" );
		mapRoadView.setPreferredSize( new Dimension(500, 400));
		mapRoadView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Map by Road", TitledBorder.LEFT,TitledBorder.TOP));
		mapsPanel.add( mapRoadView );
		
		// TODO add a map for MapByRoadComponent
		// ...
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible( true );
	}
	private JPanel createViewPanel(JComponent c , String title ) {
		JPanel p = new JPanel( new BorderLayout() );
		// TODO add a framed border to p with title
		p.add( new JScrollPane( c ));
		return p ;
	}
}
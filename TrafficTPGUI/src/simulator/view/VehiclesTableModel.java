package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.simulatedOBJ.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	
	private List<Vehicle> _vehicles;
	private String[] _colNames = {"Id", "Location","Itinerary","CO2 Class","Max. Speed","Speed","Total CO2","Distance","Status" };

	public VehiclesTableModel() {
		_vehicles=null;
	}

	public VehiclesTableModel(Controller _ctrl) {
		_ctrl.addObserver(this);
	}

	public void update() {
		// observar que si no refresco la tabla no se carga
		// La tabla es la represantación visual de una estructura de datos,
		// en este caso de un ArrayList, hay que notificar los cambios.
		//this.a
		
		// We need to notify changes, otherwise the table does not refresh.
		fireTableDataChanged(); // Avisa a todo dios que algo que ha
		//habido cambios en la tabla.
	}
	
	public void setVehiclesList(List<Vehicle> vehicles) {
		_vehicles = vehicles;
		update();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	//si no pongo esto no coge el nombre de las columnas
	//
	//this is for the column header
	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	// método obligatorio, probad a quitarlo, no compila
	//
	// this is for the number of columns
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	// método obligatorio
	//
	// the number of row, like those in the events list
	public int getRowCount() {
		return _vehicles == null ? 0 : _vehicles.size();
	}

	@Override
	// método obligatorio
	// así es como se va a cargar la tabla desde el ArrayList
	// el índice del arrayList es el número de fila pq en este ejemplo
	// quiero enumerarlos.
	//
	// returns the value of a particular cell 
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _vehicles.get(rowIndex).getId();
			break;
		case 1:
			s = _vehicles.get(rowIndex).getLocation();
			break;
		
		case 2:
			s= _vehicles.get(rowIndex).getIterinary();
			break;
		
		case 3:
			s= _vehicles.get(rowIndex).getContClass();
			break;
		case 4:
			s= _vehicles.get(rowIndex).getMaxSpeed();
			break;
		case 5:
			s= _vehicles.get(rowIndex).getSpeed();
			break;
		case 6:
			s= _vehicles.get(rowIndex).getTotalCO2();
			break;
		case 7:
			s=_vehicles.get(rowIndex).getDistance();
			break;
		case 8:
			s=_vehicles.get(rowIndex).getStatus();
			break;
		}
	
		return s;
	}@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}



}

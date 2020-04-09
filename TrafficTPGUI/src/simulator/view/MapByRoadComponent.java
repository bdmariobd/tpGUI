package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.VehicleStatus;
import simulator.model.Weather;
import simulator.model.simulatedOBJ.Junction;
import simulator.model.simulatedOBJ.Road;
import simulator.model.simulatedOBJ.Vehicle;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;

	private Image car;
	private Image cont0; // Verde;
	private Image cont1; //Amarillo;
	private Image cont2; //Naranja;
	private Image cont3; // roja enfadao
	private Image cont4; // nivel almeida
	private Image cont5; // nivel coronavairus
	private Image rain,wind,sun,storm,cloud;

	MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
		setPreferredSize(new Dimension (300,200));
	}

	private void initGUI() {
		loadImages();
	}

	private void loadImages() {
		car = loadImage("car.png");
		cont0= loadImage("cont_0.png");
		cont1=loadImage("cont_1.png");
		cont2=loadImage("cont_2.png");
		cont3=loadImage("cont_3.png");
		cont4=loadImage("cont_4.png");
		cont5=loadImage("cont_5.png");
		rain=loadImage("rain.png");
		wind=loadImage("wind.png");
		sun=loadImage("sun.png");
		storm=loadImage("storm.png");
		cloud=loadImage("cloud.png");
	}
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color // aka dibujar fondo blanco
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("che bldo mete un mapa cabesa sandia!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		drawRoads(g);
		drawVehicles(g);
	}

	private void drawRoads(Graphics g) {
		int i=0;
		for (Road r : _map.getRoads()) {

			// calculo de coordenadas
			int x1 = 50;
			int x2 = (int) getWidth()-100;
			int y= (i+1) * 50;
			
			g.setColor(Color.BLACK);
			g.drawString(r.getId(), x1-30, y+5);
			g.drawLine(x1, y, x2, y);
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1, y-6, 14, 14);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSrcJunc().getId(), x1+2, y-10); // fixeo del toc1
			g.drawString(r.getDestJunc().getId(), x2+2, y-10);
			int indice = r.getDestJunc().getGreenLightIndex();
			if(indice!=-1 && r.equals(r.getDestJunc().getInRoads().get(indice))) g.setColor(_GREEN_LIGHT_COLOR);
			else g.setColor(_RED_LIGHT_COLOR);
			g.fillOval(x2, y-6, 14, 14);
			if(r.getWeather()==Weather.CLOUDY) g.drawImage(cloud,x2+25,y-15,30,30,this);
			else if(r.getWeather()==Weather.RAINY) g.drawImage(rain,x2+25,y-15,30,30,this);
			else if(r.getWeather()==Weather.STORM) g.drawImage(storm,x2+25,y-15,30,30,this);
			else if(r.getWeather()==Weather.SUNNY) g.drawImage(sun,x2+25,y-15,30,30,this);
			else g.drawImage(wind,x2+25,y-15,30,30,this);
			int C = ( int ) Math.floor(Math.min(( double ) r.getTotalCont()/(1.0 + ( double ) r.getCO2Limit()),1.0) / 0.19);
			if(C==0) g.drawImage(cont0, x2+60, y-15, 32, 32, this);
			else if(C==1) g.drawImage(cont1, x2+60, y-15, 32, 32, this);
			else if(C==2) g.drawImage(cont2, x2+60, y-15, 32, 32, this);
			else if(C==3) g.drawImage(cont3, x2+60, y-15, 32, 32, this);
			else if(C==4) g.drawImage(cont4, x2+60, y-15, 32, 32, this);
			else g.drawImage(cont5, x2+60, y-15, 32, 32, this);
			
			
			++i;
		}

	}

	private void drawVehicles(Graphics g) {
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				
				// The calculation below compute the coordinate (vX,vY) of the vehicle on the
				// corresponding road. It is calculated relativly to the length of the road, and
				// the location on the vehicle.
				int x1 = 50;
				int x2 = (int) getWidth()-100;
				int y= (_map.getRoads().lastIndexOf(v.getRoad()) +1) * 50;
				int vX=x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) v.getRoad().getLength()));

				// Choose a color for the vehcile's label and background, depending on its
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));

				// draw an image of a car (with circle as background) and it identifier
				g.drawImage(car, vX, y-12, 25, 25, this);
				g.drawString(v.getId(), vX+7, y - 8);
			}
		}
	}

	

	// this method is used to update the preffered and actual size of the component,
	// so when we draw outside the visible area the scrollbars show up
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		if (maxW > getWidth() || maxH > getHeight()) {
		    setPreferredSize(new Dimension(maxW, maxH));
		   setSize(new Dimension(maxW, maxH));
		}
	}

	
	

	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

}

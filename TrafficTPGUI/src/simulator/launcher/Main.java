package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import simulator.control.Controller;
import simulator.exceptions.IncorrectValues;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {
	private final static Integer _timeLimitDefaultValue = 10;
	private static Integer _timeLimit; // n�mero de pasos
	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static boolean gui = true;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			guiORConsoleOption(line);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator�s main "
				+ "loop (default value is 10).").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Switch into GUI mode and console mode").build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (!gui && _inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}
	private static void guiORConsoleOption(CommandLine line) throws ParseException {
		if(line.hasOption("m")) {
			if(line.getOptionValue("m").equals("console")) {
				System.out.println("Console mode selected");
				gui=false;
			}
		}
	}


	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if(!gui) _outFile = line.getOptionValue("o");
	}
	private static void parseTicksOption(CommandLine line) throws ParseException {
		if(line.hasOption("t")) {
			_timeLimit = Integer.parseInt(line.getOptionValue("t"));
		}
		else _timeLimit = _timeLimitDefaultValue;
	}
	private static void initFactories() {
		//SEMAFORO
		ArrayList<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder() );
		lsbs.add( new MostCrowdedStrategyBuilder() );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		//EXTRACCION COLA
		ArrayList<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		//RESTO DE BUILDERS
		ArrayList<Builder<Event>> eventBuilders = new ArrayList<>();
		eventBuilders.add(new NewJunctionEventBuilder(lssFactory, dqsFactory)); //JUNCTION BUILDER
		eventBuilders.add(new NewVehicleEventBuilder());
		eventBuilders.add(new NewCityRoadEventBuilder());
		eventBuilders.add(new NewInterCityRoadEventBuilder());
		eventBuilders.add(new SetContClassEventBuilder());
		eventBuilders.add(new SetWeatherEventBuilder());
		_eventsFactory = new BuilderBasedFactory<>(eventBuilders); //initialize the factory
	}
	
	private static void startBatchMode() throws IOException {
		InputStream in = new FileInputStream(new File(_inFile));
		OutputStream out = _outFile == null ?
		System.out : new FileOutputStream(new File(_outFile));
		TrafficSimulator sim = new TrafficSimulator();
		try {
			Controller ctrl = new Controller(sim, _eventsFactory);
			ctrl.loadEvents(in);
			ctrl.run(_timeLimit, out); 
		}
		catch(IncorrectValues e) {
			System.out.println("Simulation failed! ("+ e.toString()+")");
		}
		in.close();
		System.out.println("Done!");
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(!gui)startBatchMode();
		else startGuiMode();
	}
	private static void startGuiMode() throws IOException {
		InputStream in = null;
		if(_inFile!=null) in = new FileInputStream(new File(_inFile));
		//OutputStream out = _outFile == null ? System.out : new FileOutputStream(new File(_outFile));
		TrafficSimulator sim = new TrafficSimulator();
		try {
			Controller ctrl = new Controller(sim, _eventsFactory);
			if(in!=null) ctrl.loadEvents(in);
			SwingUtilities.invokeLater( new Runnable() {
				@ Override
				public void run() {
				new MainWindow(ctrl);
				}
			});
		}
		catch(IncorrectValues e) {
			System.out.println("Simulation failed! ("+ e.toString()+")");
		}
		if(in!=null) in.close();
		System.out.println("Done!");
		
		
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help
	// -m console : para usar el modo consola(obligatorio -i, opcional -o)
	// nada o -m gui : usar modo GUI(opcional -i, no usa -o)

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

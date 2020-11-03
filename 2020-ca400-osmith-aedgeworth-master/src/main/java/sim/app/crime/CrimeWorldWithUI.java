package sim.app.crime;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.media.chart.TimeSeriesChartGenerator;

// guide here https://cs.gmu.edu/~eclab/projects/mason/manual.pdf
public class CrimeWorldWithUI extends GUIState {

	private static final int MAPWIDTH = 600;
	private static final int MAPHEIGHT = 600;
	public Display2D display;
	public JFrame displayFrame;
	public SparseGridPortrayal2D societyPortrayal = new SparseGridPortrayal2D();

	FastValueGridPortrayal2D homeSitesPortrayal = new FastValueGridPortrayal2D("Home Site", true); // immutable
	FastValueGridPortrayal2D workSitesPortrayal = new FastValueGridPortrayal2D("Work Site", true); // immutable
	FastValueGridPortrayal2D educationSitesPortrayal = new FastValueGridPortrayal2D("Site", true); // immutable
	FastValueGridPortrayal2D recreationSitesPortrayal = new FastValueGridPortrayal2D("Site", true); // immutable
	Font font = new Font("SansSerif", Font.BOLD, 20);

	// Adding a chart programmatically
	HistogramDataset dataset; // the dataset we'll add to
	DefaultPieDataset pieChartDataset;

	TimeSeriesChartGenerator lineChart; // the charting facility

	public CrimeWorldWithUI() {
		super(new CrimeWorld(System.currentTimeMillis()));
	}

	public CrimeWorldWithUI(SimState state) {
		super(state);
	}

	public static String getName() {
		return "Crime World";
	}

	public static void main(String[] args) {
		CrimeWorldWithUI vid = new CrimeWorldWithUI();
		Console c = new Console(vid);
		c.setVisible(true);

	}

	public Object getSimulationInspectedObject() {
		return state;
	}

	public Inspector getInspector() {

		Inspector i = super.getInspector();
		i.setVolatile(true);
		return i;
	}

	// This method simply calls a method we made up called
	// setupPortrayals()
	public void start() {
		super.start();
		setupPortrayals();
		setupSitePortrayals();
		drawingHistogram();
		drawingPieChart();

	}

	private void drawingPieChart() {

		createPieChartDataset();

	}

	// to create dynamically updating graphs, you need to get the
	// values from the class that extends SimState. if you dont, then
	// the values will not change, and the function will not be called.
	private HashMap<String, Integer> createPieChartDataset() {

		HashMap<String, Integer> homelessNumbers = new HashMap<String, Integer>();

		for (int i = 0; i < CrimeWorld.retrieveHomelessNumber().length; i++) {

			System.out.println(CrimeWorld.retrieveHomelessNumber()[i]);
			
			if (CrimeWorld.retrieveHomelessNumber()[i] == false) {
				if (!homelessNumbers.containsKey("is Homeless")) {
					homelessNumbers.put("is Homeless", 1);
				} else {
					homelessNumbers.put("is Homeless", homelessNumbers.get("is Homeless") + 1);
				}
			} else {
				if (!homelessNumbers.containsKey("is not Homeless")) {
					homelessNumbers.put("is not Homeless", 1);
				} else {
					homelessNumbers.put("is not Homeless", homelessNumbers.get("is not Homeless") + 1);
				}
			}
			// histogram.addSeries(null, 15, CrimeWorld.getIncome()[i], null);
		}

		// System.out.println(homelessNumbers.get("is not Homeless"));
		// System.out.println(homelessNumbers.get("is Homeless"));
		pieChartDataset.setValue("Have a Home", homelessNumbers.get("is not Homeless"));
		pieChartDataset.setValue("Homeless", homelessNumbers.get("is Homeless"));
		return homelessNumbers;

	}

	public void drawingHistogram() {
		// chart.removeAllSeries();

		createDataset();

		scheduleRepeatingImmediatelyAfter(new Steppable() {
			public void step(SimState state) {
				double x = 100;
				double y = (100.0);
			}
		});
	}

	public static double getMax(double[] inputArray) {
		double maxValue = inputArray[0];
		for (int i = 0; i < inputArray.length; i++) {
			if (inputArray[i] > maxValue) {
				maxValue = inputArray[i];
			}
		}
		return maxValue;
	}

	public static double getMin(double[] inputArray) {
		double minValue = inputArray[0];
		for (int i = 0; i < inputArray.length; i++) {
			if (inputArray[i] < minValue) {
				minValue = inputArray[i];
			}
		}
		return minValue;
	}

	// create the dataset for income distribtuion to be used by the histogram
	private void createDataset() {

		// creating dataset for income distribtuion
		double[] incomeValues = new double[CrimeWorld.getWealth().length];
		
		for(int i = 0; i < CrimeWorld.getWealth().length; i++) {
			incomeValues[i] = CrimeWorld.getWealth()[i];
			//histogram.addSeries(null, 15, CrimeWorld.getIncome()[i], null);
		}

		// add the income values dataset into the histogram dataset. spread the values
		// out into 15 different buckets.
		dataset.addSeries("key", incomeValues, 10);

	}

	// called when the UI is closed
	public void finish() {
		super.finish();
	}

	// is called when a simulation is loaded from a checkpoint
	public void load(SimState state) {
		super.load(state);
		setupPortrayals();
		drawingHistogram();
		drawingPieChart();
	}

	private void setupSitePortrayals() {
		CrimeWorld crimeWorld = (CrimeWorld) state;

		homeSitesPortrayal.setField(crimeWorld.sitesHome);
		// setMap for every pixel around the location
		homeSitesPortrayal
				.setMap(new sim.util.gui.SimpleColorMap(0, 1, new Color(0, 0, 0, 0), new Color(255, 0, 0, 150)));

		workSitesPortrayal.setField(crimeWorld.sitesWork);
		workSitesPortrayal
				.setMap(new sim.util.gui.SimpleColorMap(0, 1, new Color(0, 0, 0, 0), new Color(211, 211, 211, 150)));

		educationSitesPortrayal.setField(crimeWorld.sitesEducation);
		educationSitesPortrayal
				.setMap(new sim.util.gui.SimpleColorMap(0, 1, new Color(0, 0, 0, 0), new Color(30, 144, 255, 150)));

		recreationSitesPortrayal.setField(crimeWorld.sitesRecreation);
		recreationSitesPortrayal
				.setMap(new sim.util.gui.SimpleColorMap(0, 1, new Color(0, 0, 0, 0), new Color(0, 0, 0, 150)));

		// trying to implement images for sites
		// sitesPortrayal.setPortrayalForClass(Home.class, new FacetedPortrayal2D(
		//
		// new SimplePortrayal2D[]
		// {
		//
		// //new ImagePortrayal2D(this.getClass(),
		// "src/main/resources/sim/app/pacman/blinkyu.png", 2),
		// new ImagePortrayal2D(null, null, afterSize);
		//
		// }));

	}


	protected Color sickGreenColor = new Color(255,255,0);
	//used to create the default console and yard where the agents move
	private void setupPortrayals() {
		CrimeWorld crimeWorld = (CrimeWorld) state;
		displayFrame.repaint();

		// resourcePortrayal.setField(crimeWorld.getResources().getPatch());
		// resourcePortrayal.setMap(
		// new sim.util.gui.SimpleColorMap(
		// 0.0,1.0,Color.white,Color.YELLOW));

		// tell the Portrayals what to portray and how to portray them
		
		Steps steps = new Steps(crimeWorld);

		societyPortrayal.setField(crimeWorld.yard);
		societyPortrayal.setPortrayalForAll(new OvalPortrayal2D() {
			
		// create agents of different colors from blue to red depdning on their stress levels
		public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				Criminal citizen = (Criminal)object;
				Rectangle2D boundary = new TextLayout("" + 11, font, graphics.getFontRenderContext()).getBounds();
				graphics.drawString("" + steps.getTime() + ":00", (int)((info.clip.width - boundary.getWidth()) / 2),  (int)((10)));
				if(citizen.isSick) {
					paint = new Color(80, 220, 100);
				}
				else {
					int StressShade = (int) (citizen.getStress() * 255 /100.0);
					if (StressShade > 255) StressShade = 255;
					paint = new Color(StressShade, 0, 255 - StressShade);
					
				}
				
				super.draw(object,  graphics, info);
			}

		});

		// reschedule the displayer
		display.reset();
		display.setBackdrop(Color.white);

		// redraw the display
		display.repaint();

	}

	JFrame chartFrame = new JFrame("Histogram");
	JFrame chartFrame1 = new JFrame("Histogram Example Time");
	JFrame chartFrame2 = new JFrame("LineGraph Example Time");;

	// In this method we construct a new display of size 600x600
	public void init(Controller c) {
		super.init(c);
		display = new Display2D(MAPHEIGHT, MAPWIDTH, this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("SimulatorDisplay");
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		
		display.attach(societyPortrayal, "Yard");

		// yard colors
		display.attach(homeSitesPortrayal, "Site Locations");
		display.attach(workSitesPortrayal, "Site Locations");
		display.attach(educationSitesPortrayal, "Site Locations");
		display.attach(recreationSitesPortrayal, "Site Locations");

		GeneratePieChart(c);
		GenerateHistogram(c);
		GenerateLineGraph(c);

		createTabs();
	}

	private void GeneratePieChart(Controller c) {

		pieChartDataset = new DefaultPieDataset();

		// this has to be here because the values arent able to be updated
		// pieChartDataset.setValue("No Home", getHomelessNumbers());
		pieChartDataset.setValue("Have a Home", 0);
		pieChartDataset.setValue("Homeless", 0);

		JFreeChart Piechart = ChartFactory.createPieChart("Pie Chart", pieChartDataset, true, true, false);
		Piechart.setTitle("Pie Chart");
		chartFrame1 = new ChartFrame("", Piechart);
		c.registerFrame(chartFrame1);

		chartFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void GenerateLineGraph(Controller c) {

		XYSeries seriesx = new XYSeries("TEST");
		final XYSeriesCollection datasetx = new XYSeriesCollection();

		double testX = 100.0;
		double testY = 2.0;
		seriesx.add(testX, testY);

		testX = testX + 100.0;
		testY = testY + 200.0;
		seriesx.add(testX, testY);

		datasetx.addSeries(seriesx);
		TimeSeriesChartGenerator timeChart = new TimeSeriesChartGenerator();
		timeChart.addSeries(seriesx, null);

		JFreeChart LineGraph = ChartFactory.createXYLineChart("TEST", "TEST", "TEST", datasetx);

		chartFrame2 = new ChartFrame("", LineGraph);
		c.registerFrame(chartFrame2);
		chartFrame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void GenerateHistogram(Controller c) {

		// adding chart information and dataset
		dataset = new HistogramDataset();
		JFreeChart chart = ChartFactory.createHistogram("My Histogram", "Income", "Crime", dataset,
				PlotOrientation.VERTICAL, true, false, false);
		chart.setTitle("Income Distribution");

		chartFrame = new ChartFrame("", chart);

		// perhaps you might move the chart to where you like.
		c.registerFrame(chartFrame1);

		chartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// the console automatically moves itself to the right of all
		// of its registered frames -- you might wish to rearrange the
		// location of all the windows, including the console, at this
		// point in time....

	}

	private void createTabs() {
		JTabbedPane graphTabs = new JTabbedPane();
		graphTabs.addTab("Charts", chartFrame.getContentPane());
		graphTabs.addTab("Charts 1", chartFrame1.getContentPane());
		graphTabs.addTab("Charts 2", chartFrame2.getContentPane());
		JFrame collectorFrame = new JFrame();
		collectorFrame.setTitle("Graphs");
		collectorFrame.add(graphTabs);
		collectorFrame.setVisible(true);
		collectorFrame.pack();
		controller.registerFrame(collectorFrame);

	}

	// the GUI is about to be destroyed, we dispose the frame
	// and set it to null
	public void quit() {
		super.quit();
		if (displayFrame != null)
			displayFrame.dispose();
		displayFrame = null;
		display = null;

		if (chartFrame != null)
			chartFrame.dispose();
		chartFrame = null;
		lineChart = null;
	}

}

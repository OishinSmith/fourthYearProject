package sim.app.crime;

import java.util.ArrayList;
import java.util.Collections;

//import org.junit.Assert;
//import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

//import org.junit.Test;
import sim.engine.SimState;
import sim.field.grid.IntGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Int2D;

@SuppressWarnings("serial")
public class CrimeWorld extends SimState {

	static final int GRIDWIDTH = 100;
	static final int GRIDHEIGHT = 100;
	Checker checker = new Checker();
	double globalTax = 0;
	Random random = new Random();
	double wealth = 0;
	int jobsForHire = 20;
	int currentDay = 0;
	Home Home = new Home();
	Work Work = new Work();
	Recreation Recreation = new Recreation();
	Shop Shop = new Shop();
	public final Int2D homeLocation = Home.location();
	public final Int2D WorkLocation = Work.location();
	public final Int2D RecreationLocation = Recreation.location();
	public List<Criminal> allCriminals = new ArrayList<Criminal>();
	List<Double> wealthValues = new ArrayList();
	List<Double> percentageOfWealth = new ArrayList();
	HashMap<Integer, Integer> jobsAvailable = new HashMap<Integer, Integer>();
	public final Int2D EducationLocation = Shop.location();
	IntGrid2D sitesHome = null;
	IntGrid2D sitesWork = null;
	IntGrid2D sitesEducation = null;
	IntGrid2D sitesRecreation = null;
	// steps keeps time of steps
	Steps steps = null;
	private Int2D[] randomLocations = { WorkLocation, homeLocation, RecreationLocation, EducationLocation };
	private double forceToCentreMultiplier = 0.01;
	private double randomMultiplier = 0.5;
	public int citizens = 20;

	// Model Tab ------------------------------------
	// somehow MASON recognises the values below as
	// the values on the console.
	// so if you use getNumCitizens(), the Console will
	// display NumCitizens under the Model tab

	// to make a constant graph of updating values, you need
	// to make it a list eg. int[] or double[]

	public Object domRandomMultiplier() {
		return new sim.util.Interval(0.0, 100.0);
	}
	// -----------------------------------------------------------

	public static Network buddies = new Network(false);

	SparseGrid2D yard = new SparseGrid2D(GRIDWIDTH, GRIDHEIGHT);

	public CrimeWorld(long seed) {
		super(seed);

	}

	static Bag allCitizens = buddies.getAllNodes();

	public static void main(String[] args) {
		doLoop(CrimeWorld.class, args);
		System.exit(0);
	}

	public void start() {
		super.start();

		Random random = new Random();

		Checker chekcer = new Checker();
		schedule.scheduleRepeating(chekcer);

		// clear yard
		yard.clear();

		// clear buddy relationships
		buddies.clear();

		Steps steps = new Steps(this);
		steps.getTime();

		// initialise Home site
		sitesHome = Home.sites;
		sitesHome.field[Home.location().x][Home.location().y] = 1;
		for (int i = Home.location().x - 3; i < Home.location().x + 3; i++) {
			for (int j = Home.location().y - 2; j < Home.location().y + 2; j++)
				sitesHome.field[i][j] = 1;
		}

		// initialise Work site
		sitesWork = Work.sites;
		sitesWork.field[Work.location().x][Work.location().y] = 1;
		for (int i = Work.location().x - 3; i < Work.location().x + 3; i++) {
			for (int j = Work.location().y - 2; j < Work.location().y + 2; j++)
				sitesWork.field[i][j] = 1;
		}

		// initialise Shop site
		sitesEducation = Shop.sites;
		sitesEducation.field[Shop.location().x][Shop.location().y] = 1;
		for (int i = Shop.location().x - 3; i < Shop.location().x + 3; i++) {
			for (int j = Shop.location().y - 2; j < Shop.location().y + 2; j++)
				sitesEducation.field[i][j] = 1;
		}

		// initialise Recreation site
		sitesRecreation = Recreation.sites;
		sitesRecreation.field[Recreation.location().x][Recreation.location().y] = 1;
		for (int i = Recreation.location().x - 3; i < Recreation.location().x + 3; i++) {
			for (int j = Recreation.location().y - 2; j < Recreation.location().y + 2; j++)
				sitesRecreation.field[i][j] = 1;
		}

		// adding citizens into yard ( grid )
		for (int i = 0; i < citizens; i++) {
			HashMap<String, String> citizenAttributes = createCitizenAttributes();
			Criminal criminal = new Criminal(i, this, steps, citizenAttributes);

			Random random2 = new Random();
			int randomInt = random.nextInt(((100 - 0) + 1) + 0);
			int randomInt2 = random.nextInt(((100 - 0) + 1) + 0);
			yard.setObjectLocation(criminal, new Int2D(randomInt, randomInt2));

			// here Im adding the buddy relationships between citizens
			buddies.addNode(criminal);
			schedule.scheduleRepeating(criminal);

			allCriminals.add(criminal);

		}

		// Network allows any object to be a node. Objects are
		// connected via edges defined by the class
		// sim.field.network.Edge. An Edge stores an info
		// object which labels the edge: this can be anything.

		// here, the edge is the attraction that a citizen will have
		// for another citizen. If citizens have no edge between them
		// then we assume they have no mutual opinion.

		// implementing a simple crime relationship
		Bag citizens = buddies.getAllNodes();
		for (int i = 0; i < citizens.size(); i++) {
			Object citizen = citizens.get(i);

			// who is the criminal going to go after?
			Object citizenB = null;
			do
				citizenB = citizens.get(random.nextInt(citizens.numObjs));
			while (citizen == citizenB);

			double buddiness = random.nextDouble();
			buddies.addEdge(citizen, citizenB, new Double(buddiness));

			// who does the criminal not go for?
			do
				citizenB = citizens.get(random.nextInt(citizens.numObjs));
			while (citizen == citizenB);
			buddiness = random.nextDouble();
			buddies.addEdge(citizen, citizenB, new Double(-buddiness));

		}

	}

	private HashMap<Integer, Integer> createJobs() {
		HashMap<Integer, Integer> jobs = new HashMap<Integer, Integer>();
		jobs.put(1, 0);
		jobs.put(2, (Integer) citizens / 2);
		jobs.put(3, (Integer) citizens / 4);
		jobs.put(4, (Integer) citizens / 10);
		return jobs;
	}

	// create Citizen attributes to be accessed by Criminal object

	public List<Criminal> retrieveAllCriminals() {
		return allCriminals;
	}

	private boolean[] randomBool = { true, false };

	private HashMap<String, String> createCitizenAttributes() {
		Random r = new Random();
		double wealth = 0;
		double needs = 0.0;
		needs = createRandomNeedsValue(needs);

		wealth = createwealthValue(wealth);
		double workMultiplier = createCitizenWorkMultiplier(wealth);
		boolean employed = amIEmployed(r.nextInt(2), wealth);
		boolean hasHome = doIHaveAHouse(r.nextInt(2), wealth);
		double startWorkAt = createMonringAfternoonOrEveningWorkingTime(r, wealth);
		boolean isLazy = createIsLazy(r.nextInt(2));

		HashMap<String, String> citizenAttributes = new HashMap<String, String>();
		citizenAttributes.put("wealth", wealth + ""); // convert wealth to string and back to
		citizenAttributes.put("amIEmployed", employed + "");
		citizenAttributes.put("doIHaveAHouse", hasHome + "");
		citizenAttributes.put("needs", needs + "");
		citizenAttributes.put("wealthMultiplier", workMultiplier + "");
		citizenAttributes.put("startWorkAt", startWorkAt + "");
		citizenAttributes.put("isLazy", isLazy + "");

		return citizenAttributes;

	}

	private boolean createIsLazy(int nextInt) {
		if (nextInt == 1)
			return true;
		else
			return false;
	}

	private double createCitizenWorkMultiplier(double wealth) {
		if (wealth < 100) {
			double multiplier = 1.05;
			return multiplier;
		} else if (100 <= wealth && wealth < 200) {
			double multiplier = 1.1;
			return multiplier;
		} else if (200 <= wealth && wealth < 300) {
			double multiplier = 1.3;
			return multiplier;
		} else {
			double multiplier = 1.5;
			return multiplier;
		}
	}

	// Depending on the amount of wealth each person makes, they'll have a broader
	// selection of working times. People with lower wealth will have less choice
	// meaning that they are the most likely people to be working overnight for
	// example
	private double[] randomMorningNumber = { 1.0, 2.0, 3.0 };
	private double[] randomAfternoonNumber = { 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 };
	private double[] randomEveningNumber = { 4.0, 5.0, 6.0, 7.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0,
			19.0 };
	public int robberies = 0;

	private int economy = 1;
	public double gini;
	private boolean welfareState = true;
	private double taxRate = 0.2;

	private double createMonringAfternoonOrEveningWorkingTime(Random r, double wealth) {
		if (wealth <= 100.0) {
			return randomEveningNumber[r.nextInt(randomEveningNumber.length)];
		}
		if (wealth <= 200) {
			return 0;
		}
		if (wealth <= 250) {
			return randomMorningNumber[r.nextInt(randomMorningNumber.length)];
		} else {
			return randomAfternoonNumber[r.nextInt(randomAfternoonNumber.length)];
		}
	}

	public double createRandomNeedsValue(double needs) {
		Random random = new Random();
		needs = random.nextDouble() * 100;
		return needs;
	}

	public double createwealthValue(double wealth) {
		Random random = new Random();
		double val = random.nextGaussian() * 100;
		wealth = val + 200; // mean will be 200, distribution values will cluster around 200
		return wealth;

	}

	public boolean doIHaveAHouse(int x, double wealth) {
		if (wealth <= 50.0) {
			return false;
		}
		return true;

	}

	public boolean amIEmployed(int x, double wealth) {
		if (wealth <= 50.0) {
			return randomBool[x];
		}
		return true;

	}

	void payBills(Criminal criminal) {
		criminal.payBills(1, this);
	}

	// This is where every day checks are run
	public void newDay() {
		currentDay++;
		billDay(1);

		if (random.nextInt(5) == 0) {
			changeEconomy();
		}

		// Here is where the stress checks are done

		stressChange();

		if (jobsForHire > 0) {
			givePromotion();
			jobsForHire--;
		}

		if (jobsForHire < 0) {
			giveDemotion();
			jobsForHire++;
		}

		System.out.println(getGini(getLorenz(), citizens));
		int one = 0;
		int two = 0;
		int three = 0;
		int four = 0;
		int zero = 0;
		for (int i = 0; i < allCriminals.size(); i++) {

			switch (allCriminals.get(i).getJob()) {
			case 0:
				zero++;
				break;
			case 1:
				one++;
				break;
			case 2:
				two++;
				break;
			case 3:
				three++;
				break;
			case 4:
				four++;
				break;

			}

		}
		System.out.println(
				"JOB 0: " + zero + " JOB 1: " + one + " JOB 2: " + two + " JOB 3: " + three + " JOB 4: " + four);
//		System.out.println("NUM OF ROBBERIES: " + robberies);

	}

	private void givePromotion() {
		allCriminals.get(random.nextInt(citizens)).promote();

	}

	private void giveDemotion() {
		Criminal subject = allCriminals.get(random.nextInt(citizens));
		if (subject.getJob() > 0) {
			subject.demote();
			jobsForHire++;
		}

	}

	private void stressChange() {
		for (int a = 0; a < allCriminals.size(); a++) {
			if (allCriminals.get(a).getWealth() < 200)
				allCriminals.get(a).increaseStress();
		}

	}

	private void changeEconomy() {
		if (jobsForHire < 0) {
			if (!(random.nextInt(3) == 0))
				jobsForHire = jobsForHire + 5;
			else
				jobsForHire = jobsForHire - 5;
		}

		else {
			if (!(random.nextInt(3) == 0))
				jobsForHire = jobsForHire - 5;
			else
				jobsForHire = jobsForHire + 5;
		}

	}

	private void billDay(int i) {
		for (int a = 0; a < allCriminals.size(); a++) {
			if (allCriminals.get(a).getWealth() > 0)
				allCriminals.get(a).payBills(i, this);
			else if (allCriminals.get(a).getJob() == 0) {
				allCriminals.get(a).recieveWelfare(i, this);
			}

		}

	}

	public double getGlobalTax() {
		return globalTax;
	}

	public void setGlobalTax(double d) {
		this.globalTax = d;
	}

	public int getEconomy() {
		return economy;
	}

	public boolean getWelfare() {
		return welfareState;
	}

	public double getTaxRate() {
		return taxRate;
	}

	public double getGini() {
		return gini;
	}

	public int getRobberies() {
		return robberies;
	}

	public int getJobsForHire() {
		return jobsForHire;
	}

	public void setJobsForHire(int jobsForHire) {
		this.jobsForHire = jobsForHire;
	}

	public HashMap<Integer, Integer> getJobsAvailable() {
		return jobsAvailable;
	}

	public Int2D getRandomLocation(int x) {
		return randomLocations[x];
	}

	public void setRandomLocations(Int2D[] randomLocations) {
		this.randomLocations = randomLocations;
	}

	public int getNumCitizens() {
		return citizens;
	}

	public void setNumCitizens(int val) {
		if (val > 0)
			citizens = val;
	}

	public int[] getNumCitizens1() {
		return retrieveCitizens();
	}

	public double getRandomMultiplier() {
		return randomMultiplier;
	}

	public void setRandomMultiplier(double val) {
		if (randomMultiplier >= 0.0)
			randomMultiplier = val;
	}

	public String[] getName() {
		Bag citizens = buddies.getAllNodes();
		String[] distro1 = new String[citizens.numObjs];
		for (int i = 0; i < citizens.numObjs; i++)
			distro1[i] = ((Criminal) (citizens.objs[i])).getNames();

		return distro1;
	}

	public double[] getStress() {
		Bag citizens = buddies.getAllNodes();
		double[] distro1 = new double[citizens.numObjs];
		for (int i = 0; i < citizens.numObjs; i++)
			distro1[i] = ((Criminal) (citizens.objs[i])).getStress();

		return distro1;
	}

	public int[] retrieveCitizens() {
		Bag citizens = buddies.getAllNodes();
		int[] size = new int[citizens.size()];
		for (int i = 0; i < citizens.size(); i++)
			size[i] = citizens.size();
		return size;
	}

	public Criminal[] retrieveCitizenObjects() {
		Bag citizens = buddies.getAllNodes();
		Criminal[] distro1 = new Criminal[citizens.numObjs];
		for (int i = 0; i < citizens.numObjs; i++)
			distro1[i] = ((Criminal) (citizens.objs[i])).getCriminal();
		return distro1;
	}

	public static double[] getWealth() {
		Bag citizens = buddies.getAllNodes();
		double[] distro1 = new double[citizens.numObjs];
		for (int i = 0; i < citizens.numObjs; i++)
			distro1[i] = ((Criminal) (citizens.objs[i])).getWealth();

		return distro1;
	}

	public static boolean[] retrieveHomelessNumber() {
		Bag allCitizens = buddies.getAllNodes();
		boolean[] size = new boolean[allCitizens.size()];
		for (int i = 0; i < allCitizens.numObjs; i++)
			size[i] = (boolean) ((Criminal) (allCitizens.objs[i])).amIHomeless();
		return size;
	}

	public static boolean[] retrieveEmployedNumbers() {
		Bag allCitizens = buddies.getAllNodes();
		boolean[] size = new boolean[allCitizens.size()];
		for (int i = 0; i < allCitizens.numObjs; i++)
			size[i] = (boolean) ((Criminal) (allCitizens.objs[i])).amIEmployed();
		return size;
	}

	private List<Double> getLorenz() {
		List<Double> wealthValues = new ArrayList();
		double totalPopWealth = 0;

		for (int a = 0; a < allCriminals.size(); a++) {
			wealthValues.add(allCriminals.get(a).getIncome());
			totalPopWealth = totalPopWealth + allCriminals.get(a).getIncome();
		}
		Collections.sort(wealthValues);

		return wealthValues;

	}

	public double getGini(List<Double> wealths, int pop) {

		double sum = 0;
		for (int i = 0; i < wealths.size(); i++) {
			sum = sum + wealths.get(i);
		}

		double mean = sum / pop;

		sum = 0;
		for (int i = 0; i < wealths.size(); i++) {
			for (int j = 0; j < wealths.size(); j++) {
				sum = sum + Math.abs(wealths.get(i) - wealths.get(j));
			}
		}

		if (mean == 0) {
			return 0.0;
		}

		gini = 1 - (sum / (2 * mean * (pop * ((double) pop - 1))));
		return gini;
	}

}

package sim.app.crime;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.IntBag;

import java.util.HashMap;
import java.util.Random;

public class Criminal implements Steppable {

	int citizenNumber;

	Random random = new Random();
	int randomX = random.nextInt(100);
	int RandomY = random.nextInt(100);
	Int2D target = new Int2D(randomX, RandomY);
	Criminal targetToRob;
	Int2D myLocation = null;
	boolean isBeingRobbed = false;
	boolean isRobbing = false;
	// Agent properties
	public double STRESS = 0;
	public double SLEEP = 0;
	private boolean employed = false;
	private boolean hasHome;
	private boolean isHungry = false;
	private boolean isStarving = false;
	private boolean robbingTarget = false;
	private boolean newDayFlag;
	double income = 100.0;
	int job = 1;

	// if stress is high enough you may get sick
	boolean isSick = false;
	private double chanceOfGettingSick = 0.1;
	private boolean isSickFlag = false;

	int waitingTime = 0;
	String name = "";

	Random r = new Random();
	double stress = -1.0;
	double wealth;
	private CrimeWorld crimeWorld;
	private Steps steps;
	private Int2D criminalTarget;

	private boolean robbing = false;
	private Criminal targetCitizen;
	private Criminal targetCriminal;

	private double moneyToSteal = 300;
	private double stolenMoney;
	private double needs;
	private double wealthMultiplier;
	private double startWorkAt;
	private boolean lazy;

	public static final double MAX_FORCE = 3.0;

	private static Int2D[] randomLocations = { new Int2D(1, 1), new Int2D(25, 100), new Int2D(75, 100),
			new Int2D(100, 1) };

	public Criminal(int i, CrimeWorld crimeWorld, Steps steps, HashMap<String, String> citizenAttributes) {
		citizenNumber = i;
		this.crimeWorld = crimeWorld;
		this.steps = steps;
		this.wealth = Double.parseDouble(citizenAttributes.get("wealth"));
		this.hasHome = Boolean.parseBoolean(citizenAttributes.get("doIHaveAHouse"));
		this.employed = Boolean.parseBoolean(citizenAttributes.get("amIEmployed"));
		this.needs = Double.parseDouble(citizenAttributes.get("needs"));
		this.wealthMultiplier = Double.parseDouble(citizenAttributes.get("wealthMultiplier"));
		this.startWorkAt = Double.parseDouble(citizenAttributes.get("startWorkAt"));
		this.lazy = Boolean.parseBoolean(citizenAttributes.get("isLazy"));
	}

	public void IncrementNeeds() {
		needs = needs - 0.01;
	}

	public void satisfyNeeds() {
		double satisfied = 100.0;
		needs = satisfied;
	}

	public void robWealth(double robbed) {
		wealth = wealth - robbed;
	}

	public double getWealth() {
		// wealth attribute
		return wealth;
	}

	public void robwealth(double robbed) {
		wealth = wealth - robbed;
	}

	public void spendMoney() {
		double moneySpentOnStuff = 20;
		wealth = wealth - moneySpentOnStuff;
	}

	public void gainwealth(double robbed) {
		wealth = wealth + robbed;
	}

	public void reduceSetStress(double reduceBy) {
		if ((stress - reduceBy) >= 0) {
			stress = stress - reduceBy;
		} else {
			stress = 0;
		}
	}

	// properties cannot be set in a Simstate step method
	// because each step

	// this gets x and y values from 0 - 1.0. These values determine the attraction
	// of an agent towards another one

	public void step(SimState state) {
		CrimeWorld crimeWorld = (CrimeWorld) state;
		SparseGrid2D yard = crimeWorld.yard;

		this.checkIfNewDay();
		this.startFeelingBetter();
		this.IncrementNeeds();

		if (newDayFlag && ((steps.getTime() + startWorkAt) % 24 >= 9.0)) {

			this.contractingDesease(newDayFlag);
			newDayFlag = false;
		}

		myLocation = yard.getObjectLocation(this);

		if (this.getNeeds() < 30.0) {
			this.isHungry = true;

			crimeWorld.yard.setObjectLocation(this, moveToShop(myLocation, crimeWorld)); // Shop should be changed to
																							// Shop.

		}

		if (this.getStress() < 70 && this.isHungry == false) {
			crimeWorld.yard.setObjectLocation(this, moveTowardsGoal(myLocation, crimeWorld));

		} else if (targetToRob != null) {
			commitCrime(crimeWorld);
			crimeWorld.yard.setObjectLocation(this, this.getLocation());
		} else if (this.getStress() >= 70 && wealth < 200 && targetToRob == null) {
			chooseTarget(crimeWorld);
		}

	}

	private void startFeelingBetter() {
		if (this.isSick && this.newDayFlag) {
			setisSick(false);
			isSickFlag = false;
		}
	}

	private void contractingDesease(boolean isNewDay) {
		if (this.stress > 70 && isNewDay) {
			boolean randomBoolean = Math.random() < 0.1;
			this.isSick = randomBoolean;
		} else if (this.stress <= 70 && isNewDay) {
			// theres a 0.05% chance of a person getting sick
			boolean randomBoolean = Math.random() < 0.05;
			this.isSick = randomBoolean;
		}

	}

	private void checkIfCriminalIsCloseToCitizen() {
		if (targetCitizen != null && targetCitizen.getRobbed() == true && this.getRobbing() == true) {
			stolenMoney = 100.0;

			if (myLocation.x == criminalTarget.x + 1) {

				// gain and reduce wealth of Criminal and citizen
				targetCitizen.robwealth(stolenMoney);
				this.gainwealth(stolenMoney);
				reduceSetStress(30.0);
				return;
			} else if (myLocation.x == criminalTarget.x - 1) {

				targetCitizen.robwealth(stolenMoney);
				this.gainwealth(stolenMoney);
				reduceSetStress(30.0);
				return;
			}
			if (myLocation.y == criminalTarget.y - 1) {

				targetCitizen.robwealth(stolenMoney);
				this.gainwealth(stolenMoney);
				reduceSetStress(30.0);
				return;
			} else if (myLocation.y == criminalTarget.y + 1) {

				targetCitizen.robwealth(stolenMoney);
				this.gainwealth(stolenMoney);
				reduceSetStress(30.0);
				return;
			} else {

				// reset all values for targetCitizens
				targetCitizen.setRobbed(false);
				targetCitizen.setRobbing(false);
				targetCitizen = null;
				crimeWorld.robberies++;

			}

		}

	}

	// if gettime == 0.0 and startWorkAt == 9.0 then
	private void checkIfNewDay() {
		if (((steps.getTime() + this.startWorkAt) % 24.0 == 0.0) && newDayFlag == false) {
			newDayFlag = true;
		}

	}

	private Int2D moveToShop(Int2D myLocation, CrimeWorld crimeWorld) {
		target = crimeWorld.Shop.location();
		if ((myLocation.x == crimeWorld.Shop.location().x) && (myLocation.y == crimeWorld.Shop.location().y)) {
			spendMoney();
			satisfyNeeds();
			this.isHungry = false;
		}

		if (this.getNeeds() < 5.0) {
			this.isStarving = true;
			crimeWorld.yard.setObjectLocation(this, crimeWorld.Shop.location());
		}

		if (!myLocation.equals(target)) {

			int x = myLocation.x, y = myLocation.y;
			return moveAgentFromMyLocationToTargetLocation(x, y);
		}
		return myLocation;

	}

	public void start() {

	}

	public void finish() {

	}

	public Bag getNeighbors(int x, int y, Int2D myLocation, CrimeWorld crimeWorld, SparseGrid2D yard, int j) {

		Bag objectsAtLocation = null;
		// scan area
		int i = x - j;
		while (i <= x + j && targetCitizen == null) {
			int k = y - j;
			while (k <= y + j) {
				objectsAtLocation = yard.getObjectsAtLocation(i, k);
				if (objectsAtLocation != null && (i != x && k != y)) {
					// System.out.println(i + " " + k + " " + this.citizenNumber);
					objectsAtLocation = yard.getObjectsAtLocation(i, k);
					return objectsAtLocation;
				}
				k = k + 1;
			}
			i = i + 1;
		}
		// this should only return nulls
		return objectsAtLocation;

	}

	private void chooseTarget(CrimeWorld crimeWorld) {
		Bag nearbyCitizens = getNeighbors(myLocation.x, myLocation.y, myLocation, crimeWorld, crimeWorld.yard, 5);
		if (nearbyCitizens != null)
			targetToRob = (Criminal) nearbyCitizens.get(0);
		else {

			crimeWorld.yard.setObjectLocation(this, moveRandomly());
		}
	}

	private void commitCrime(CrimeWorld crimeWorld) {

		Bag nearbyCitizens = getNeighbors(myLocation.x, myLocation.y, myLocation, crimeWorld, crimeWorld.yard, 1);
		if (nearbyCitizens == null)
			crimeWorld.yard.setObjectLocation(this, moveCriminalFromMyLocationToTargetLocation());
		else if (nearbyCitizens.contains(targetToRob)) {
			targetToRob.wealth = targetToRob.wealth - 200;
			wealth = wealth + 200;
			stress = stress - 50;
			targetToRob = null;
			crimeWorld.robberies++;
			System.out.println("CRIME: JOB = " + job + ", WEALTH = " + wealth + ", INCOME = " + income);
		} else
			crimeWorld.yard.setObjectLocation(this, moveCriminalFromMyLocationToTargetLocation());

	}

	private Int2D moveRandomly() {
		int x = myLocation.x;
		int y = myLocation.y;

		boolean leftBound = x == 0;
		boolean rightBound = x == 100;
		boolean upperBound = y == 0;
		boolean lowerBound = y == 100;

		Bag objectsAtLocation = crimeWorld.yard.getObjectsAtLocation(x, y);
		objectsAtLocation = getNeighbors(x, y, myLocation, crimeWorld, crimeWorld.yard, 5);

		if (objectsAtLocation != null) {
			criminalTarget = crimeWorld.yard.getObjectLocation(objectsAtLocation.getValue(0));
			targetCitizen = (Criminal) crimeWorld.yard.getObjectsAtLocation(criminalTarget).getValue(0);
			targetCitizen.setRobbed(true);
			this.setRobbing(true);
			robbingTarget = targetCitizen.isBeingRobbed;
		}

		// make the target citizen stop moving and move the criminal towards the target
		// citizen
		if (targetCitizen != null && (targetCitizen.getLocation().x != x && targetCitizen.getLocation().y != y)) {
			// System.out.println(criminalTarget);
			return moveCriminalFromMyLocationToTargetLocation();
		}

		if (r.nextBoolean() && !rightBound)
			x++;
		else if (!leftBound)
			x--;

		if (r.nextBoolean() && !upperBound)
			y++;
		else if (!lowerBound)
			y--;

		return new Int2D(x, y);
	}

	private Int2D moveCriminalFromMyLocationToTargetLocation() {
		int x = myLocation.x;
		int y = myLocation.y;

		int targetX = targetToRob.myLocation.x;
		int targetY = targetToRob.myLocation.y;

		if (x < targetX + 2) {
			x = x + 2;
		} else if (x > targetX + 2) {
			x = x - 2;
		} else if (x < targetX + 1) {
			x++;
		} else if (x > targetX + 1) {
			x--;
		}
		if (y < targetY + 2) {
			y = y + 2;
		} else if (y > targetY + 2) {
			y = y - 2;
		} else if (y < targetY + 1) {
			y++;
		} else if (y > targetY + 1) {
			y--;
		}
		return new Int2D(x, y);

	}

	// move towards the location
	private Int2D moveTowardsGoal(Int2D myLocation, CrimeWorld crimeWorld) {

		// if waiting time is not 0, make it equal 0

		if (waitingTime != 0) {

			citizenIsAddedToLocation();
			return myLocation;

		}

		// if waiting time == 0 then start moving to next location. If myLocation ==
		// target then make waitTime equal to a number
		else {
			// if int2D[10,90] does not equal target int2D[1,10] then move to location

			if (!myLocation.equals(target)) {

				citizenIsRemovedFromLocation();
				int x = myLocation.x, y = myLocation.y;
				return moveAgentFromMyLocationToTargetLocation(x, y);
			}

			// if myLocation equals target, get next target
			else {

				// Move to a random location
				ChooseRandomLocation();

				// make target location Home if SLEEP is smaller than 50
				if (this.employed && (9.0 <= (steps.getTime() + startWorkAt) % 24)
						&& ((steps.getTime() + startWorkAt) % 24 <= 17.0)) {
					target = crimeWorld.Work.location();
					if (target.equals(myLocation)) {
						earnWealth(crimeWorld);
					}

				}

				if (this.hasHome && ((23.0 <= (steps.getTime() + startWorkAt) % 24)
						|| ((steps.getTime() + startWorkAt) % 24 < 6.0))) {
					target = crimeWorld.Home.location();
					// System.out.println("new day for "+this.citizenNumber);

					if (this.SLEEP < 50) {
						sleepIfAtHome();
					}
				}

				this.chill();

				return moveTowardsGoal(myLocation, crimeWorld);

			}

		}

	}

	private void earnWealth(CrimeWorld crimeWorld) {
		wealth = wealth + (income * (1 - crimeWorld.getTaxRate()));

	}

	// choose a random location for the citizen to move to
	private void ChooseRandomLocation() {
		Random random = new Random();
		randomX = random.nextInt(100);
		RandomY = random.nextInt(100);
		target = new Int2D(randomX, RandomY);

	}

	// agent leaves the location
	private void citizenIsRemovedFromLocation() {

		// System.out.println(steps.getSteps());
		if (myLocation.equals(crimeWorld.Home.location())) {
			crimeWorld.Home.removePersonFromSite(this, crimeWorld);
			// crimeWorld.yard.remove(this);
		} else if (myLocation.equals(crimeWorld.Work.location())) {
			crimeWorld.Work.removePersonFromSite(this, crimeWorld);
			// crimeWorld.yard.remove(this);
		} else if (myLocation.equals(crimeWorld.Recreation.location())) {
			crimeWorld.Recreation.removePersonFromSite(this, crimeWorld);
			// crimeWorld.yard.remove(this);
		} else if (myLocation.equals(crimeWorld.Shop.location())) {
			crimeWorld.Shop.removePersonFromSite(this, crimeWorld);
			// crimeWorld.yard.remove(this);
		}

	}

	// Agent enters the location
	private void citizenIsAddedToLocation() {

		if (myLocation.equals(crimeWorld.Home.location())) {
			crimeWorld.Home.addPersonToSite(this, crimeWorld);
			// crimeWorld.yard.remove(this);
		} else if (myLocation.equals(crimeWorld.Work.location())) {
			crimeWorld.Work.addPersonToSite(this, crimeWorld);
			// crimeWorld.yard.remove(this);
		} else if (myLocation.equals(crimeWorld.Recreation.location())) {
			crimeWorld.Recreation.addPersonToSite(this, crimeWorld);
			// crimeWorld.yard.remove(this);
		} else {
			crimeWorld.Shop.addPersonToSite(this, crimeWorld);
			// crimeWorld.yard.remove(this);
		}

	}

	private void sleepIfAtHome() {
		if (myLocation.equals(crimeWorld.Home.location())) {
			// System.out.println(this.SLEEP);
			this.SLEEP = crimeWorld.Home.citizenStartSleeping(this);

		}

	}

	private Int2D moveAgentFromMyLocationToTargetLocation(int x, int y) {
		if (myLocation.x < target.x)
			x++;
		else if (myLocation.x > target.x)
			x--;
		if (myLocation.y < target.y)
			y++;
		else if (myLocation.y > target.y)
			y--;
		return new Int2D(x, y);

	}

	// pick a random place i.e. WORK, HOME, SCHOOL, RECREATION and move towards it
	private Int2D getRandomLocation(CrimeWorld crimeWorld) {

		return crimeWorld.getRandomLocation(r.nextInt(4));

	}

	public void chill() {
		if (waitingTime == 0)
			waitingTime = r.nextInt(50) + 50;
		else
			waitingTime--;

	}

	public void processingRobbing() {
		if (waitingTime == 0)
			waitingTime = r.nextInt(100) + 100;
		else
			waitingTime--;

	}

	// Half of the bills paid goes to the global tax fund
	public void payBills(int i, CrimeWorld crimeWorld2) {
		double taxRate = crimeWorld2.getTaxRate();
		double bills = 50 + (r.nextInt(300) + 100);

		wealth = wealth - bills;

		crimeWorld2.setGlobalTax(crimeWorld2.getGlobalTax() + (income * taxRate));

		if (this.isSick) {
			wealth = wealth - 150;
		}

	}

	public void recieveWelfare(int i, CrimeWorld crimeWorld2) {
		if (crimeWorld2.getGlobalTax() > 0 && crimeWorld2.getWelfare()) {
			wealth = wealth + 50;
			crimeWorld2.setGlobalTax(crimeWorld2.getGlobalTax() - 50);
		}

	}

	public void promote() {
		if (job < 4)
			switch (job) {
			case 0:
				job++;
				income = 200.0;
				break;
			case 1:
				job++;
				income = 600.0;
				break;
			case 2:
				job++;
				income = 1000.0;
				break;
			case 3:
				job++;
				income = 1400.0;
				break;

			}
	}

	public void increaseStress() {
		if (stress < 95)
			stress = stress + 5;

	}

	public void demote() {
		if (job > 0)
			switch (job) {
			case 1:
				job--;
				income = 0.0;
				break;
			case 2:
				job--;
				income = 200.0;
				break;
			case 3:
				job--;
				income = 600.0;
				break;
			case 4:
				job--;
				income = 1000.0;
				break;

			}
	}

	public double getNeeds() {
		return needs;
	}

	public int getCitizenNumber() {
		return citizenNumber;
	}

	public double getIncome() {
		return income;
	}

	public int getJob() {
		return job;
	}

	public Criminal getCriminal() {
		return this;
	}

	public String getNames() {
		if (name == "") {
			name = "citizen " + citizenNumber;
		}

		return name;
	}

	public boolean getRobbed() {
		return isBeingRobbed;
	}

	public void setRobbed(boolean isCitizenBeingRobbed) {
		isBeingRobbed = isCitizenBeingRobbed;
	}

	public boolean getRobbing() {
		return isRobbing;
	}

	public void setRobbing(boolean isRobbingCitizen) {
		isRobbing = isRobbingCitizen;
	}

	public boolean getisSick() {
		return isSick;
	}

	public void setisSick(boolean isCitizenSick) {
//		System.out.println(isSick + " " + isCitizenSick);
		isSick = isCitizenSick;
	}

	public double getStress() {
		// stress property
		if (stress == -1.0) {
			Random random = new Random();
			stress = random.nextDouble();
			stress = stress * 100.0;
		}
		return stress;
	}

	public boolean amIHomeless() {
		return hasHome;

	}

	public boolean amIEmployed() {
		return employed;

	}

	public Int2D getLocation() {
		SparseGrid2D yard = crimeWorld.yard;
		return yard.getObjectLocation(this);
	}

}

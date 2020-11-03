package sim.app.crime;

import java.util.HashMap;
import java.util.List;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.IntBag;

public class Checker implements Steppable{
	
	Criminal testCriminal;

	@Override
	public void step(SimState state) {
		CrimeWorld crimeWorld = (CrimeWorld) state;
		
		nextDay(crimeWorld);
		
//		if(crimeWorld.schedule.getSteps() % 100 == 0) {
//			System.out.println(getAllHomeless(crimeWorld));
//		}
		
//		System.out.println("MY INCOME IS " + testCriminal.getIncome());
		
	}
	
	public int getAllHomeless(SimState state) {
		CrimeWorld crimeWorld = (CrimeWorld) state;
		
		
		return checkHomelessNumber(crimeWorld);
	}


	public int checkHomelessNumber(CrimeWorld crimeWorld){
	
	HashMap<String, Integer> homelessNumbers = new HashMap<String, Integer>();

	for (int i = 0; i < crimeWorld.getHomelessNumbers().length; i++) {

		
		if (crimeWorld.getHomelessNumbers()[i] == false) {
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
		return homelessNumbers.get("is Homeless");
	}
	
	static void nextDay(SimState state) {
		CrimeWorld crimeWorld = (CrimeWorld) state;
//		System.out.println("GLOBAL TAX IS AT: " + crimeWorld.getGlobalTax());
		
		if(crimeWorld.schedule.getSteps()%1000 == 0) {
			crimeWorld.newDay();
		}
		
		
	}
	
	
}

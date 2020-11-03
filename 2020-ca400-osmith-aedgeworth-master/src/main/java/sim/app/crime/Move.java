package sim.app.crime;

import java.util.Random;

import sim.engine.SimState;
import sim.util.Int2D;

public class Move {
	
	private SimState state;
	private static Criminal criminal;
	private static CrimeWorld crimeWorld;
	
	static Random random = new Random();
	static int randomX = random.nextInt(100);
	static int RandomY = random.nextInt(100);
	static Int2D target = new Int2D(randomX, RandomY);
	static Int2D myLocation = null;
	static int waitingTime = 0;
	

	public Move(Criminal criminal, CrimeWorld crimeWorld) {
		this.criminal = criminal;
		this.crimeWorld = crimeWorld;
		
	}
	
	public long getSteps(SimState state) {
		return state.schedule.getSteps();
		
	}
	
	public static void main(String[] args) {
		
		myLocation = criminal.getLocation();
		moveTowardsGoal(myLocation, crimeWorld, criminal);
	
	}
	
	static Int2D moveTowardsGoal(Int2D myLocation, CrimeWorld crimeWorld, Criminal criminal) {
		
		// if waiting time is not 0, make it equal 0
		
		if (waitingTime != 0) {
			
			citizenIsAddedToLocation();

			return myLocation;

		}
		
		// if waiting time == 0 then start moving to next location. If myLocation == target then make waitTime equal to a number
		else {
			// if int2D[10,90] does not equal target int2D[1,10] then move to location
			
			if (!myLocation.equals(target)) {
				citizenIsRemovedFromLocation();
				
				int x = myLocation.x, y = myLocation.y;
				return moveAgentFromMyLocationToTargetLocation(x, y);
			} 
			
			// if myLocation equals target, get next target
			else {

				ChooseRandomLocation();
				
				// make target location Home if SLEEP is smaller than 50
				//SimState state = crimeWorld.state;
				//if(crimeWorld.steps.getSteps(state) ) {
					
				//}
				
				if(criminal.SLEEP < 50.0) {
					
					target = crimeWorld.Home.location();
					sleepIfAtHome();
				}
				
				chill();
				return moveTowardsGoal(myLocation, crimeWorld, criminal);
				
			} 
		}
	}
	
	// choose a random location for the citizen to move to
		private static void ChooseRandomLocation() {
			Random random = new Random();
			randomX = random.nextInt(100);
			RandomY = random.nextInt(100);
			target = new Int2D(randomX, RandomY);
			
		}

		// agent leaves the location
		private static void citizenIsRemovedFromLocation() {
			System.out.println(myLocation);
			System.out.println(crimeWorld.Home.location());
			if(myLocation.equals(crimeWorld.Home.location())) {
				crimeWorld.Home.removePersonFromSite(criminal, crimeWorld);
				//crimeWorld.yard.remove(this);
			}
			else if(myLocation.equals(crimeWorld.Work.location())) {
				crimeWorld.Work.removePersonFromSite(criminal, crimeWorld);
				//crimeWorld.yard.remove(this);
			}
			else if(myLocation.equals(crimeWorld.Recreation.location())) {
				crimeWorld.Recreation.removePersonFromSite(criminal, crimeWorld);
				//crimeWorld.yard.remove(this);
			}
			else if(myLocation.equals(crimeWorld.Shop.location())) {
				crimeWorld.Shop.removePersonFromSite(criminal, crimeWorld);
				//crimeWorld.yard.remove(this);
			}
			
		}

		// Agent enters the location
		private static void citizenIsAddedToLocation() {
			
			if(myLocation.equals(crimeWorld.Home.location())) {
				crimeWorld.Home.addPersonToSite(criminal, crimeWorld);
				//crimeWorld.yard.remove(this);
			}
			else if(myLocation.equals(crimeWorld.Work.location())) {
				crimeWorld.Work.addPersonToSite(criminal, crimeWorld);
				//crimeWorld.yard.remove(this);
			}
			else if(myLocation.equals(crimeWorld.Recreation.location())) {
				crimeWorld.Recreation.addPersonToSite(criminal, crimeWorld);
				//crimeWorld.yard.remove(this);
			}
			else {
				crimeWorld.Shop.addPersonToSite(criminal, crimeWorld);
				//crimeWorld.yard.remove(this);
			}
			
		}
		
		

		private static void sleepIfAtHome() {
			if(myLocation.equals(crimeWorld.Home.location() )) {
				System.out.println(criminal.SLEEP);
				criminal.SLEEP = crimeWorld.Home.citizenStartSleeping(criminal);
				
			}
			
		}

		private static Int2D moveAgentFromMyLocationToTargetLocation(int x, int y) {
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

			return crimeWorld.getRandomLocation(random.nextInt(4));

		}

		public static void chill() {
			if(waitingTime==0)
			waitingTime = random.nextInt(50) + 50;
			else
				waitingTime--;

		}
	
}
package sim.app.crime;

import sim.engine.SimState;

public class Steps {
	
	private SimState state;
	private CrimeWorld crimeWorld;
	private double time;
	

	public Steps(CrimeWorld crimeWorld) {
		this.crimeWorld = crimeWorld;
	}
	
	public void step(SimState state) {
		CrimeWorld crimeWorld = (CrimeWorld) state;
	}
	
	public long getTime() {
		//time = ((crimeWorld.schedule.getSteps() % 1200) / 50 );
		time = ((crimeWorld.schedule.getSteps() % 1200) / 50 );
		//System.out.println(time);
		return (long) time;
		
	}
	
	public long getNewDayTime() {
		//time = ((crimeWorld.schedule.getSteps() % 1200) / 50 );
		if(time != ((crimeWorld.schedule.getSteps() % 1200) / 50 )) {
			time = ((crimeWorld.schedule.getSteps() % 1200) / 50 );
			System.out.println(time);
			return (long) time;
		}
		return 0;
	}
	
}
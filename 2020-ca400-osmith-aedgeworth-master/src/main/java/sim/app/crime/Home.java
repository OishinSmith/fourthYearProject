package sim.app.crime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import sim.field.grid.IntGrid2D;
import sim.util.Int2D;

public class Home implements Site {
	
	private Set<Criminal> criminals = new HashSet<Criminal>();
	final Int2D HOME = new Int2D(90, 90);
	double sleepRecoveryRate = 10;
	
	public IntGrid2D sites = new IntGrid2D(CrimeWorld.GRIDWIDTH, CrimeWorld.GRIDHEIGHT, 0);
	
	public Home() {
		
	}
	
	public Set<Criminal> getCriminals() {
		return criminals;
	}
	
	@Override
	public Int2D location() {
		return HOME;
	}
	
	@Override
	public String locationName() {
		return "HOME";
	}
	
	@Override
	public void resources() {
		
	}
	
	@Override
	public void whoIsInside() {
		//String[] criminalsString;
		//for(int i = 0; i < criminals.size(); i++)
		//	criminalsString[i] = criminals[i];
		//return criminalsString;
	}
	
	@Override
	public void howManyInside() {
		
	}
	
	@Override
	public void color() {
		
	}
	
	@Override
	public void addPersonToSite(Criminal criminal, CrimeWorld crimeWorld) {
		//System.out.println(criminal.name);
		criminals.add(criminal);
		criminal.chill();
	}
	
	@Override
	public synchronized void removePersonFromSite(Criminal criminal, CrimeWorld crimeWorld) {
		criminals.remove(criminal);
	}
	
	public double citizenStartSleeping(Criminal criminal) {
		criminal.SLEEP += sleepRecoveryRate;
		return criminal.SLEEP;
		
	}
	
}
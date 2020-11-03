package sim.app.crime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sim.field.grid.IntGrid2D;
import sim.util.Int2D;

class Recreation implements Site {
	
	private Set<Criminal> criminals = new HashSet<Criminal>();
	private Int2D RECREATION= new Int2D(10, 90);
	
	public IntGrid2D sites = new IntGrid2D(CrimeWorld.GRIDWIDTH, CrimeWorld.GRIDHEIGHT, 0);
	
	@Override
	public Int2D location() {
		return RECREATION;
	}
	
	@Override
	public String locationName() {
		return "RECREATION";
	}
	
	@Override
	public void resources() {
		
	}
	
	@Override
	public void whoIsInside() {
		
	}
	
	@Override
	public void howManyInside() {
		
	}
	
	@Override
	public void color() {
		
	}
	
	@Override
	public void addPersonToSite(Criminal criminal, CrimeWorld crimeWorld) {
		criminals.add(criminal);
		criminal.chill();
	}
	
	@Override
	public synchronized void removePersonFromSite(Criminal criminal, CrimeWorld crimeWorld) {
		criminals.remove(criminal);
	}
	
}
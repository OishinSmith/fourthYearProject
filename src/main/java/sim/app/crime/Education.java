package sim.app.crime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sim.field.grid.IntGrid2D;
import sim.util.Int2D;

class Shop implements Site {
	
	private Set<Criminal> criminals = new HashSet<Criminal>();
	private Int2D SHOP= new Int2D(90, 10);
	
	public IntGrid2D sites = new IntGrid2D(CrimeWorld.GRIDWIDTH, CrimeWorld.GRIDHEIGHT, 0);
	
	@Override
	public Int2D location() {
		return SHOP;
	}
	
	@Override
	public String locationName() {
		return "SHOP";
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
		//System.out.println(criminal.name);
		criminals.add(criminal);
		criminal.chill();
	}
	
	@Override
	public synchronized void removePersonFromSite(Criminal criminal, CrimeWorld crimeWorld) {
		criminals.remove(criminal);
	}
}
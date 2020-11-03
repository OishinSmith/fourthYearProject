package sim.app.crime;

import sim.util.Int2D;

interface Site {
	
	public Int2D location();
	
	public String locationName();
	
	public void resources();
	
	public void whoIsInside();
	
	public void howManyInside();
	
	public void color();
	
	public void addPersonToSite(Criminal criminal, CrimeWorld crimeWorld);
	
	public void removePersonFromSite(Criminal criminal, CrimeWorld crimeWorld);
	
}
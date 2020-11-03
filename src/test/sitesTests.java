package test.test;

import java.util.Set;

import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import sim.app.crime.Home;
import sim.app.crime.Criminal;
import sim.app.crime.CrimeWorld;
import sim.util.Int2D;

/**
 * Unit test for simple App.
 */
public class sitesTests 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public sitesTests( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( sitesTests.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
	
	public void testCitizensNumber() {
		Home tester = new Home(); // MyClass is tested

	    // assert statements
		final Int2D HOME = new Int2D(90, 90);
	    Assert.assertEquals(HOME, tester.location());
	 }
	
	public void testLocationName() {
		Home tester = new Home(); // MyClass is tested

	    // assert statements
	    Assert.assertEquals("HOME", tester.locationName());
	 }
	
	public void testAddPersonToSite() {
		Home tester = new Home(); // MyClass is tested
		CrimeWorld crimeWorld = new CrimeWorld(0);
		crimeWorld.start();
		Criminal[] allCitizens = crimeWorld.retrieveCitizenObjects();
		System.out.println(crimeWorld.retrieveCitizenObjects());
		Set<Criminal> results = null;
	    // assert statements
		for(int i = 0; i < allCitizens.length; i++) {
			tester.addPersonToSite(allCitizens[i], crimeWorld);
			results = tester.getCriminals();
		}
		Assert.assertEquals(20, results.size());
	 }
}

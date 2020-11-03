package test.test;

import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import sim.app.crime.CrimeWorld;
import sim.app.crime.Criminal;

/**
 * Unit test for simple Criminal
 */
public class CriminalTests 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CriminalTests( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CriminalTests.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    


	public void testStress() {
		CrimeWorld crimeWorld = new CrimeWorld(0);
		crimeWorld.start();
		Criminal[] allCitizens = crimeWorld.retrieveCitizenObjects();
		System.out.println(crimeWorld.retrieveCitizenObjects());
		
	    // assert statements
		for(int i = 0; i < allCitizens.length; i++)
			if(allCitizens[i].getStress() >= 0.0) {
				Assert.assertTrue(true);
			}
			else {
				Assert.assertTrue(false);
			}
	 }
	
	public void testIncome() {
		CrimeWorld crimeWorld = new CrimeWorld(0);
		crimeWorld.start();
		Criminal[] allCitizens = crimeWorld.retrieveCitizenObjects();
		System.out.println(crimeWorld.retrieveCitizenObjects());
		
	    // assert statements
		for(int i = 0; i < allCitizens.length; i++)
			if(allCitizens[i].getWealth() >= -200) {
				Assert.assertTrue(true);
			}
			else {
				Assert.assertTrue(false);
			}
	    	
	 }
	
	public void testReduceStress() {
		CrimeWorld crimeWorld = new CrimeWorld(0);
		crimeWorld.start();
		Criminal[] allCitizens = crimeWorld.retrieveCitizenObjects();
		System.out.println(crimeWorld.retrieveCitizenObjects());
		
	    // assert statements
		for(int i = 0; i < allCitizens.length; i++) {
			double compareStress = allCitizens[i].getStress();
			allCitizens[i].reduceSetStress(10.0);
			double compareReducedStress = allCitizens[i].getStress();
			if(compareStress > compareReducedStress) {
				Assert.assertTrue(true);
			}
			else {
				Assert.assertTrue(false);
			}
		}
	}
}

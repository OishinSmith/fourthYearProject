package test.test;

import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import sim.app.crime.CrimeWorld;
import sim.app.crime.CrimeWorldWithUI;

/**
 * Unit test for simple App.
 */
public class UITests 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public UITests( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( UITests.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
	
	public void testCitizensNumber() {
		CrimeWorldWithUI tester = new CrimeWorldWithUI(); // MyClass is tested
		CrimeWorld crimeWorld = new CrimeWorld(0);
		crimeWorld.start();
		System.out.println(tester.display);
		System.out.println(tester.displayFrame);
		tester.main(null);
		System.out.println(tester);
		
		
	    // assert statements
	    //Assert.assertEquals(20, );
	 }
	
	public void testGetName() {

	 }
	
	public void testGetIncome() {

	 }
	
	public void testGetStress() {

	 }
	
}

package test.test;

import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import sim.app.crime.CrimeWorld;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
	
	public void testCitizensNumber() {
		CrimeWorld tester = new CrimeWorld(0); // MyClass is tested

	    // assert statements
	    Assert.assertEquals(20, tester.getNumCitizens());
	 }
	
	public void testGetName() {
		CrimeWorld tester = new CrimeWorld(0); // MyClass is tested
		tester.start();
	    // assert statements
		// System.out.println(tester.getName().length); prints 0 because no agents are created
		for(int i = 0; i < tester.getName().length; i++) {
			//System.out.println(tester.getName()[i]);
			if (tester.getName()[i] != "citizen 1")
			{
					
		    	Assert.assertTrue( true );
			} 
			else {
				System.out.println(false);
				Assert.assertTrue( false );
			}
		}
	 }
	
	public void testGetIncome() {
		CrimeWorld tester = new CrimeWorld(0); // MyClass is tested
		tester.start();
	    // assert statements
		// System.out.println(tester.getName().length); prints 0 because no agents are created
		for(int i = 0; i < tester.getName().length; i++) {
			//System.out.println(tester.getIncome()[i]);
			if (tester.getWealth()[i] != -100000)
			{
					
		    	Assert.assertTrue( true );
			} 
			else {
				System.out.println(false);
				Assert.assertTrue( false );
			}
		}
	 }
	
	public void testGetStress() {
		CrimeWorld tester = new CrimeWorld(0); // MyClass is tested
		tester.start();
	    // assert statements
		// System.out.println(tester.getName().length); prints 0 because no agents are created
		for(int i = 0; i < tester.getStress().length; i++) {
			//System.out.println(tester.getStress()[i]);
			if (tester.getStress()[i] != -1.00)
			{
					
		    	Assert.assertTrue( true );
			} 
			else {
				System.out.println(false);
				Assert.assertTrue( false );
			}
		}
	 }
	
}

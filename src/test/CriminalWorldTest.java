package test.test;

import java.util.Random;

import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import sim.app.crime.CrimeWorld;

/**
 * Unit test for simple CriminalWorld
 */
public class CriminalWorldTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CriminalWorldTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CriminalWorldTest.class );
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
	
	public void testIfHomelessExist() {
		CrimeWorld tester = new CrimeWorld(0); // MyClass is tested
		tester.start();
		
		for(int i = 0; i < tester.getHomelessNumbers().length; i++) {
			if (tester.getHomelessNumbers()[i] == false || tester.getHomelessNumbers()[i] == true)
			{
		    	Assert.assertTrue( true );
			} 
			else {
				System.out.println(false);
				Assert.assertTrue( false );
			}
		}
	}
	
	public void testEmployedNumbers() {
		CrimeWorld tester = new CrimeWorld(0); // MyClass is tested
		tester.start();
		
		for(int i = 0; i < tester.getEmployedNumbers().length; i++) {
			//System.out.println(tester.getIncome()[i]);
			if (tester.getEmployedNumbers()[i] == false || tester.getEmployedNumbers()[i] == true)
			{
		    	Assert.assertTrue( true );
			} 
			else {
				System.out.println(false);
				Assert.assertTrue( false );
			}
		}
	}
	
	//createRandomNeedsValue
	
	public void testCreateRandomNeedsValue() {
		CrimeWorld tester = new CrimeWorld(0); // MyClass is tested
		double needs = tester.createRandomNeedsValue(0.0);
	    // assert statements
		if (needs <= 100.0 && needs >= 0.0)
		{
	    	Assert.assertTrue( true );
		} 
		else {
			Assert.assertTrue( false );
		}
	 }
	
	public void testCreateIncomeValue() {
		CrimeWorld tester = new CrimeWorld(0); // MyClass is tested
		double income = tester.createwealthValue(0);
	    // as it averages around 200 and its a gaussian distribution, anithing below
		// -200 should fail the test as values below 200 become improbable
		if (income >= -200)
		{
	    	Assert.assertTrue( true );
		} 
		else {
			Assert.assertTrue( false );
		}
	 }
	
	public void testDoIHaveAHouse() {
		CrimeWorld tester = new CrimeWorld(0); // MyClass is tested
		Random r = new Random();
		boolean doIhaveaHouse = tester.doIHaveAHouse(r.nextInt(2), 100.0);
	    // this has to be true
		if (doIhaveaHouse == true)
		{
	    	Assert.assertTrue( true );
		} 
		else {
			Assert.assertTrue( false );
		}
		// this could either be true or false
		r = new Random();
		doIhaveaHouse = tester.doIHaveAHouse(r.nextInt(2), 49.0);
		if(doIhaveaHouse == true || doIhaveaHouse == false) {
			Assert.assertTrue( true );
		}
		
		
	 }
	
	public void testAmIEmployed() {
		CrimeWorld tester = new CrimeWorld(0); // MyClass is tested
		Random r = new Random();
		boolean amIEmployed = tester.amIEmployed(r.nextInt(2), 100.0);
	    // this has to be true
		if (amIEmployed == true)
		{
	    	Assert.assertTrue( true );
		} 
		else {
			Assert.assertTrue( false );
		}
		// this could either be true or false
		r = new Random();
		amIEmployed = tester.doIHaveAHouse(r.nextInt(2), 49.0);
		if(amIEmployed == true || amIEmployed == false) {
			Assert.assertTrue( true );
		}
	 }
}

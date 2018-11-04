package net.catae.messagehub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.security.InvalidParameterException;

import org.junit.Test;

public class ScenarioTests {
	
	@Test
	public void TestSimpleFlow() {		
		Branch b = new Branch("b", 10);
		Account a = new Account("a");
		Account aaa = new Account("aaa");

		// Test: b(10) -> a -> aaa
		SetupScenario.commonFlow(b, a, aaa);
		
		// Expected result: b -> a -> aaa(10)
		assertEquals("b", b.total(), 0);
		assertEquals("a", a.getAmount(), 0);
		assertEquals("aaa", aaa.getAmount(), 10);
	}	

	@Test
	public void TestScenarioFlow() {

		Scenario scenario = new Scenario("scene1", () -> {
			sleep(10);
		});
		
		scenario.start();
		
		sleep(5);
		assertEquals(scenario.hasFinished(), false);

		sleep(10);
		assertEquals(scenario.hasFinished(), true);
	}

	void sleep(int miliseconds) {
		try {Thread.sleep(miliseconds);} 
		catch(Exception ex) {}
	}
}

package net.catae.messagehub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.security.InvalidParameterException;

import org.junit.Test;

public class FlowTests {
	
	@Test
	public void TestFlow() {
		Branch branch = new Branch("b", 1000);
		Account a = new Account("a");
		 
		Flow flow = new Flow(branch, b ->{
			b.credit(a, 1);
			sleep(10);
			b.credit(a, 2);
		});		
		
		assertEquals("a=0", a.getAmount(), 0);
		assertEquals(flow.hasFinished(), false);
		
		sleep(5);
		assertEquals("flow hasn't started (a=0)", a.getAmount(), 0);

		flow.start();
		sleep(5);
		assertEquals("flow started (a=1)", a.getAmount(), 1);

		flow.join();
		assertEquals("flow finished (a=3)", a.getAmount(), 3);
		assertTrue(flow.hasFinished());
		assertTrue(flow.success());
	}	

	@Test
	public void TestFlowWait() {
		Branch branch = new Branch("b", 1000);
		Account a = new Account("a");

		Flow f1 = new Flow(branch, b -> { sleep(10); });
		Flow f2 = new Flow(branch, b -> { b.credit(a, 500);; });

		Flow.wait(new Flow[] { f1 });
		Flow.wait(new Flow[] { f2 });
		assertTrue(f1.hasFinished());
		assertEquals(branch.total(), 500);
	}
	
	void sleep(int miliseconds) {
		try {Thread.sleep(miliseconds);} 
		catch(Exception ex) {}
	}
}

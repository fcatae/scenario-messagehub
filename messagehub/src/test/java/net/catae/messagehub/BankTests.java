package net.catae.messagehub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Assert;
import org.junit.Test;

public class BankTests {
	
	@Test
	public void TestAccount() {
		Account a1 = new Account("a1");
		Account a2 = new Account("a2");
		
		// Ensure different accounts
		assertNotEquals(a1.getName(), a2.getName());
		
		assertEquals("start with balance zero", a1.getAmount(), 0);
		
		// Check setAmount
		a2.setAmount(100);		
		assertEquals(a2.getAmount(), 100);
		
		// Check credit
		a1.credit(50);
		assertEquals(a1.getAmount(), 50);
		
		// Check negative credit
		a2.credit(-40);
		assertEquals(a2.getAmount(), 60);
	}
}

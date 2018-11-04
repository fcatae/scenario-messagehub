package net.catae.messagehub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.security.InvalidParameterException;

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

	@Test
	public void TestBranch() {
		Account a1 = new Account("a1");
		Account a2 = new Account("a2");
		
		// Branch: b = 1000
		Branch b = new Branch("b", 1000);

		// Transaction 1: b -> 600 -> a1
		// b = 400
		// a1 = 600
		b.credit(a1, 600);
		assertEquals("transfer a1 <- 600", a1.getAmount(), 600);
		assertEquals("b has still 400", b.total(), 400);

		// Transaction 2: a1 -> 150 -> a2
		// a1 = 450
		// a2 = 150
		b.transfer(a1, a2, 150);
		assertEquals("a1 has still 450", a1.getAmount(), 450);
		assertEquals("transfer a2 <- 150", a2.getAmount(), 150);

		// Transaction 3: a2 -> 50 -> b
		// a2 = 100
		// b = 450
		b.debit(a2, 50);
		assertEquals("a2 has still 100", a2.getAmount(), 100);
		assertEquals("transfer b <- 50", b.total(), 450);
	}

	@Test
	public void TestBranchNegativeValues() {
		Account a = new Account("a");
		Branch b = new Branch("b", 1000);

		// perform negative operations
		b.credit(a, -100);
		b.debit(a, -50);
		assertEquals(a.getAmount(), -50);
		assertEquals(b.total(), 1050);

		// check negative balance
		b.credit(a, 1100);
		assertEquals(b.total(), -50);

		// perform zero operation
		b.credit(a, 0);
		b.debit(a, 0);
		assertEquals(b.total(), -50);

		// test initialization
		Branch b0 = new Branch("b1", 0);
		Branch bneg = new Branch("b2", -100);

		assertEquals(b0.total(), 0);
		assertEquals(bneg.total(), -100);
	}
	
	@Test
	public void TestBranchInvalidParameters() {
		Account a1 = new Account("a1");
		Branch b = new Branch("b", 1000);

		assertThrown(IllegalArgumentException.class, ()->{
			b.credit(null, 1);			
		});
		assertThrown(IllegalArgumentException.class, ()->{
			b.debit(null, 2);			
		});
		assertThrown(IllegalArgumentException.class, ()->{
			b.transfer(a1, null, 4);
		});
		assertThrown(IllegalArgumentException.class, ()->{
			b.transfer(null, a1, 8);
		});		
		assertThrown(IllegalArgumentException.class, ()->{
			b.transfer(a1, a1, 16);
		});

		assertEquals(a1.getAmount(), 0);
		assertEquals(b.total(), 1000);
	}

	@Test
	public void TestSimpleFlow() {		
		Branch b = new Branch("b", 10);
		Account a = new Account("a");
		Account aaa = new Account("aaa");

		// Test: b(10) -> a -> aaa
		int amount = b.total();
		
		b.credit(a, amount);
		
		for(int i=0; i<amount; i++) {
			b.transfer(a, aaa, 1);
		}
		
		// Expected result: b -> a -> aaa(10)
		assertEquals("b", b.total(), 0);
		assertEquals("a", a.getAmount(), 0);
		assertEquals("aaa", aaa.getAmount(), 10);
	}
	
	void assertThrown(Class<? extends Exception> cex, Action action) {
		try {
			action.exec();
			throw new Exception();
		}
		catch(Exception ex) {
			assertTrue(cex.isInstance(ex));
		}
	}

	interface Action {
		void exec();
	}
}

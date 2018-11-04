package net.catae.messagehub;

import java.security.InvalidParameterException;

public class Branch {
	
	private String name;
	private int amount;
	
	public Branch(String name, int amount) {		
		this.name = name;
		this.amount = amount;
	}
	
	public void credit(Account a, int value) {		
		if(a == null) 
			throw new InvalidParameterException("account == null");

		a.credit(value);
		this.amount -= value;
	}
	
	public void debit(Account a, int value) {
		if(a == null) 
			throw new InvalidParameterException("account == null");

		a.credit(-value);
		this.amount += value;
	}
	
	public void transfer(Account a, Account b, int value) {
		if(a == null || b == null) 
			throw new InvalidParameterException("account == null");

		if(a == b) 
			throw new InvalidParameterException("account1 == account2");

		a.credit(-value);
		b.credit(+value);
	}
	
	public String getName() {
		return this.name;
	}
	
	public int total() {	
		return this.amount;
	}
}

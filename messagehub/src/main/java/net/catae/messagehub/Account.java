package net.catae.messagehub;

public class Account {
	public Account(String name) {		
	}
	
	public static Account create(String name) {
		return new Account(name);
	}
}

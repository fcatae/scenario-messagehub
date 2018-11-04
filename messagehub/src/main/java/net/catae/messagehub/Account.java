package net.catae.messagehub;

public class Account {
	private String name;
	private int amount;
	
	public Account(String name) {
		this.name = name;
		this.amount = 0;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}
	
	public void credit(int value) {
		this.amount += value;
	}
}

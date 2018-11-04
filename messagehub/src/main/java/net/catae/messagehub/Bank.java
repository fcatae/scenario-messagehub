package net.catae.messagehub;

public class Bank {
	public Flow flow(String name, int amount, Flow.Action f) {
		Branch branch = new Branch(name, amount);

		return new Flow(branch, f);
	}

	public Account account(String name) {
		return new Account(name);
	}
		
	public void wait(Flow[] flows) {		
	}
	
	public void start(Flow flow) {		
	}
}

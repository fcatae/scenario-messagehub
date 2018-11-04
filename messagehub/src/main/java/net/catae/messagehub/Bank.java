package net.catae.messagehub;

public class Bank {
	public Flow flow(String name, int amount, FlowExecution f) {
		return new Flow();
	}

	public Account account(String name) {
		return new Account(name);
	}
		
	public void wait(Flow[] flows) {		
	}
	
	public void start(Flow flow) {		
	}
}

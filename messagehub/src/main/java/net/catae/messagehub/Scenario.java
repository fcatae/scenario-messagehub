package net.catae.messagehub;

public class Scenario {	

	public static Scenario create(String name, BankAction action) {
		return new Scenario();	
	}
	
	public void start() {		
	}
	
	public Object getStats() {
		return null;
	}
	
	public boolean hasFinished() {
		return false;
	}
	
	public String getId() {
		return "0001";
	}
	
	public interface BankAction {
		void action();
	}
}

package net.catae.messagehub;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SetupScenario implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("SetupScenario");
		
		Bank bank = new Bank();
		Scenario scenario = SetupScenario.create(bank);
		
		scenario.start();
		while(!scenario.hasFinished()) {
			Thread.sleep(1000);
			System.out.println("running scenario " + scenario.getId());
		}		
	}
	
	public static Scenario create(Bank bank) {

		// small accounts a1, a2, a3
		Account a1 = bank.account("a1");
		Account a2 = bank.account("a2");
		Account a3 = bank.account("a3");
		
		// major account AAA
		Account aaa = bank.account("AAA");

		// transfer 2000 from small account to major account: total = 6000
		Flow f1 = bank.flow("F1", 2000, b -> commonFlow(b, a1, aaa));
		Flow f2 = bank.flow("F2", 2000, b -> commonFlow(b, a2, aaa));
		Flow f3 = bank.flow("F3", 2000, b -> commonFlow(b, a3, aaa));
		
		// transfer 6000 back to the bank
		Flow ff = bank.flow("FF", -6000, b -> {		
			b.debit(aaa, 6000);
		});	
		
		// setup scenario
		return Scenario.create("scenario1", () -> {
			bank.wait(new Flow[] {f1, f2, f3});
			bank.wait(new Flow[] {ff});
		});		
	}
	
	static void commonFlow(Branch b, Account a, Account aaa) {
		
		int amount = b.total();
		
		b.credit(a, amount);
		
		for(int i=0; i<amount; i++) {
			b.transfer(a, aaa, 1);
		}
		
	}
}

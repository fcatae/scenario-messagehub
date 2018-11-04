package net.catae.messagehub;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HubClient implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		System.out.println("hello world");		
	}

}

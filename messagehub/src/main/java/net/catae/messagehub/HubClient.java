package net.catae.messagehub;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;

//@Component
public class HubClient {
	//	implements CommandLineRunner
	String TOPIC = "messageh";

	//@Override
	public void run(String... args) throws Exception {
		runProducer();
		runConsumer();
	}

	public void runConsumer() throws Exception {
		System.out.println("hello world");		

		Properties config = new Properties();		
		config.put("bootstrap.servers", "<>.servicebus.windows.net:9093");
		config.put("client.id", "hubclient");
		config.put("group.id", "cli");		
		config.put("sasl.mechanism","PLAIN");
		config.put("security.protocol","SASL_SSL");
		config.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\"\";");
		config.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
		config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("auto.offset.reset", "earliest");
		
		KafkaConsumer<Long,String> consumer = new KafkaConsumer<>(config);
		
		consumer.subscribe(Collections.singletonList(TOPIC));
		
		//consumer.assign(Collections.singletonList(new TopicPartition(TOPIC, 5)));

		consumer.seekToBeginning(consumer.assignment());

		try {
			while(true) {
				ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofSeconds(5));

				for(ConsumerRecord<Long, String> cr : consumerRecords) {
					System.out.printf("(%d): %s\n", cr.partition(), cr.value());
				}
			}
		}
		finally {
			consumer.close();	
		}
	}

	public void runProducer() throws Exception {
		System.out.println("hello world");		

		Properties config = new Properties();		
		config.put("bootstrap.servers", "<>.servicebus.windows.net:9093");
		config.put("client.id", "hubclient");
		config.put("sasl.mechanism","PLAIN");
		config.put("security.protocol","SASL_SSL");
		config.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\"\";");
		config.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
 		config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<Long,String> producer = new KafkaProducer<>(config);
		
		for(int j = 0; j < 10; j++) {
			for(int i = 0; i < 1000; i++) {
				//producer.send(new ProducerRecord<String, String>("my-topic", Integer.toString(i), Integer.toString(i)));
				producer.send(new ProducerRecord<Long, String>(TOPIC, (long)i, "test " + String.valueOf(i) ));					
			}

			System.out.println(String.format("Messages: %d", j));
			producer.flush();
			System.out.println("Flush");
		}

		producer.close();	
	}
}

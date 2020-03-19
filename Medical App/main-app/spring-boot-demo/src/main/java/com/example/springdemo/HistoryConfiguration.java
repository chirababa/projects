package com.example.springdemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class HistoryConfiguration {

	@Bean
	public Jaxb2Marshaller marshaller()
	{
		Jaxb2Marshaller marshaller =  new Jaxb2Marshaller();
		marshaller.setContextPath("soapServices");
		return marshaller;
	}

	@Bean
	public HistoryClient historyClient1(Jaxb2Marshaller jaxb2Marshaller)
	{
		HistoryClient client =  new HistoryClient();
		client.setDefaultUri("http://localhost:9093/soap");
		client.setMarshaller(jaxb2Marshaller);
		client.setUnmarshaller(jaxb2Marshaller);
		return client;
	}
}

package com.example.springdemo;

import com.example.springdemo.controller.UserController;
import com.example.springdemo.dto.builders.MonitorActivityDTO;
import com.example.springdemo.entities.MonitorActivity;
import com.example.springdemo.services.MonitorActivityService;
import com.example.springdemo.services.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

@Component
@Order(value = 1)
public class Receiver implements CommandLineRunner {

	@Autowired
	private MonitorActivityService monitorActivityService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private UserController userController;

	private final static String QUEUE_NAME = "activities";

	@Override
	public void run(String... args) throws Exception {
		final SimpleDateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ObjectMapper mapper = new ObjectMapper();
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + message);
			MonitorActivityDTO monitorActivityDTO = mapper.readValue(message,MonitorActivityDTO.class);
			MonitorActivity monitorActivity = new MonitorActivity();
			monitorActivity.setPatient(patientService.findPatientById(Integer.parseInt(monitorActivityDTO.getIdPatient())));
			try {
				monitorActivity.setStartTime(formatter.parse(monitorActivityDTO.getStartTime()));
				monitorActivity.setEndTime(formatter.parse(monitorActivityDTO.getEndTime()));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			monitorActivity.setActivityName(monitorActivityDTO.getName());
			long diffinMillies = Math.abs(monitorActivity.getEndTime().getTime() - monitorActivity.getStartTime().getTime());
			long diff = TimeUnit.HOURS.convert(diffinMillies,TimeUnit.MILLISECONDS);
			if(monitorActivity.getActivityName().equalsIgnoreCase("Sleeping") && diff >= 12) {
				monitorActivity.setAnomalous(true);
				try {
					userController.alerting(monitorActivity.getPatient().getUser().getUsername() + " has been sleeping for more than 12 hours",monitorActivity.getPatient().getCareGiver().getIdUser());
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(monitorActivity.getAnomalous());
			}
			else if((monitorActivity.getActivityName().equalsIgnoreCase( "Toileting") || monitorActivity.getActivityName().equalsIgnoreCase("Showering")) && diff >= 1) {
				monitorActivity.setAnomalous(true);
				try {
					userController.alerting("The period spent by " + monitorActivity.getPatient().getUser().getUsername() + " in bathroom is longer than 1 hour",monitorActivity.getPatient().getCareGiver().getIdUser());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(monitorActivity.getActivityName().equalsIgnoreCase("Leaving") && diff >= 12) {
				monitorActivity.setAnomalous(true);
				try {
					userController.alerting(monitorActivity.getPatient().getUser().getUsername() + "  has been away from house for more than 12 hours",monitorActivity.getPatient().getCareGiver().getIdUser());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				monitorActivity.setAnomalous(false);
			}
			monitorActivityService.insert(monitorActivity);
		};
		channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
	}

}
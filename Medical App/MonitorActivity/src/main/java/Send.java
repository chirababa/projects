import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Send {
    private final static String QUEUE_NAME = "activities";
    public static void main(String[] argv) throws Exception {
        BufferedReader reader;
        ObjectMapper mapper = new ObjectMapper();
        MonitorActivity monitorActivity =  new MonitorActivity();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            reader = new BufferedReader(new FileReader("src/main/java/activities.txt"));
            String line = reader.readLine();
            while(line!=null)
            {
                String[] tokens = line.split("\\t+");
                monitorActivity.setIdPatient(tokens[0]);
                monitorActivity.setStartTime(tokens[1]);
                monitorActivity.setEndTime(tokens[2]);
                monitorActivity.setName(tokens[3]);
                String jsonString = mapper.writeValueAsString(monitorActivity);
                channel.basicPublish("", QUEUE_NAME, null, jsonString.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + jsonString + "'");
                TimeUnit.SECONDS.sleep(1);
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
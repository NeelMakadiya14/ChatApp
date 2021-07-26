package ChatAppFinal;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;

public class Helper {

    public String getUserInput(String prompt){
        String inputLine = null;
        if(prompt!=null){
            System.out.print(prompt);
        }

        try {
            BufferedReader is = new BufferedReader( new InputStreamReader(System.in));
            inputLine = is.readLine();
            if (inputLine.length() == 0 ) {
                return null;
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return inputLine;
    }

    public void createTopic(String name, int numPartition, int replicationFactor){

        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(AdminClientConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG,"100000");
        properties.setProperty(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG,"5000");

        AdminClient adminClient = AdminClient.create(properties);

        DescribeTopicsResult topic = adminClient.describeTopics(Arrays.asList(name));
        try{
            TopicDescription topicDescription = topic.values().get(name).get();
//            System.out.println("Description of demo topic:" + topicDescription);
//            System.out.println("Topic already created..");
        }
        catch (Exception e) {

            if (!(e.getCause() instanceof UnknownTopicOrPartitionException)) {
                e.printStackTrace();
            }
            // if we are here, topic doesn't exist
//            System.out.println("Topic " + name + " does not exist. Going to create it now");

            NewTopic newTopic = new NewTopic(name, numPartition, (short) replicationFactor);

            CreateTopicsResult topicsResult =  adminClient.createTopics(Arrays.asList(newTopic));
        }
        adminClient.close();
    }

}

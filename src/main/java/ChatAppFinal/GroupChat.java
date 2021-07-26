package ChatAppFinal;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static ChatApp.User.createTopic;

public class GroupChat extends ChatApplication{

    private String groupName;

    public GroupChat(){
        groupName = getHelper().getUserInput("Enter the Group Name : ");
    }


    @Override
    public void send() {
        String message = getHelper().getUserInput(null);

        getHelper().createTopic(groupName,1,1);

        Message msg = new Message(getUserName(),groupName,message);

        Producer producer = new Producer();
        producer.Produce(groupName,getUserName(),msg);
    }

    @Override
    public void receive() {
        ConsumerWithThread.consume(groupName,1);
    }

    public static void main(String[] args) {
        ChatApplication application = new GroupChat();
        application.setUp();
    }
}

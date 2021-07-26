package ChatAppFinal;

import ChatAppFinal.Message;
import ChatAppFinal.Producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OneToOne extends ChatApplication{

    @Override
    public void send() {

        String receiver = getHelper().getUserInput(null);

        String msg = getHelper().getUserInput("Enter The Message : ");


        Message message = new Message(getUserName(),receiver,msg);

        getHelper().createTopic(getUserName(),10,1);
        getHelper().createTopic(receiver,10,1);



        Producer producer = new Producer();

        producer.Produce(getUserName(),receiver,message);
        producer.Produce(receiver,getUserName(),message);

    }

    @Override
    public void receive() {
        ConsumerWithThread.consume(getUserName(),10);
    }

    public static void main(String[] args) {
        ChatApplication application = new OneToOne();
        application.setUp();
    }
}

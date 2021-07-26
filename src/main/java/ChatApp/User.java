package ChatApp;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import static ChatApp.Consumer.consume;

public class User {
    private String username;
    private Scanner sc;
    private PrintStream console;

    public User(String name, Scanner scanner){
        username = name;
        sc=scanner;
        console = System.out;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Scanner getSc() {
        return sc;
    }

    public void setSc(Scanner sc) {
        this.sc = sc;
    }

    public void run(){
//        Semaphore sem = new Semaphore(1);
        ListenerRunnable listenerRunnable = new ListenerRunnable(username);
        Thread listener = new Thread(listenerRunnable);

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    send();
                }

            }
        });

        listener.start();
        sender.start();


//        while(true){
//            try {
//                sem.acquire();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            send();
//            sem.release();
//            Thread.yield();
//        }
    }

    public void send(){
        System.setOut(console);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.print("Enter the receiver's name : ");
        String receiver = null;
        try {
            receiver = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Enter the message : ");
        String msg = null;
        try {
            msg = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message message = new Message(username,receiver,msg);
//        System.out.println(username);
//        System.out.println(receiver);
//        System.out.println(msg);
        createTopic(username,10);
        createTopic(receiver,10);

        Producer producer = new Producer();
        producer.produce(message);
    }



    public static void createTopic(String name, int numPartition){
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

            NewTopic newTopic = new NewTopic(name, numPartition, (short) 1);

            CreateTopicsResult topicsResult =  adminClient.createTopics(Arrays.asList(newTopic));
        }
        adminClient.close();
    }
}

class ListenerRunnable implements Runnable{

    String username;
//    Semaphore sem;

    public ListenerRunnable(String name){
        username = name;
//        sem = semaphore;
    }

    @Override
    public void run() {
//        while (true){
//            try {
//                sem.acquire();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            consume(username);
//            sem.release();
//            Thread.yield();
//        }

    }
}



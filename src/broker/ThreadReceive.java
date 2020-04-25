package broker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class ThreadReceive implements Runnable {
    private final static String QUEUE_NAME = "queries";
    private final static String HOST_NAME = "172.16.80.182";
    private final static String USER_NAME = "msg";
    private final static String PASSWORD = "msg123";
    private Channel channel;
    private Connection connection;
    private final ConnectionFactory factory = new ConnectionFactory();

    private Connection connect() {
        Connection connection = null;
        try {
            connection = factory.newConnection();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void run() {
        try {
//          factory.setHost(HOST_NAME);
//          factory.setUsername(USER_NAME);
//          factory.setPassword(PASSWORD);
            factory.setHost("localhost");
            connection = connect();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(500);
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                channel.queueDelete(QUEUE_NAME);
                channel.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


package broker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import database.MasterConnect;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class ThreadReceive implements Runnable {
    private final static String QUEUE_NAME = "Douda";
    private final static String HOST_NAME = "196.234.243.24";
    private final static String USER_NAME = "sinda";
    private final static String PASSWORD = "sinda123";
    private Channel channel;
    private Connection connection;
    private final ConnectionFactory factory = new ConnectionFactory();
    static int i = 0;
    private MasterConnect conn ;

    private Connection connect() {
        // Au cas ou on va essayer a distance on decommente le bloc ci dessous

        // factory.setHost(HOST_NAME);
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        this.conn =  MasterConnect.getDbCon("branchMaster") ;
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void run() {
        try {
            connection = connect();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" Waiting for messages . To exit press Ctrl+C");
            while (true) {
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    try {
                        conn.executeQuery(message);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }


                };
                channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
                });
                //receiverController.receiveMessage(user, message);
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(500);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                channel.queueDelete(QUEUE_NAME);
                channel.close();
                connection.close();
                System.out.println("Channel closed");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("thread exists!");
        }
    }

}

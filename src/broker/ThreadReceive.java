package broker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import database.MasterConnect;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/*
* Thread responsable de la réception des requetes et de les exécuter chez la branche Master
* */


public class ThreadReceive implements Runnable {
    private final static String QUEUE_NAME = "queries";
    private final static String HOST_NAME = "196.234.243.24";
    private final static String USER_NAME = "bd";
    private final static String PASSWORD = "bd123";
    private Channel channel;
    private Connection connection;
    private final ConnectionFactory factory = new ConnectionFactory();
    private MasterConnect conn ;

    private Connection connect() {
//        factory.setHost(HOST_NAME);
//        factory.setUsername(USER_NAME);
//        factory.setPassword(PASSWORD);
        factory.setHost("localhost");
        this.conn =  MasterConnect.getDbCon("branch_master") ;
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
            while (true) {
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    try {
                        //Exécution de la requete reçue
                        conn.executeQuery(message);
                    }
                    catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                };
                channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
                });
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

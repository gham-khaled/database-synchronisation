package broker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/*
* Thread responsable de la transmission des requetes qu'exécutent les branches offices à la branche Master
* et qui prend en paramètre la requete à exécuter
*  */


public class ThreadSend implements Runnable{
    private final static String QUEUE_NAME = "queries";
    private final static String HOST_NAME = "196.234.243.24";
    private final static String USER_NAME = "bd";
    private final static String PASSWORD = "bd123";
    private String query = "TEST";
    private Channel channel;
    private Connection connection;
    private final ConnectionFactory factory = new ConnectionFactory();

    public ThreadSend (String query) {
        this.query = query;
    }

    private Connection connect(){
//        factory.setUsername(USER_NAME);
//        factory.setPassword(PASSWORD);
//        factory.setHost(HOST_NAME);
        factory.setHost("localhost");
        connection = null;
        try{
            connection = factory.newConnection();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }
    @Override
    public void run() {
        try {
            connection = connect();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("",QUEUE_NAME,null, query.getBytes());
            System.out.println(query);
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e){
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

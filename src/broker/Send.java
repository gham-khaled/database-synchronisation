package broker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

    private final static String QUEUE_NAME = "queries";
    private final static String HOST_NAME = "196.234.243.24";
    private final static String USER_NAME = "sinda";
    private final static String PASSWORD = "sinda123";
    private String query;
    private Channel channel;
    private Connection connection;
    private final ConnectionFactory factory = new ConnectionFactory();



    private Connection connect(){
        Connection connection = null;
        try{
            connection = factory.newConnection();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }


    public void sendMessage(String query) {
        try {
           // factory.setHost(HOST_NAME);
            factory.setUsername(USER_NAME);
            factory.setPassword(PASSWORD);
            factory.setHost("localhost");
            connection = connect();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("",QUEUE_NAME,null,query.getBytes());

        }  catch (Exception e){
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

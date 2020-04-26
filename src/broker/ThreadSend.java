package broker;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ThreadSend implements Runnable{
    private final static String QUEUE_NAME = "Douda";
    private final static String HOST_NAME = "196.234.243.24";
    private final static String USER_NAME = "sinda";
    private final static String PASSWORD = "sinda123";
    private String msg = "TEST";
    private Channel channel;
    private Connection connection;
    private final ConnectionFactory factory = new ConnectionFactory();

    public ThreadSend (String msg) { this.msg = msg ;}

    private Connection connect(){
     //En cas de connection à distance:
        // factory.setHost(HOST_NAME);
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
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
            AMQP.BasicProperties properties = new AMQP.BasicProperties() ;
            properties = properties.builder().userId("sinda").build() ;
            channel.basicPublish("",QUEUE_NAME,properties,msg.getBytes());
            System.out.println(msg);
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
                System.out.println("Channel closed");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("thread exists!");
        }

    }
}

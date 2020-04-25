import broker.ThreadReceive;

import java.sql.SQLException;

public class TestMain {
    public static void main(String args []) throws SQLException {
        ThreadReceive threadReceive = new ThreadReceive() ;
        Thread thread = new Thread(threadReceive);
        thread.start();

    }
}

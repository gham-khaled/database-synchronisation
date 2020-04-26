import broker.ThreadReceive;

import java.sql.SQLException;

public class TestMain {
    public static void main(String args []) throws SQLException {
      Thread thread = new Thread(new ThreadReceive());
      thread.start();



    }
}

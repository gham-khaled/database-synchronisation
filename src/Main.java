import database.MysqlConnect;

import java.sql.SQLException;

public class Main {
    public static void main(String args []) throws SQLException {
//        ThreadReceive threadReceive = new ThreadReceive() ;
//        Thread thread = new Thread(threadReceive);
//        thread.start();
        MysqlConnect conn =  MysqlConnect.getDbCon("branchOffice1") ;
        System.out.println("khaled");
        conn.query("SELECT * FROM Users");
        conn.insertQuery("Ghada",17,"Aouina");
        conn.updateQuery("DELETE FROM Users WHERE age=17");
        conn.query("SELECT * FROM Users");

    }
}

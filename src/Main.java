import database.MysqlConnect;

import java.sql.SQLException;

public class Main {
    public static void main(String args []) throws SQLException {
        MysqlConnect conn =  MysqlConnect.getDbCon() ;
        System.out.println(conn.insertQuery("khaled",21,"ariana"));
        System.out.println("test main");
    }
}

import broker.ThreadReceive;
import database.MysqlConnect1;
import database.MysqlConnect2;

import java.sql.SQLException;

public class Main {
    public static void main(String args []) throws SQLException, InterruptedException {
        MysqlConnect1 conn1 =  MysqlConnect1.getDbCon("branch_office_1") ;
        conn1.insertQuery("ghada",17,"aouina");
        conn1.upadateByNameQuery("ghada","Sinda");
        System.out.println(conn1.sendUpdatedUsers());
    }
}

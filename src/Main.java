import broker.ThreadReceive;
import database.MysqlConnect1;
import database.MysqlConnect2;

import java.sql.SQLException;

public class Main {
    public static void main(String args []) throws SQLException, InterruptedException {
        Thread thread = new Thread(new ThreadReceive());
        thread.start();
        MysqlConnect1 conn1 =  MysqlConnect1.getDbCon("branchOffice1") ;
        MysqlConnect2 conn2 =  MysqlConnect2.getDbCon("branchOffice2") ;
        conn1.insertQuery("Ghada",17,"Aouina");
        conn1.insertQuery("Sinda",20,"Aouina");
        conn2.insertQuery("Khaled",21,"Ariana");


    }
}

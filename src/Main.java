import database.MysqlConnect;

import java.sql.SQLException;

public class Main {
    public static void main(String args []) throws SQLException {
        MysqlConnect conn =  MysqlConnect.getDbCon("branche_office_1") ;
        conn.query("SELECT * FROM Users");
        conn.insertQuery("Ghada",17,"Aouina");
        conn.updateQuery("DELETE FROM Users WHERE age=17");
        conn.query("SELECT * FROM Users");
    }
}

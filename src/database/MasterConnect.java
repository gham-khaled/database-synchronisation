package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class MasterConnect {
    private Connection connection;
    public static MasterConnect db;

    private MasterConnect(String dbName) {
        String url = "jdbc:mysql://localhost:3306/"+dbName+"?useSSL=false";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "";
        try {
            Class.forName(driver).newInstance();
            this.connection = (Connection) DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }

    public static synchronized MasterConnect getDbCon(String dbName) {
        if (db == null) {
            db = new MasterConnect(dbName);
        }
        return db;
    }
    public void executeQuery (String query) throws SQLException {
        Statement statement = db.getConnection().createStatement();
        boolean result = statement.execute(query);
    }
}

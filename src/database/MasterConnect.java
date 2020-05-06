package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/*
 * Cette classe sert à la connection à la base de données de la branche Master
 * Le design pattern Singleton est implémenté.
 * */


public final class MasterConnect {
    private Connection connection;
    public static MasterConnect masterBranch;

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
        if (masterBranch == null) {
            masterBranch = new MasterConnect(dbName);
        }
        return masterBranch;
    }

    //Pour tout type de requetes
    public void executeQuery (String query) throws SQLException {
        Statement masterStatement = masterBranch.getConnection().createStatement();
        masterStatement.execute(query);
    }
}

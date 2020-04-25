package database;

import java.sql.*;

/**
 * @author Ramindu
 * @desc A singleton database access class for MySQL
 */

public final class MysqlConnect {

    private Connection connection;
    public static MysqlConnect db;

    private MysqlConnect(String dbName) {
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

    public static synchronized MysqlConnect getDbCon(String dbName) {
        if (db == null) {
            db = new MysqlConnect(dbName);
        }
        return db;
    }

    //For delete/update
    public void updateQuery(String query) throws SQLException {
        Statement statement = db.getConnection().createStatement();
        int result = statement.executeUpdate(query);
        System.out.println(result);
    }

    //For select
    public void query(String query) throws SQLException{
        Statement statement = db.getConnection().createStatement();
        ResultSet result = statement.executeQuery(query);
        while (result.next()){
            System.out.println(result.getString(1)+result.getString(2)+result.getString(3));
        }
    }

    public void insertQuery(String name, int age, String address) throws SQLException {
        String query = "INSERT INTO Users (name,address,age) " + " values (?, ?, ?)";
        PreparedStatement statement = db.getConnection().prepareStatement(query);
        statement.setString(1, name);
        statement.setInt(3, age);
        statement.setString(2, address);
        System.out.println(statement);
<<<<<<< Updated upstream
        statement.execute();
=======
        boolean result = statement.execute();
        return result;
>>>>>>> Stashed changes
    }

}
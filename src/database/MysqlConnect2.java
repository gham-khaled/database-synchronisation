package database;

import broker.ThreadSend;

import java.sql.*;

/**
 * @author Ramindu
 * @desc A singleton database access class for MySQL
 */

public final class MysqlConnect2 {

    private Connection connection;
    public static MysqlConnect2 db;

    private MysqlConnect2(String dbName) {
        String url = "jdbc:mysql://localhost:3306/"+dbName  ;
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

    public static synchronized MysqlConnect2 getDbCon(String dbName) {
        if (db == null) {
            db = new MysqlConnect2(dbName);
        }
        return db;
    }

    //For delete/update
    public void updateQuery(String query) throws SQLException {
        Statement statement = db.getConnection().createStatement();
        new Thread(new ThreadSend(query)).start();
        int result = statement.executeUpdate(query);
        System.out.println(result);
    }

    //For select
    public void query(String query) throws SQLException, InterruptedException {
        Statement statement = db.getConnection().createStatement();
        ResultSet result = statement.executeQuery(query);
        new Thread(new ThreadSend(query)).start();


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

        String finalStatement = statement.toString().split(":")[1];
        boolean result = statement.execute();
        new Thread(new ThreadSend(finalStatement)).start();


    }
    public void executeQuery (String query) throws SQLException {
        Statement statement = db.getConnection().createStatement();
        boolean result = statement.execute(query);
    }

}
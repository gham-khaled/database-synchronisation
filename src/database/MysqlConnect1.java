package database;

import broker.ThreadSend;
import com.google.gson.Gson;
import entities.User;

import java.sql.*;
import java.util.ArrayList;

/*
* Cette classe sert à la connection à la base de données de la branche office 1
* Le design pattern Singleton est implémenté.
* */


public final class MysqlConnect1 {

    private Connection connection;
    public static MysqlConnect1 db;
    private ArrayList<User> insertedUsers = new ArrayList<>();


    private MysqlConnect1(String dbName) {
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

    public static synchronized MysqlConnect1 getDbCon(String dbName) {
        if (db == null) {
            db = new MysqlConnect1(dbName);
        }
        return db;
    }

    //Pour les requetes DELETE/UPDATE
    public void updateQuery(String query) throws SQLException {
        Statement statement = db.getConnection().createStatement();
        int result = statement.executeUpdate(query);
    }

    //Retourne ID du dernier element inseré pendant la session
    public Long selectLastInsertedIdQuery() throws SQLException, InterruptedException {
        Statement statement = db.getConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT LAST_INSERT_ID()");
        result.next();
        return Long.valueOf(result.getString(1));
    }

    //Pour les requetes INSERT
    public void insertQuery(String name, int age, String address) throws SQLException, InterruptedException {
        String query = "INSERT INTO Users (name,address,age) " + " values (?, ?, ?)";
        PreparedStatement statement = db.getConnection().prepareStatement(query);
        statement.setString(1, name);
        statement.setString(2, address);
        statement.setInt(3, age);
        boolean result = statement.execute();

        User user = new User();
        user.setId(selectLastInsertedIdQuery());
        user.setBranch("bo1");
        user.setName(name);
        user.setAge(age);
        user.setAddress(address);
        insertedUsers.add(user);
    }

    //Send Inserted users in JSON
    public String sendInsertedUsers(){
        Gson gson = new Gson();
        if(insertedUsers != null) {
            String usersList = gson.toJson(insertedUsers);
            return usersList;
        }
        return null;
    }
}
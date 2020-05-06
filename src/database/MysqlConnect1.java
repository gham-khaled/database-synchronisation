package database;

import broker.ThreadSend;
import com.google.gson.Gson;
import entities.User;

import javax.jws.soap.SOAPBinding;
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
    private ArrayList<User> updatedUsers = new ArrayList<>();
    private ArrayList<String> deletedUsers = new ArrayList<>();
    private final Gson gson = new Gson();


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

    public User getUserByName (String name) throws SQLException {
        String selectQuery = "SELECT * FROM Users WHERE name=?";
        PreparedStatement selectStatement = db.getConnection().prepareStatement(selectQuery);
        selectStatement.setString(1,name);
        ResultSet result =  selectStatement.executeQuery();
        result.next(); //On sélectionne la première ligne reçue
        User user = new User();
        user.setId(Long.valueOf(result.getString(1)));
        user.setName(result.getString(2));
        user.setAddress(result.getString(3));
        user.setAge(Integer.valueOf(result.getString(4)));
        return user;
    }

    /*
    * Suppression : on prend le cas de suppression par prenom seulement
    * */

    public void deleteFirstByNameQuery(String name) throws SQLException {
        String selectQuery = "SELECT * FROM Users WHERE name=?";
        PreparedStatement selectStatement = db.getConnection().prepareStatement(selectQuery);
        selectStatement.setString(1,name);
        ResultSet result =  selectStatement.executeQuery();
        result.next(); //On sélectionne la première ligne reçue
        String id = result.getString(1);
        String deleteQuery = "DELETE FROM Users WHERE name='"+name+"';";
        Statement deleteStatement = db.getConnection().createStatement();
        deleteStatement.execute(deleteQuery);
        String finalEntry = "bo1_"+id;
        deletedUsers.add(finalEntry);
    }

    public String sendDeletedUsers(){
        if(deletedUsers != null) {
            String deletedUsersList = gson.toJson(deletedUsers);
            return deletedUsersList;
        }
        return null;
    }

    /*
    * Insertion
    * */

    //Retourne ID du dernier element inseré pendant la session
    public Long selectLastInsertedIdQuery() throws SQLException, InterruptedException {
        Statement statement = db.getConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT LAST_INSERT_ID()");
        result.next();
        return Long.valueOf(result.getString(1));
    }

    public void insertQuery(String name, int age, String address) throws SQLException, InterruptedException {
        String query = "INSERT INTO Users (name,address,age,synchro) " + " values (?, ?, ?,?)";
        PreparedStatement statement = db.getConnection().prepareStatement(query);
        statement.setString(1, name);
        statement.setString(2, address);
        statement.setInt(3, age);
        statement.setBoolean(4, false);
        boolean result = statement.execute();

        User user = new User();
        user.setBranch_id("bo1_"+selectLastInsertedIdQuery());
        user.setName(name);
        user.setAge(age);
        user.setAddress(address);
        insertedUsers.add(user);
    }

    public String sendInsertedUsers(){
        if(insertedUsers != null) {
            String usersList = gson.toJson(insertedUsers);
            return usersList;
        }
        return null;
    }

    /*
    * Update
    * */

    public void upadateByNameQuery(String oldName,String newName) throws SQLException {
        User user = getUserByName(oldName);
        String updateQuery = "UPDATE Users SET name='"+newName+"' WHERE name='"+oldName+"'";
        Statement updateStatement = db.getConnection().createStatement();
        updateStatement.execute(updateQuery);
        user.setName(newName);
        user.setBranch_id("bo1_"+user.getId());
        updatedUsers.add(user);
    }

    public String sendUpdatedUsers(){
        if(updatedUsers != null) {
            String updatedUsersList = gson.toJson(updatedUsers);
            return updatedUsersList;
        }
        return null;
    }
}
package org.ximure.simpleauth.auth;

import java.sql.*;
import java.util.UUID;

import static org.ximure.simpleauth.SimpleAuth.PASSWORDS_DATABASE;

public class SQLiteManager implements Database {
    private static Connection connection;

    /**
     * Inserts player uuid, password and password reminder into the database
     * @return true if data has been written, false if something will go wrong
     */
    @Override
    public Boolean insertPassword(UUID playerUUID, String password, String passwordReminder) {
        final String query = "INSERT INTO passwords(uuid, password, password_reminder) VALUES(?, ?, ?)";
        Connection connection = this.connectToDatabase();
        assert connection != null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, playerUUID.toString());
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, passwordReminder);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Changes password associated with player uuid
     * @param playerUUID  this will be used to find password in the database
     * @param newPassword oldPassword will be changed to this one
     * @return true if data has been written, false if something will go wrong
     */
    @Override
    public Boolean changePassword(UUID playerUUID, String newPassword) {
        try {
            String query = "UPDATE passwords SET password = ? WHERE uuid = ?";
            Connection connection = this.connectToDatabase();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, playerUUID.toString());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Changes password reminder associated with uuid
     * @param playerUUID          this will be used to find password reminder in the database
     * @param newPasswordReminder old password reminder will be changed to this one
     * @return true if password reminder has been changed, false if something will go wrong
     */
    @Override
    public Boolean changePasswordReminder(UUID playerUUID, String newPasswordReminder) {
        try {
            String query = "UPDATE passwords SET password_reminder = ? WHERE uuid = ?";
            Connection connection = this.connectToDatabase();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newPasswordReminder);
            preparedStatement.setString(2, playerUUID.toString());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if player's UUID exists in database. If not - he's not registere
     * @param playerUUID this one will be used to find player in the database
     * @return true if player uuid is in the database, false otherwise
     */
    @Override
    public Boolean isRegistered(UUID playerUUID) throws NullPointerException {
        final String query = "SELECT uuid FROM passwords WHERE uuid = '" + playerUUID + "'";
        Connection connection = this.connectToDatabase();
        assert connection != null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getString("uuid").equals(playerUUID.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a password reminder string which player entered while registering. Null if something will go wrong
     * @param playerUUID this will be used to get reminder in the database
     * @return password reminder string. Null if something will go wrong
     */
    @Override
    public String getPasswordReminder(UUID playerUUID) {
        final String query = "SELECT password_reminder FROM passwords WHERE uuid = '" + playerUUID + "'";
        Connection connection = this.connectToDatabase();
        assert connection != null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getString("password_reminder");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Compares player entered password with one's in the database
     * @param playerUUID associated to get old password inside method
     * @param password   which player entered in in-game chat via /register command
     * @return true if password player entered is the same in the database, otherwise false
     */
    @Override
    public Boolean isCorrectPassword(UUID playerUUID, String password) throws NullPointerException {
        final String query = "SELECT password FROM passwords WHERE uuid = '" + playerUUID + "'";
        Connection connection = this.connectToDatabase();
        assert connection != null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getString("password").equals(password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method simply creates a database if it not exists
     * @return Boolean true if database has been created. False otherwise
     */
    @Override
    public Boolean createDatabase() {
        final String url = "jdbc:sqlite:" + PASSWORDS_DATABASE;
        try {
            Connection connection = DriverManager.getConnection(url);
            if (connection != null) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method creates a passwords table to store players passwords if it not already exists
     * @return Boolean true if table has been created. False otherwise
     */
    @Override
    public Boolean createPasswordsTable() {
        String url = "jdbc:sqlite:" + PASSWORDS_DATABASE;
        String query = "CREATE TABLE IF NOT EXISTS passwords (\n"
                + "	uuid TEXT, \n"
                + "	password TEXT,\n"
                + "	password_reminder TEXT\n"
                + ");";
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method allows connecting to database
     * @return Connection object
     */
    private Connection connectToDatabase() {
        String url = "jdbc:sqlite:" + PASSWORDS_DATABASE;
        try {
            if (connection != null) {
                return connection;
            }
            connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

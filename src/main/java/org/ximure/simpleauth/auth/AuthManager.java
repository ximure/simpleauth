package org.ximure.simpleauth.auth;

import java.sql.*;
import java.util.UUID;

import static org.ximure.simpleauth.SimpleAuth.PASSWORDS_DATABASE;

public class AuthManager extends PlayerStatus {
    private static Connection connection;

    /**
     * Inserts player uuid, password and password reminder into the database
     * @return  true if data has been written, false if something will go wrong
     */
    public Boolean insertPassword(UUID playerUUID, String password, String passwordReminder) {
        final String formattedUUID = playerUUID.toString().replace("-", "");
        final String query = "INSERT INTO passwords(uuid, password, password_reminder) VALUES(?, ?, ?)";
        try {
            Connection connection = this.connectToDatabase();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, formattedUUID);
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
     * Changes password associated with uuid
     * @param playerUUID    playerUUID - to find in the database to change password associated with it
     * @param newPassword   newPassword - oldPassword will be changed to this one
     * @return  true if data has been written, false if something will go wrong
     */
    public Boolean changePassword(UUID playerUUID, String newPassword) {
        String formattedUUID = playerUUID.toString().replace("-", "");
        try {
            String query = "UPDATE passwords SET password = ? WHERE uuid = ?";
            Connection connection = this.connectToDatabase();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, formattedUUID);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Changes password reminder associated with uuid
     * @return                      true - if password reminder has been changed, false if something will go wrong
     * @param playerUUID            playerUUID - to find in the database to change password reminder associated with it
     * @param newPasswordReminder   newPasswordReminder - old password reminder will be changed to this one
     */
    public Boolean changePasswordReminder(UUID playerUUID, String newPasswordReminder) {
        String formattedUUID = playerUUID.toString().replace("-", "");
        try {
            String query = "UPDATE passwords SET password_reminder = ? WHERE uuid = ?";
            Connection connection = this.connectToDatabase();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newPasswordReminder);
            preparedStatement.setString(2, formattedUUID);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if player's UUID exists in database. If not - he's not registered
     * @return              true if player is registered, false if not, null if something will go wrong
     * @param playerUUID    playerUUID to check if it exists in the database
     */
    public Boolean isRegistered(UUID playerUUID) throws NullPointerException {
        final String formattedUUID = playerUUID.toString().replace("-", "");
        final String query = "SELECT uuid FROM passwords WHERE uuid LIKE '" + formattedUUID + "'";
        try {
            Connection connection = this.connectToDatabase();
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getString("uuid").equals(formattedUUID);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Returns a password reminder string which player entered while registering. Null if something will go wrong
     * @param playerUUID    playerUUID which will be used to get reminder in the database
     * @return              password reminder string. Null if something will go wrong
     */
    public String getPasswordReminder(UUID playerUUID) {
        final String formattedUUID = playerUUID.toString().replace("-", "");
        final String query = "SELECT password_reminder FROM passwords WHERE uuid LIKE '" + formattedUUID + "'";
        try {
            Connection connection = this.connectToDatabase();
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getString("password_reminder");
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Compares player entered password with one's in the database
     * @param playerUUID    playerUUID to find which player password needs to be compared
     * @param password      password which player entered in in-game chat
     * @return              true if password player entered is the same in the database, else - false. Null if password
     * does not exist
     */
    public Boolean verifyPassword(UUID playerUUID, String password) throws NullPointerException {
        final String formattedUUID = playerUUID.toString().replace("-", "");
        final String query = "SELECT password FROM passwords WHERE uuid LIKE '" + formattedUUID + "'";
        try {
            Connection connection = this.connectToDatabase();
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getString("password").equals(password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method allows connecting to database
     * @return  Connection object
     */
    private Connection connectToDatabase() {
        try {
            String url = "jdbc:sqlite:" + PASSWORDS_DATABASE;
            if (connection != null) {
                connection.close();
            }
            connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method simply created a database if it not exists
     * @return  Boolean true if database has been created. False otherwise
     */
    public Boolean createDatabase() {
        final String url = "jdbc:sqlite:" + PASSWORDS_DATABASE;
        try {
            Connection connection = DriverManager.getConnection(url);
            if (connection != null) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * This method creates a passwords table to store players passwords if it not already exists
     * @return  Boolean true if table has been created. False otherwise
     */
    public Boolean createPasswordsTable() {
        String url = "jdbc:sqlite:" + PASSWORDS_DATABASE;
        String sql = "CREATE TABLE IF NOT EXISTS passwords (\n"
                + "	uuid TEXT, \n"
                + "	password TEXT,\n"
                + "	password_reminder TEXT\n"
                + ");";
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

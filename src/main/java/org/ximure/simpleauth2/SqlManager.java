package org.ximure.simpleauth2;

import java.sql.*;
import java.util.UUID;

import static org.ximure.simpleauth2.SimpleAuth2.PASSWORDS_DATABASE;

public class SqlManager {

    /**
     * It will insert player uuid, password and password reminder in the database
     * @param playerUUID        String playerUUID which will be written (first column)
     * @param password          String password (second column)
     * @param passwordReminder  String password reminder (third column)
     */
    public void setPassword(UUID playerUUID, String password, String passwordReminder) {
        final String formattedUUID = playerUUID.toString().replace("-", "");
        final String query = "INSERT INTO passwords(uuid, password, password_reminder) VALUES(?, ?, ?)";
        try {
            Connection connection = this.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, formattedUUID);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, passwordReminder);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method allows changing password in the database
     * @param playerUUID    UUID playerUUID to find in the database to change password associate with it
     * @param newPassword   String newPassword oldPassword will be changed to this one
     */
    public void changePassword(UUID playerUUID, String newPassword) {
        String formattedUUID = playerUUID.toString().replace("-", "");
        try {
            Connection connection = this.connectToDatabase();
            String query = "UPDATE passwords SET password = '" + newPassword + "' WHERE uuid = '" + formattedUUID + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePasswordReminder(UUID playerUUID, String newPasswordReminder) {
        String formattedUUID = playerUUID.toString().replace("-", "");
        try {
            Connection connection = this.connectToDatabase();
            String query = "UPDATE passwords SET password_reminder = '" + newPasswordReminder +
                    "' WHERE uuid = '" + formattedUUID + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if player's UUID exists in database. If not - he's not registered
     * @param playerUUID    UUID playerUUID to check if it exists in the database
     * @return              Boolean true if registered. Otherwise - false
     */
    public Boolean isRegistered(UUID playerUUID) {
        final String formattedUUID = playerUUID.toString().replace("-", "");
        final String query = "SELECT uuid FROM passwords WHERE uuid LIKE '" + formattedUUID + "'";
        try {
            Connection connection = this.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getString("uuid").equals(formattedUUID);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Returns a password reminder string which player entered while registering
     * @param playerUUID    UUID playerUUID which will be used to get reminder in the database
     * @return              String password reminder. Null if something will go wrong
     */
    public String getPasswordReminder(UUID playerUUID) {
        final String formattedUUID = playerUUID.toString().replace("-", "");
        final String query = "SELECT password_reminder FROM passwords WHERE uuid LIKE '" + formattedUUID + "'";
        try {
            Connection connection = this.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getString("password_reminder");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Compares player entered password with one's in the database
     * @param playerUUID    UUID playerUUID to find which player password needs to be compared
     * @param password      String password which player entered in in-game chat
     * @return              Boolean true if password player entered is the same in the database, else - false
     */
    public Boolean checkPassword(UUID playerUUID, String password) {
        final String formattedUUID = playerUUID.toString().replace("-", "");
        final String query = "SELECT password FROM passwords WHERE uuid LIKE '" + formattedUUID + "'";
        try {
            Connection connection = this.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getString("password").equals(password);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * This method allows connecting to database
     * @return  Connection object
     */
    private Connection connectToDatabase() {
        final String url = "jdbc:sqlite:" + PASSWORDS_DATABASE;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
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

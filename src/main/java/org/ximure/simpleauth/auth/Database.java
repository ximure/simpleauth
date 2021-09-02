package org.ximure.simpleauth.auth;

import java.util.UUID;

public interface Database {
    public Boolean insertPassword(UUID playerUUID, String password, String passwordReminder);
    public Boolean changePassword(UUID playerUUID, String newPassword);
    public Boolean changePasswordReminder(UUID playerUUID, String newPasswordReminder);
    public Boolean isRegistered(UUID playerUUID) throws NullPointerException;
    public Boolean isCorrectPassword(UUID playerUUID, String password) throws NullPointerException;
    public Boolean createDatabase();
    public Boolean createPasswordsTable();
    public String getPasswordReminder(UUID playerUUID);
}

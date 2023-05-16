package de.nikolas.einsteinmedia.repository;

import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.LoginRequestModel;
import de.nikolas.einsteinmedia.models.User;
import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;

import java.sql.ResultSet;
import java.util.Objects;

import lombok.Getter;

/**
 * @author Nikolas Rummel
 * @since 29.09.2021
 */
@Getter
public class UserRepository {

    private static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS users"
                    + "("
                    + " uniqueId INT NOT NULL AUTO_INCREMENT,"
                    + " firstName varchar(255) NOT NULL,"
                    + " lastName varchar(255) NOT NULL,"
                    + " userName varchar(255) NOT NULL,"
                    + " email varchar(255) NOT NULL,"
                    + " password TEXT NOT NULL,"
                    + " PRIMARY KEY (uniqueId)"
                    + ")";

    private DatabaseConnection databaseConnection;
    private Logger logger;

    public UserRepository() {
        this.databaseConnection =
                new DatabaseConnection(
                        "localhost", "einsteinmedia", "root", "", 3306);
        this.databaseConnection.connect();
        this.databaseConnection.queryUpdate(SQL_CREATE);
        this.logger = Logger.Factory.createLogger(UserRepository.class);
        this.logger.info("Started connection to sql server");
    }

    public void saveUser(User user) {
        this.databaseConnection.update(
                "INSERT INTO users (firstName, lastName, userName, email, password) VALUES ('"
                        + user.getFirstName()
                        + "', '"
                        + user.getLastName()
                        + "', '"
                        + user.getUserName()
                        + "', '"
                        + user.getEmail()
                        + "', '"
                        + user.getPassword()
                        + "');");
        this.logger.info(
                "Successfully registered a new user (" + user.getUserName() + "/" + user.getFirstName()
                        + " " + user.getLastName() + ")");
    }

    public boolean userExists(String email) {
        ResultSet resultSet =
                this.databaseConnection.asyncQuery(
                        "SELECT email FROM users WHERE email= '" + email + "'");
        try {
            if (resultSet.next()) {
                return resultSet.getString("email") != null;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUser(String email) {
        ResultSet resultSet =
                this.databaseConnection.asyncQuery(
                        "SELECT * FROM users WHERE email= '" + email + "'");
        try {
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("uniqueId"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("userName"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUser(int uniqueId) {
        ResultSet resultSet =
                this.databaseConnection.asyncQuery(
                        "SELECT * FROM users WHERE uniqueId= '" + uniqueId + "'");
        try {
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("uniqueId"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("userName"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}

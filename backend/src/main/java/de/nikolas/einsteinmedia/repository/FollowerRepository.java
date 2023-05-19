package de.nikolas.einsteinmedia.repository;

import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.User;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Getter
public class FollowerRepository {

    private static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS followers"
                    + "("
                    + " uniqueId INT NOT NULL AUTO_INCREMENT,"
                    + " followeeId INT NOT NULL,"
                    + " followerId INT NOT NULL,"
                    + " PRIMARY KEY (uniqueId)"
                    + ")";

    private final DatabaseConnection databaseConnection;
    private final Logger logger;
    private final UserRepository userRepository = Providers.get(UserRepository.class);

    public FollowerRepository() {
        this.databaseConnection = userRepository.getDatabaseConnection();

        this.databaseConnection.queryUpdate(SQL_CREATE);
        this.logger = Logger.Factory.createLogger(FollowerRepository.class);
    }

    public void addFollower(int followeeId, int followerId) {
        String insertQuery = "INSERT INTO followers (followeeId, followerId) VALUES (%d, %d)";
        String formattedQuery = String.format(insertQuery, followeeId, followerId);
        this.databaseConnection.update(formattedQuery);

        this.logger.info("Successfully added follower (followeeId: " + followeeId + ", followerId: " + followerId + ")");
    }

    public void removeFollower(int followeeId, int followerId) {
        String deleteQuery = "DELETE FROM followers WHERE followeeId = %d AND followerId = %d";
        String formattedQuery = String.format(deleteQuery, followeeId, followerId);
        this.databaseConnection.update(formattedQuery);

        this.logger.info("Successfully removed follower (followeeId: " + followeeId + ", followerId: " + followerId + ")");
    }

    public ArrayList<User> getFollowers(int followeeId) {
        ArrayList<User> followers = new ArrayList<>();

        String selectQuery = "SELECT u.* FROM users u INNER JOIN followers f ON u.uniqueId = f.followerId WHERE f.followeeId = %d";
        String formattedQuery = String.format(selectQuery, followeeId);
        ResultSet resultSet = this.databaseConnection.asyncQuery(formattedQuery);

        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("uniqueId");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String userName = resultSet.getString("userName");
                String email = resultSet.getString("email");
                String profileImage = resultSet.getString("profileImage");
                String bannerImage = resultSet.getString("bannerImage");

                User follower = new User(userId, firstName, lastName, userName, email, null, profileImage, bannerImage);
                followers.add(follower);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return followers;
    }

    public ArrayList<User> getFollowees(int followerId) {
        ArrayList<User> followees = new ArrayList<>();

        String selectQuery = "SELECT u.* FROM users u INNER JOIN followers f ON u.uniqueId = f.followeeId WHERE f.followerId = %d";
        String formattedQuery = String.format(selectQuery, followerId);
        ResultSet resultSet = this.databaseConnection.asyncQuery(formattedQuery);

        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("uniqueId");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String userName = resultSet.getString("userName");
                String email = resultSet.getString("email");
                String profileImage = resultSet.getString("profileImage");
                String bannerImage = resultSet.getString("bannerImage");

                User followee = new User(userId, firstName, lastName, userName, email, null, profileImage, bannerImage);
                followees.add(followee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return followees;
    }
}

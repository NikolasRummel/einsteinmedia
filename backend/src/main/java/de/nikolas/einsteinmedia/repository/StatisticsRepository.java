package de.nikolas.einsteinmedia.repository;

import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.StatisticsResponse;

import java.sql.ResultSet;

public class StatisticsRepository {

    private final UserRepository userRepository = Providers.get(UserRepository.class);
    public StatisticsResponse getStatistics() {
        StatisticsResponse response = new StatisticsResponse();

        String query =
                "SELECT (SELECT COUNT(*) FROM users) AS users, " +
                "(SELECT COUNT(*) FROM posts) AS posts, " +
                "(SELECT userName FROM users ORDER BY uniqueId DESC LIMIT 1) AS newestUser";

        ResultSet resultSet = this.userRepository.getDatabaseConnection().asyncQuery(query);
        try {
            if (resultSet.next()) {
                response.setUsers(resultSet.getInt("users"));
                response.setPosts(resultSet.getInt("posts"));
                response.setNewestUser(resultSet.getString("newestUser"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}

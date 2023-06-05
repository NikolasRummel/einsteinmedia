package de.nikolas.einsteinmedia.repository;

import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.Post;
import de.nikolas.einsteinmedia.models.responses.PostResponse;
import de.nikolas.einsteinmedia.models.User;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Getter
public class PostsRepository {

    private static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS posts"
                    + "("
                    + " uniqueId INT NOT NULL AUTO_INCREMENT,"
                    + " authorId varchar(255) NOT NULL,"
                    + " timestamp TEXT NOT NULL,"
                    + " headline varchar(255) NOT NULL,"
                    + " imageLink TEXT NOT NULL,"
                    + " text TEXT NOT NULL,"
                    + " likes INT NOT NULL,"
                    + " PRIMARY KEY (uniqueId)"
                    + ")";

    private final DatabaseConnection databaseConnection;
    private final Logger logger;
    private final UserRepository userRepository = Providers.get(UserRepository.class);

    public PostsRepository() {
        this.databaseConnection = userRepository.getDatabaseConnection();

        this.databaseConnection.queryUpdate(SQL_CREATE);
        this.logger = Logger.Factory.createLogger(PostsRepository.class);
        this.logger.info("Started connection to sql server");
    }

    public void createPost(Post post) {
        this.databaseConnection.update(
                "INSERT INTO posts (authorId, timestamp, headline, imageLink, text, likes) VALUES ('"
                        + post.getAuthorId()
                        + "', '"
                        + post.getTimestamp()
                        + "', '"
                        + post.getHeadline()
                        + "', '"
                        + post.getImageLink()
                        + "', '"
                        + post.getText()
                        + "', '"
                        + post.getLikes()
                        + "');");
        this.logger.info(
                "Successfully created a new post (" + post.getAuthorId() + " / " + post.getHeadline() + ")");
    }

    public void deletePost(int postId) {
        String deleteQuery = "DELETE FROM posts WHERE uniqueId = %d";
        String formattedQuery = String.format(deleteQuery, postId);
        this.databaseConnection.update(formattedQuery);

        this.logger.info("Successfully removed post (postId: " + postId + ")");
    }

    public int getPostCount() {
        ResultSet resultSet = this.databaseConnection.asyncQuery("SELECT COUNT(*) AS postCount FROM posts");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("postCount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public ArrayList<PostResponse> getAllPostsResponses() {
        ArrayList<PostResponse> posts = new ArrayList<>();

        ResultSet resultSet = this.databaseConnection.asyncQuery(
                "SELECT p.*, u.* " +
                        "FROM posts p " +
                        "INNER JOIN users u ON p.authorId = u.uniqueId");

        try {

            while (resultSet.next()) {
                int postId = resultSet.getInt("uniqueId");
                int authorId = resultSet.getInt("authorId");
                String timestamp = resultSet.getString("timestamp");
                String headline = resultSet.getString("headline");
                String imageLink = resultSet.getString("imageLink");
                String text = resultSet.getString("text");
                int likes = resultSet.getInt("likes");
                int userId = resultSet.getInt("u.uniqueId");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String userName = resultSet.getString("userName");
                String email = resultSet.getString("email");
                String profileImage = resultSet.getString("profileImage");
                String bannerImage = resultSet.getString("bannerImage");

                Date date = new Date(Long.parseLong(timestamp));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);

                User user = new User(userId, firstName, lastName, userName, email, null, profileImage, bannerImage);
                Post post = new Post(postId, authorId, formattedDate, headline, imageLink, text, likes);
                posts.add(new PostResponse(post, user));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }


    public ArrayList<PostResponse> getAllPostsResponses(String email) {
        ArrayList<PostResponse> posts = new ArrayList<>();

        ResultSet resultSet = this.databaseConnection.asyncQuery(
                "SELECT p.*, u.* " +
                        "FROM posts p " +
                        "INNER JOIN users u ON p.authorId = u.uniqueId " +
                        "WHERE u.email = '" + email + "'");


        try {
            while (resultSet.next()) {
                int postId = resultSet.getInt("uniqueId");
                int authorId = resultSet.getInt("authorId");
                String timestamp = resultSet.getString("timestamp");
                String headline = resultSet.getString("headline");
                String imageLink = resultSet.getString("imageLink");
                String text = resultSet.getString("text");
                int likes = resultSet.getInt("likes");
                int userId = resultSet.getInt("u.uniqueId");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String userName = resultSet.getString("userName");
                String profileImage = resultSet.getString("profileImage");
                String bannerImage = resultSet.getString("bannerImage");

                Date date = new Date(Long.parseLong(timestamp));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);

                User user = new User(userId, firstName, lastName, userName, email, null, profileImage, bannerImage);
                Post post = new Post(postId, authorId, formattedDate, headline, imageLink, text, likes);
                posts.add(new PostResponse(post, user));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

}

package de.nikolas.einsteinmedia.repository;

import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.Comment;
import de.nikolas.einsteinmedia.models.Post;
import de.nikolas.einsteinmedia.models.User;
import de.nikolas.einsteinmedia.models.responses.CommentResponse;
import de.nikolas.einsteinmedia.models.responses.PostResponse;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class CommentsRepository {

    private static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS comments"
                    + "("
                    + " uniqueId INT NOT NULL AUTO_INCREMENT,"
                    + " postId INT NOT NULL,"
                    + " authorId varchar(255) NOT NULL,"
                    + " timestamp TEXT NOT NULL,"
                    + " text TEXT NOT NULL,"
                    + " PRIMARY KEY (uniqueId)"
                    + ")";


    private final DatabaseConnection databaseConnection;
    private final Logger logger;
    private final UserRepository userRepository = Providers.get(UserRepository.class);

    public CommentsRepository() {
        this.databaseConnection = userRepository.getDatabaseConnection();

        this.databaseConnection.queryUpdate(SQL_CREATE);
        this.logger = Logger.Factory.createLogger(CommentsRepository.class);
    }

    public void createComment(Comment comment) {
        this.databaseConnection.update(
                "INSERT INTO comments (postId, authorId, timestamp, text) VALUES ('"
                        + comment.getPostId()
                        + "', '"
                        + comment.getAuthorId()
                        + "', '"
                        + comment.getTimestamp()
                        + "', '"
                        + comment.getText()
                        + "');");
        this.logger.info(
                "Successfully created a new comment (" + comment.getAuthorId() + " / " + comment.getText() + ")");
    }

    public List<CommentResponse> getAllCommentsOf(int postId) {
        List<CommentResponse> comments = new ArrayList<>();

        ResultSet resultSet = this.databaseConnection.query("SELECT * FROM comments c INNER JOIN users " +
                "u ON c.authorId = u.uniqueId WHERE postId = " + postId);

        try {
            while (resultSet.next()) {
                //fetch comment data
                int commentId = resultSet.getInt("uniqueId");
                int authorId = resultSet.getInt("authorId");
                String timestamp = resultSet.getString("timestamp");
                String text = resultSet.getString("text");

                // fetch user data
                int userId = resultSet.getInt("u.uniqueId");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String userName = resultSet.getString("userName");
                String email = resultSet.getString("email");
                String profileImage = resultSet.getString("profileImage");

                // format date
                Date date = new Date(Long.parseLong(timestamp));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);

                // Create response
                Comment comment = new Comment(commentId, postId, authorId, formattedDate, text);
                User user = new User(userId, firstName, lastName, userName, email, null, profileImage, null);
                comments.add(new CommentResponse(comment, user));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }



}

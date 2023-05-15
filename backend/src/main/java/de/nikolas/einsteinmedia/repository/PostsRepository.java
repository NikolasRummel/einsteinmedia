package de.nikolas.einsteinmedia.repository;

import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.Post;
import de.nikolas.einsteinmedia.models.PostRequestModel;
import de.nikolas.einsteinmedia.models.User;
import lombok.Getter;

import java.sql.ResultSet;

/**
 * @author Nikolas Rummel
 * @since 29.09.2021
 */
@Getter
public class PostsRepository {

    private static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS posts"
                    + "("
                    + " uniqueId INT NOT NULL AUTO_INCREMENT,"
                    + " authorId varchar(255) NOT NULL,"
                    + " timestamp LONG NOT NULL,"
                    + " headline varchar(255) NOT NULL,"
                    + " imageLink TEXT NOT NULL,"
                    + " text TEXT NOT NULL,"
                    + " likes INT NOT NULL,"
                    + " PRIMARY KEY (uniqueId)"
                    + ")";

    private final DatabaseConnection databaseConnection;
    private final Logger logger;

    public PostsRepository() {
        UserRepository repository = Providers.get(UserRepository.class);
        this.databaseConnection = repository.getDatabaseConnection();

        this.databaseConnection.queryUpdate(SQL_CREATE);
        this.logger = Logger.Factory.createLogger(PostsRepository.class);
        this.logger.info("Started connection to sql server");
    }

    public void createPost(Post post) {
        System.out.println("---------__---_!!!!!!!!!!");
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

}

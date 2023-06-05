package de.nikolas.einsteinmedia.controller;

import de.nikolas.einsteinmedia.commons.auth.AuthProvider;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpStatus;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpController;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpMapping;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.Comment;
import de.nikolas.einsteinmedia.models.Post;
import de.nikolas.einsteinmedia.models.requests.CommentRequestModel;
import de.nikolas.einsteinmedia.models.requests.PostRequestModel;
import de.nikolas.einsteinmedia.models.responses.CommentResponse;
import de.nikolas.einsteinmedia.models.responses.PostResponse;
import de.nikolas.einsteinmedia.models.responses.StatisticsResponse;
import de.nikolas.einsteinmedia.repository.CommentsRepository;
import de.nikolas.einsteinmedia.repository.PostsRepository;
import de.nikolas.einsteinmedia.repository.StatisticsRepository;
import de.nikolas.einsteinmedia.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HttpController
public class CommentsController {

    private final CommentsRepository commentsRepository = Providers.get(CommentsRepository.class);
    private final UserRepository userRepository = Providers.get(UserRepository.class);
    private final AuthProvider authProvider = Providers.get(AuthProvider.class);

    @HttpMapping(path = "/comments/", method = HttpMethod.POST)
    public String createNewPost(HttpRequest request, HttpResponse response) {
        if (!authProvider.checkToken(request.getToken())) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return null;
        }

        String email = authProvider.getEmailByKey(request.getToken());
        int authorId = userRepository.getUser(email).getUniqueId();

        CommentRequestModel requestModel = request.getBodyAsObject(CommentRequestModel.class);

        commentsRepository.createComment(new Comment(
                -1,
                requestModel.getPostId(),
                authorId,
                String.valueOf(System.currentTimeMillis()),
                requestModel.getText()
        ));
        return "Successfully created a new comment";
    }

    @HttpMapping(path = "/comments/post/{uniqueId}", method = HttpMethod.GET)
    public List<CommentResponse> getAllCommentsByPostId(HttpRequest request, HttpResponse response) {
        System.out.println("getAllCommentsByPostId");
        int uniqueId = Integer.parseInt(request.getPathParameter("uniqueId"));

        List<CommentResponse> postResponses = commentsRepository.getAllCommentsOf(uniqueId);
        return postResponses;
    }

}

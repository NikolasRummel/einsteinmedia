package de.nikolas.einsteinmedia.controller;

import de.nikolas.einsteinmedia.commons.auth.AuthProvider;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpStatus;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpController;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpMapping;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.*;
import de.nikolas.einsteinmedia.repository.PostsRepository;
import de.nikolas.einsteinmedia.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Nikolas Rummel
 * @since 17.10.2021
 */
@HttpController
public class PostController {

    private final PostsRepository postsRepository = Providers.get(PostsRepository.class);
    private final UserRepository userRepository = Providers.get(UserRepository.class);
    private final AuthProvider authProvider = Providers.get(AuthProvider.class);

    @HttpMapping(path = "/posts/", method = HttpMethod.POST)
    public String createNewPost(HttpRequest request, HttpResponse response) {
        if (!authProvider.checkToken(request.getToken())) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return null;
        }

        String email = authProvider.getEmailByKey(request.getToken());
        PostRequestModel requestModel = request.getBodyAsObject(PostRequestModel.class);
        int authorId = userRepository.getUser(email).getUniqueId();

        System.out.println("000000" + requestModel.getImageLink() + "0000000");

        postsRepository.createPost(new Post(
                -1, authorId, String.valueOf(System.currentTimeMillis()),
                requestModel.getHeadline(), requestModel.getImageLink(), requestModel.getText(), 0
        ));
        return "Successfully created a new post";
    }


    @HttpMapping(path = "/posts/delete/{uniqueId}", method = HttpMethod.POST)
    public String deletePost(HttpRequest request, HttpResponse response) {
        if (!authProvider.checkToken(request.getToken())) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return null;
        }
        int uniqueId = Integer.parseInt(request.getPathParameter("uniqueId"));

        String email = authProvider.getEmailByKey(request.getToken());

        Optional<PostResponse> optional = postsRepository.getAllPostsResponses(email)
                .stream()
                .filter(postResponse -> postResponse.getPost().getUniqueId() == uniqueId)
                .findAny();

        if (optional.isPresent()) {
            response.setStatusCode(HttpStatus.OK);
            postsRepository.deletePost(uniqueId);
            return "Successfully deleted post";
        }

        response.setStatusCode(HttpStatus.FORBIDDEN);
        return "You are not allowed to delete this post";
    }

    @HttpMapping(path = "/posts/", method = HttpMethod.GET)
    public List<PostResponse> getAllPosts(HttpRequest request, HttpResponse response) {
        ArrayList<PostResponse> postResponses = postsRepository.getAllPostsResponses();
        Collections.reverse(postResponses);
        return postResponses;
    }

    @HttpMapping(path = "/posts/user/{uniqueId}", method = HttpMethod.GET)
    public List<PostResponse> getPostsOfUserById(HttpRequest request, HttpResponse response) {
        int uniqueId = Integer.parseInt(request.getPathParameter("uniqueId"));

        String email = userRepository.getUser(uniqueId).getEmail();

        ArrayList<PostResponse> postResponses = postsRepository.getAllPostsResponses(email);
        Collections.reverse(postResponses);
        return postResponses;
    }

    @HttpMapping(path = "/posts/self", method = HttpMethod.GET)
    public List<PostResponse> getPostsOfUser(HttpRequest request, HttpResponse response) {
        if (!authProvider.checkToken(request.getToken())) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return null;
        }

        String email = authProvider.getEmailByKey(request.getToken());
        ArrayList<PostResponse> postResponses = postsRepository.getAllPostsResponses(email);
        Collections.reverse(postResponses);
        return postResponses;
    }
}

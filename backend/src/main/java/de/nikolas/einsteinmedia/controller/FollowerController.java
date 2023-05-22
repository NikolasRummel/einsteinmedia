package de.nikolas.einsteinmedia.controller;

import de.nikolas.einsteinmedia.commons.auth.AuthProvider;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpStatus;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpController;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpMapping;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.FollowerRequestModel;
import de.nikolas.einsteinmedia.models.User;
import de.nikolas.einsteinmedia.repository.FollowerRepository;
import de.nikolas.einsteinmedia.repository.UserRepository;

import java.util.List;

@HttpController
public class FollowerController {

    private final FollowerRepository followerRepository = Providers.get(FollowerRepository.class);
    private final UserRepository userRepository = Providers.get(UserRepository.class);
    private final AuthProvider authProvider = Providers.get(AuthProvider.class);

    @HttpMapping(path = "/followers/add", method = HttpMethod.POST)
    public String addFollower(HttpRequest request, HttpResponse response) {
        if (!authProvider.checkToken(request.getToken())) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return null;
        }

        String email = authProvider.getEmailByKey(request.getToken());
        int followerId = userRepository.getUser(email).getUniqueId();

        FollowerRequestModel requestModel = request.getBodyAsObject(FollowerRequestModel.class);
        User followee = userRepository.getUser(requestModel.getEmail());
        if (followee == null) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return "User " + requestModel.getEmail() + " not found";
        }

        followerRepository.addFollower(followee.getUniqueId(), followerId);
        return "Successfully added follower";
    }

    @HttpMapping(path = "/followers/remove", method = HttpMethod.DELETE)
    public String removeFollower(HttpRequest request, HttpResponse response) {
        if (!authProvider.checkToken(request.getToken())) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return null;
        }

        String email = authProvider.getEmailByKey(request.getToken());
        int followerId = userRepository.getUser(email).getUniqueId();

        FollowerRequestModel requestModel = request.getBodyAsObject(FollowerRequestModel.class);
        String followeeEmail = requestModel.getEmail();
        User followee = userRepository.getUser(followeeEmail);

        if (followee == null) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return "User not found";
        }

        followerRepository.removeFollower(followee.getUniqueId(), followerId);
        return "Successfully removed follower";
    }

    @HttpMapping(path = "/followers/{uniqueId}", method = HttpMethod.GET)
    public List<User> getFollowers(HttpRequest request, HttpResponse response) {
        String userIdParam = request.getPathParameter("uniqueId").trim();
        int userId = Integer.parseInt(userIdParam);

        List<User> followers = followerRepository.getFollowers(userId);
        if (followers == null) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return null;
        }

        return followers;
    }

    @HttpMapping(path = "/followers/{uniqueId}/followees", method = HttpMethod.GET)
    public List<User> getFollowees(HttpRequest request, HttpResponse response) {
        String userIdParam = request.getPathParameter("uniqueId").trim();
        int userId = Integer.parseInt(userIdParam);

        List<User> followees = followerRepository.getFollowees(userId);
        if (followees == null) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return null;
        }

        return followees;
    }
}

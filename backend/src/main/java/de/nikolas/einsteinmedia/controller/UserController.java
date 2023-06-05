package de.nikolas.einsteinmedia.controller;

import de.nikolas.einsteinmedia.commons.auth.AuthProvider;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpStatus;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpController;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpMapping;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.requests.LoginRequestModel;
import de.nikolas.einsteinmedia.models.responses.LoginResponse;
import de.nikolas.einsteinmedia.models.requests.RegisterRequestModel;
import de.nikolas.einsteinmedia.models.User;
import de.nikolas.einsteinmedia.repository.UserRepository;

@HttpController
public class UserController {

    private UserRepository repository = Providers.get(UserRepository.class);
    private AuthProvider authProvider = Providers.get(AuthProvider.class);

    @HttpMapping(path = "/user/register/", method = HttpMethod.POST)
    public String registerUser(HttpRequest request, HttpResponse response) {
        System.out.println("aaa");
        RegisterRequestModel registerRequestModel = request.getBodyAsObject(
                RegisterRequestModel.class);

        if (repository.userExists(registerRequestModel.getEmail())) {
            System.out.println("Register: Email taken");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return "This email is taken!";
        }

        User user =
                new User(
                        1,
                        registerRequestModel.getFirstName(),
                        registerRequestModel.getLastName(),
                        registerRequestModel.getUsername(),
                        registerRequestModel.getEmail(),
                        registerRequestModel.getPassword(),
                        registerRequestModel.getProfileImage(),
                        registerRequestModel.getBannerImage()
                );
        repository.saveUser(user);
        System.out.println("Register: Registered new User");
        return "Successfully registered";
    }

    @HttpMapping(path = "/user/login/", method = HttpMethod.POST)
    public LoginResponse loginUser(HttpRequest request, HttpResponse response) {
        LoginRequestModel loginRequestModel = request.getBodyAsObject(LoginRequestModel.class);

        User user = repository.getUser(loginRequestModel.getEmail());
        if (user == null) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return null;
        }

        // Correct password
        if (user.getPassword().equals(loginRequestModel.getPassword())) {
            String token = authProvider.authenticateUser(loginRequestModel);
            request.setToken(token);
            response.setStatusCode(HttpStatus.OK);

            return new LoginResponse(user, token);
        } else {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return null;
        }
    }

    @HttpMapping(path = "/user/", method = HttpMethod.GET)
    public User getUserByKey(HttpRequest request, HttpResponse response) {
        if (!authProvider.checkToken(request.getToken())) {
            System.out.println(request.getToken() + "! ! !");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return null;
        }

        String email = authProvider.getEmailByKey(request.getToken());
        return repository.getUser(email);
    }

    @HttpMapping(path = "/user/fetch/{uniqueId}", method = HttpMethod.GET)
    public User getUserById(HttpRequest request, HttpResponse response) {
        int userId = Integer.parseInt(request.getPathParameter("uniqueId"));

        return repository.getUser(userId);
    }

}

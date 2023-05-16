package de.nikolas.einsteinmedia.controller;

import de.nikolas.einsteinmedia.commons.auth.AuthProvider;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpStatus;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpController;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpMapping;
import de.nikolas.einsteinmedia.commons.httpserver.utils.JsonUtils;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.LoginRequestModel;
import de.nikolas.einsteinmedia.models.RegisterRequestModel;
import de.nikolas.einsteinmedia.models.User;
import de.nikolas.einsteinmedia.repository.UserRepository;

import java.sql.ResultSet;
import java.util.Objects;

/**
 * @author Nikolas Rummel
 * @since 17.10.2021
 */
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
                        registerRequestModel.getPassword()
                );
        repository.saveUser(user);
        System.out.println("Register: Registered new User");
        return "Successfully registered";
    }

    @HttpMapping(path = "/user/login/", method = HttpMethod.POST)
    public String loginUser(HttpRequest request, HttpResponse response) {
        LoginRequestModel loginRequestModel = request.getBodyAsObject(LoginRequestModel.class);

        if (checkLoginCredentials(loginRequestModel)) {
            String token = authProvider.authenticateUser(loginRequestModel);
            request.setToken(token);
            response.setStatusCode(HttpStatus.OK);

            return token;
        } else {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return "Wrong username or password";
        }
    }

    @HttpMapping(path = "/user/", method = HttpMethod.GET)
    public User getUser(HttpRequest request, HttpResponse response) {
        if (!authProvider.checkToken(request.getToken())) {
            System.out.println(request.getToken() + "! ! !");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return null;
        }

        String email = authProvider.getEmailByKey(request.getToken());
        return repository.getUser(email);
    }

    private boolean checkLoginCredentials(LoginRequestModel requestModel) {
        ResultSet resultSet =
                this.repository
                        .getDatabaseConnection()
                        .asyncQuery(
                                "SELECT email, password FROM users WHERE email= '" + requestModel.getEmail()
                                        + "'");
        try {
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                return Objects.equals(requestModel.getPassword(), password);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

package de.nikolas.einsteinmedia.commons.auth;

import de.nikolas.einsteinmedia.commons.httpserver.http.server.HttpDispatcher;
import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;
import de.nikolas.einsteinmedia.models.requests.LoginRequestModel;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class AuthProvider {

    private final Logger logger;
    private final HashMap<String, String> authKeys;

    /**
     * Instantiates a new Auth provider.
     */
    public AuthProvider() {
        this.logger = Logger.Factory.createLogger(HttpDispatcher.class);
        this.authKeys = new HashMap<>();
    }

    /**
     * Authenticate a new login. Generates and saves a new Bearer token
     *
     * @param loginRequestModel the login request model
     * @return bearer token
     */
    public String authenticateUser(LoginRequestModel loginRequestModel) {
        final String token = this.generateNewToken();
        this.saveToken(token, loginRequestModel.getEmail());

        this.logger.info(
                "Successfully authenticated " + loginRequestModel.getEmail() + " with token " + token);

        return token;
    }

    /**
     * Stores a token in the redis cache.
     *
     * @param token the token
     */
    private void saveToken(String token, String email) {
        this.authKeys.put(token, email);
    }

    /**
     * Checks if a token is stored in the redis cache.
     *
     * @param token the token
     * @return the boolean
     */
    public boolean checkToken(String token) {
        return this.authKeys.containsKey(token);
    }


    /**
     * Returns the email stored to its key.
     *
     * @param token the token
     * @return the boolean
     */
    public String getEmailByKey(String token) {
        return this.authKeys.get(token);
    }

    /**
     * Generates a new auth token.
     * <p>
     * Output example: wrYl_zl_8dLXaZul7GcfpqmDqr7jEnli 7or_zct_ETxJnOa4ddaEzftNXbuvNSB-
     * CkZss7TdsTVHRHfqBMq_HqQUxBGCTgWj 8loHzi27gJTO1xTqTd9SkJGYP8rYlNQn
     *
     * @return the generated token
     */
    private String generateNewToken() {
        final SecureRandom secureRandom = new SecureRandom();
        final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}

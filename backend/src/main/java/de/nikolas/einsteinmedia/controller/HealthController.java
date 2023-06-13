package de.nikolas.einsteinmedia.controller;

import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpController;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpMapping;

@HttpController
public class HealthController {

    @HttpMapping(path = "/health", method = HttpMethod.GET)
    public String deletePost(HttpRequest request, HttpResponse response) {
        return "running";
    }
}

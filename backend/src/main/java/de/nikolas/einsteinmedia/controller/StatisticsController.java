package de.nikolas.einsteinmedia.controller;

import de.nikolas.einsteinmedia.commons.auth.AuthProvider;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpStatus;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpController;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpMapping;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.Post;
import de.nikolas.einsteinmedia.models.PostRequestModel;
import de.nikolas.einsteinmedia.models.PostResponse;
import de.nikolas.einsteinmedia.models.StatisticsResponse;
import de.nikolas.einsteinmedia.repository.PostsRepository;
import de.nikolas.einsteinmedia.repository.StatisticsRepository;
import de.nikolas.einsteinmedia.repository.UserRepository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Nikolas Rummel
 * @since 17.10.2021
 */
@HttpController
public class StatisticsController {

    private final StatisticsRepository statisticsRepository = Providers.get(StatisticsRepository.class);

    @HttpMapping(path = "/statistics/", method = HttpMethod.GET)
    public StatisticsResponse fetchStatistics(HttpRequest request, HttpResponse response) {
        return statisticsRepository.getStatistics();
    }

}

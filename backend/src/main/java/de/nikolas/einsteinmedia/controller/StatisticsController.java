package de.nikolas.einsteinmedia.controller;

import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpController;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpMapping;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.models.responses.StatisticsResponse;
import de.nikolas.einsteinmedia.repository.StatisticsRepository;

@HttpController
public class StatisticsController {

    private final StatisticsRepository statisticsRepository = Providers.get(StatisticsRepository.class);

    @HttpMapping(path = "/statistics/", method = HttpMethod.GET)
    public StatisticsResponse fetchStatistics(HttpRequest request, HttpResponse response) {
        return statisticsRepository.getStatistics();
    }

}

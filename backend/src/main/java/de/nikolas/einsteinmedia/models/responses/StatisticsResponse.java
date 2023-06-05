package de.nikolas.einsteinmedia.models.responses;

import lombok.Data;

@Data
public class StatisticsResponse {
    private int users;
    private int posts;
    private String newestUser;
}

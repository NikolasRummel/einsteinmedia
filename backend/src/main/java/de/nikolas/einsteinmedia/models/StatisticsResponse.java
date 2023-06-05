package de.nikolas.einsteinmedia.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class StatisticsResponse {
    private int users;
    private int posts;
    private String newestUser;
}

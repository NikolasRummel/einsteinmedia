package de.nikolas.einsteinmedia.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IsFollowingRequest {

    private int uniqueId;
    private int uniqueId2;
}

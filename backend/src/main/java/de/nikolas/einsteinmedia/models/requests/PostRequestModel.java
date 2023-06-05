package de.nikolas.einsteinmedia.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestModel {

    private String headline;
    private String imageLink;
    private String text;
}

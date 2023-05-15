package de.nikolas.einsteinmedia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nikolas Rummel
 * @since 09.09.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestModel {

    private String headline;
    private String imageLink;
    private String text;
}

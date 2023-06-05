package de.nikolas.einsteinmedia.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequestModel {

  private String firstName, lastName, username, email, password, bannerImage, profileImage;

}

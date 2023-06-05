package de.nikolas.einsteinmedia.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginRequestModel {

  private String email, password;
}

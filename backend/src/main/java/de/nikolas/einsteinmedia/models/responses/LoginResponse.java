package de.nikolas.einsteinmedia.models.responses;

import de.nikolas.einsteinmedia.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  private User user;
  private String authKey;

}

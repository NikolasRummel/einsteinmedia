package de.nikolas.einsteinmedia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private int uniqueId;
  private String firstName;
  private String lastName;
  private String userName;
  private String email;
  private String password;
  private String profileImage;
  private String bannerImage;

}

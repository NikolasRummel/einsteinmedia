package de.nikolas.einsteinmedia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

  private int uniqueId;
  private int authorId;
  private String timestamp;
  private String headline;
  private String imageLink;
  private String text;
  private int likes;

}

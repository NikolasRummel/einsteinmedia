package de.nikolas.einsteinmedia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nikolas Rummel
 * @since 09.09.2021
 * ## Post (id, date, headline, link, text, likes)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

  private int uniqueId;
  private int authorId;
  private long timestamp;
  private String headline;
  private String imageLink;
  private String text;
  private int likes;

}

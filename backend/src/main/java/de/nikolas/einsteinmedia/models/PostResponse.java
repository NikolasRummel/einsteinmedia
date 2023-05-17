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
public class PostResponse {

  private Post post;
  private User author;

}

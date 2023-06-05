package de.nikolas.einsteinmedia.models.responses;

import de.nikolas.einsteinmedia.models.Post;
import de.nikolas.einsteinmedia.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

  private Post post;
  private User author;

}

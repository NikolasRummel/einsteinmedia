package de.nikolas.einsteinmedia.models.responses;

import de.nikolas.einsteinmedia.models.Comment;
import de.nikolas.einsteinmedia.models.Post;
import de.nikolas.einsteinmedia.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

  private Comment comment;
  private User author;

}

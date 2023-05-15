package de.nikolas.einsteinmedia.commons.httpserver.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Nikolas Rummel
 * @since 06.10.2021
 */
public class IoUtils {

  public static int copy(InputStream in, OutputStream out) throws IOException {
    return copy(in, out, 1024);
  }

  public static int copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
    byte[] buffer = new byte[bufferSize];
    int count = 0;
    for (int n; (n = in.read(buffer)) != -1; ) {
      out.write(buffer, 0, n);
      count += n;
    }
    out.flush();
    return count;
  }
}

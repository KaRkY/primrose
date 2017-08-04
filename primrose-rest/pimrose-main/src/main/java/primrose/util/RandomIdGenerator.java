package primrose.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomIdGenerator {
  private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final Random RND   = new SecureRandom();

  public static String next64(final int length) {
    final StringBuilder builder = new StringBuilder();

    for (int i = 0; i < length; i++) {
      builder.append(CHARS.charAt(RND.nextInt(CHARS.length())));
    }

    return builder.toString();
  }
}

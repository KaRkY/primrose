package primrose.util;

public class StringUtils {

  private StringUtils() {
    throw new UnsupportedOperationException();
  }

  public static String leftPad(final String str, final int size, final char character) {
    if (str == null) { return null; }

    if (str.length() >= size) { return str; }

    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < size - str.length(); i++) {
      builder.append(character);
    }

    return builder.append(str).toString();
  }
}

package primrose.util;

public class Mod {
  private Mod() {
    throw new UnsupportedOperationException();
  }

  public static int mod11(final String id) {
    if (id == null) { throw new IllegalArgumentException(); }

    if (id.length() < 2 || id.length() > 15) { throw new IllegalArgumentException(); }

    int sum = 0;
    for (int i = id.length() - 1; i > 0; i--) {
      final int val = Character.getNumericValue(id.charAt(i));
      sum = sum + (id.length() - i + 1) * val;
    }
    return sum % 11;
  }

  public static boolean checkMod11(final String id) {
    if (id == null) { throw new IllegalArgumentException(); }

    if (id.length() < 2 || id.length() > 11) { throw new IllegalArgumentException(); }

    int sum = 0;
    for (int i = id.length() - 1; i > 0; i--) {
      final int val = Character.getNumericValue(id.charAt(i));
      sum = sum + (id.length() - i) * val;
    }

    final int remainder = sum % 11;

    return remainder == 0;
  }
}

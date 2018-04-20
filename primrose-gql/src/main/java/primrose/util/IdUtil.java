package primrose.util;

public final class IdUtil {

   private static final int ID_RADIX = 36;

   private IdUtil() {
      throw new UnsupportedOperationException();
   }

   public static int pseudo(final int value) {
      int l1 = value >> 16 & 65535;
      int r1 = value & 65535;

      for (int i = 0; i < 3; i++) {
         final int l2 = r1;
         final int r2 = l1 ^ (int) Math.round((1366 * r1 + 150889) % 714025 / 714025.0 * 32767);
         l1 = l2;
         r1 = r2;
      }
      return (r1 << 16) + l1;
   }

   public static long pseudo(final long value) {
      long l1 = value >> 32 & 4294967295L;
      long r1 = value & 4294967295L;

      for (int i = 0; i < 3; i++) {
         final long l2 = r1;
         final long r2 = l1 ^ Math.round((1366 * r1 + 150889) % 714025 / 714025.0 * 2147483647);
         l1 = l2;
         r1 = r2;
      }
      return (r1 << 32) + l1;
   }

   public static String toStringId(final long value) {
      return Long.toString(pseudo(value), ID_RADIX);
   }

   public static String toStringId(final int value) {
      return Integer.toString(pseudo(value), ID_RADIX);
   }

   public static long valueOfLongId(final String value) {
      return pseudo(Long.valueOf(value, ID_RADIX));
   }

   public static int valueOfIntegerId(final String value) {
      return pseudo(Integer.valueOf(value, ID_RADIX));
   }
}

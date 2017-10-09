package primrose.pagging.sort;

import primrose.pagging.sort.ImmutableSort.Builder;

public class SortUtil {

  private SortUtil() {
    throw new UnsupportedOperationException();
  }

  public static Sort parseSort(final String sortSpec) {
    final Builder builder = ImmutableSort.builder();

    final String[] split = sortSpec != null ? sortSpec.split(";") : new String[]{};
    for (final String element : split) {
      if (element.trim().isEmpty()) {
        continue;
      }

      if (element.startsWith("-")) {
        builder.addFields(ImmutableSortField
          .builder()
          .name(element.substring(1))
          .direction(SortDirection.DESC)
          .build());
      } else {
        if (element.startsWith("+")) {
          builder.addFields(ImmutableSortField
            .builder()
            .name(element.substring(1))
            .direction(SortDirection.ASC)
            .build());
        } else {
          builder.addFields(ImmutableSortField
            .builder()
            .name(element)
            .direction(SortDirection.ASC)
            .build());
        }
      }
    }

    return builder.build();
  }
}

package primrose.jooq;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;

import primrose.pagination.SortDirection;

public class JooqUtil {

  public static SortOrder map(final SortDirection direction) {
    switch (direction) {
    case ASC:
      return SortOrder.ASC;

    case DESC:
      return SortOrder.DESC;

    case DEFAULT:
      return SortOrder.DEFAULT;

    default:
      return SortOrder.DEFAULT;
    }
  }

  public static List<? extends SortField<?>> map(final Optional<Map<String, SortDirection>> sortProperties, final Function<String, Field<?>> mapping) {
    return sortProperties
      .map(sp -> sp
        .entrySet()
        .stream()
        .map(entry -> {
          final Field<?> mappedField = mapping.apply(entry.getKey());
          if (mappedField != null) {
            return mappedField.sort(map(entry.getValue()));
          } else {
            return null;
          }
        })
        .collect(Collectors.toList()))
      .orElseGet(() -> Collections.emptyList());
  }
}

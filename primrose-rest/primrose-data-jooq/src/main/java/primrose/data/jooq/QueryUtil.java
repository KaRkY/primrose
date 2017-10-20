package primrose.data.jooq;

import static org.jooq.impl.DSL.concat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.TableField;
import org.jooq.impl.DSL;

import primrose.model.sort.Sort;
import primrose.model.sort.SortDirection;

public class QueryUtil {

  private QueryUtil() {
    throw new UnsupportedOperationException();
  }

  public static <T extends Collection<Condition>> T addLikeIgnoreCase(
    final T conditions,
    final String value,
    final TableField<?, String> field) {
    if (value != null) {
      conditions.add(field.likeIgnoreCase(concat("%", concat(value, "%")), '_'));
    }

    return conditions;
  }

  public static Field<LocalDateTime> infinity() {
    return DSL.field("'infinity'::timestamp", LocalDateTime.class);
  }

  public static List<SortField<?>> map(final Sort sort, final Function<String, Field<?>> mapping) {
    return sort
      .fields()
      .stream()
      .filter(field -> mapping.apply(field.name()) != null)
      .map(field -> mapping.apply(field.name()).sort(map(field.direction())))
      .collect(Collectors.toList());
  }

  public static SortOrder map(final SortDirection order) {
    switch (order) {
    case ASC:
      return SortOrder.ASC;
    case DESC:
      return SortOrder.DESC;

    default:
      return SortOrder.ASC;
    }
  }

  public static Field<LocalDateTime> valOrInfinity(final Field<LocalDateTime> field) {
    return DSL.coalesce(field, infinity());
  }
}

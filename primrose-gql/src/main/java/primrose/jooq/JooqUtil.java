package primrose.jooq;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.impl.DSL;

import primrose.service.Sort;

public class JooqUtil {

  public static SortOrder map(final String direction) {
    String normalizedDirection = direction != null ? direction.toUpperCase() : null;
    switch (normalizedDirection) {
    case "ASC":
      return SortOrder.ASC;

    case "DESC":
      return SortOrder.DESC;

    default:
      return SortOrder.DEFAULT;
    }
  }

  public static SortField<?> map(final Sort sort, final Function<String, Field<?>> mapping) {
    final Field<?> mappedField = mapping.apply(sort.getProperty());
    if (mappedField != null) {
      return mappedField.sort(map(sort.getDirection()));
    } else {
      return null;
    }
  }

  public static <T> Condition between(Field<T> field, Field<T> from, Field<T> to) {
    return field.greaterOrEqual(from).and(field.lt(to).or(to.isNull()));
  }

  @SafeVarargs
  public static <T> List<Condition> search(Field<T> value, Field<T>... fields) {
    return Arrays
        .stream(fields)
        .map(field -> field.likeIgnoreCase(DSL.concat(DSL.value("%"), DSL.isnull(value, ""), DSL.value("%"))))
        .collect(Collectors.toList());
  }

  @SafeVarargs
  public static <T> List<Condition> search(T value, Field<T>... fields) {
    return search(DSL.value(value), fields);
  }

  public static <T> Condition search(Field<T> value, Field<T> field) {
    return field.likeIgnoreCase(DSL.concat(DSL.value("%"), DSL.isnull(value, ""), DSL.value("%")));
  }

  public static <T> Condition search(T value, Field<T> field) {
    return search(DSL.value(value), field);
  }
}

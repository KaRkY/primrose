package primrose.graphql.scalars;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

@Component
public class GraphQLLocalDateTime extends GraphQLScalarType {

  public GraphQLLocalDateTime() {
    super("LocalDateTime", "LocalDateTime type", new Coercing<LocalDateTime, String>() {
      private LocalDateTime convertImpl(final Object input) {
        if (input instanceof String) { return LocalDateTime.parse((String) input, DateTimeFormatter.ISO_LOCAL_DATE_TIME); }
        return null;
      }

      @Override
      public LocalDateTime parseLiteral(final Object input) {
        if (!(input instanceof StringValue)) { return null; }
        final String value = ((StringValue) input).getValue();
        return convertImpl(value);
      }

      @Override
      public LocalDateTime parseValue(final Object input) {
        final LocalDateTime result = convertImpl(input);
        if (result == null) { throw new CoercingParseValueException("Invalid value '" + input + "' for Date"); }
        return result;
      }

      @Override
      public String serialize(final Object input) {
        if (input instanceof LocalDateTime) {
          return ((LocalDateTime) input).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else {
          final LocalDateTime result = convertImpl(input);
          if (result == null) { throw new CoercingSerializeException("Invalid value '" + input + "' for Date"); }
          return result.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
      }
    });
  }

}

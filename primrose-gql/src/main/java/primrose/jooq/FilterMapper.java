package primrose.jooq;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Field;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import cz.jirutka.rsql.parser.ast.Node;

@Component
public class FilterMapper {

  private ConversionService conversionService;

  public FilterMapper(ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  public Condition map(Map<String, Field<?>> fields, Node node) {
    return node.accept(new JooqRSQLVisitor(fields, conversionService));
  }

  public Condition map(Set<Field<?>> fields, Node node) {
    return map(fields.stream().collect(Collectors.toMap(val -> val.getName(), val -> val)), node);
  }
}

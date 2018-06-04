package primrose.jooq;

import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.core.convert.ConversionService;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import primrose.RsqlSearchOperation;

public class JooqRSQLVisitor implements RSQLVisitor<Condition, Void> {

  private Map<String, Field<?>> fields;
  private ConversionService conversionService;

  public JooqRSQLVisitor(
    Map<String, Field<?>> fields,
    ConversionService conversionService) {
    this.fields = fields;
    this.conversionService = conversionService;
  }

  @Override
  public Condition visit(AndNode node, Void param) {
    return DSL.and(node.getChildren().stream().map(n -> n.accept(this, param)).collect(Collectors.toList()));
  }

  @Override
  public Condition visit(OrNode node, Void param) {
    return DSL.or(node.getChildren().stream().map(n -> n.accept(this, param)).collect(Collectors.toList()));
  }

  @Override
  public Condition visit(ComparisonNode node, Void param) {
    @SuppressWarnings("unchecked")
    Field<Object> field = (Field<Object>) fields.get(node.getSelector());

    if(field == null) throw new IllegalArgumentException(String.format("Field with name %s does not exist!", node.getSelector()));

    switch (RsqlSearchOperation.getSimpleOperator(node.getOperator())) {
    case EQUAL:
      return field.equal(conversionService.convert(node.getArguments().get(0), field.getType()));

    case NOT_EQUAL:
      return field.notEqual(conversionService.convert(node.getArguments().get(0), field.getType()));

    case GREATER_THAN:
      return field.greaterThan(conversionService.convert(node.getArguments().get(0), field.getType()));

    case GREATER_THAN_OR_EQUAL:
      return field.greaterOrEqual(conversionService.convert(node.getArguments().get(0), field.getType()));

    case LESS_THAN:
      return field.lessThan(conversionService.convert(node.getArguments().get(0), field.getType()));

    case LESS_THAN_OR_EQUAL:
      return field.lessOrEqual(conversionService.convert(node.getArguments().get(0), field.getType()));

    case IN:
      return field.in(node.getArguments().stream().map(el -> conversionService.convert(el, field.getType())).collect(Collectors.toList()));

    case NOT_IN:
      return field.notIn(node.getArguments().stream().map(el -> conversionService.convert(el, field.getType())).collect(Collectors.toList()));

    case LIKE:
      return field.like(node.getArguments().get(0));

    default:
      return DSL.condition(true);
    }
  }

}

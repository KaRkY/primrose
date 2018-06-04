package primrose;

import java.util.HashSet;
import java.util.Set;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

public class PrimroseRSQLOperators {
  public static final ComparisonOperator LIKE = new ComparisonOperator("=like=");

  public static Set<ComparisonOperator> defaultOperators() {
    HashSet<ComparisonOperator> operators = new HashSet<>();
    operators.addAll(RSQLOperators.defaultOperators());
    operators.add(LIKE);
    return operators;
  }
}

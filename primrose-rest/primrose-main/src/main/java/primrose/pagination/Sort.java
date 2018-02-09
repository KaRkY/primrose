package primrose.pagination;

import java.util.List;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Sort {

  List<SortField> fields();
}

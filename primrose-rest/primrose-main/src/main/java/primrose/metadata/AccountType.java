package primrose.metadata;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@Value.Immutable()
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface AccountType {

  String name();
}

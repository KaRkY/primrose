package primrose.accounts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@Value.Immutable()
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface AccountContactType {

  String name();
}

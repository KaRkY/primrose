package primrose.contacts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Contact {

  String id();

  String name();

  String email();

  String phone();
}

package primrose.contacts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import primrose.model.BaseInputAccountContact;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface InputAccountContact extends BaseInputAccountContact {

}

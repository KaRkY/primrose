package primrose.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import primrose.model.BaseInputAccountAddress;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface InputAccountAddress extends BaseInputAccountAddress {

}

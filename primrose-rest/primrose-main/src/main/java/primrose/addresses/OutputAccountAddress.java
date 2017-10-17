package primrose.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import primrose.model.BaseOutputAccountAddress;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface OutputAccountAddress extends BaseOutputAccountAddress {

}

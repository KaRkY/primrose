package primrose.data.jooq;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import primrose.model.output.BaseOutputAccountType;

@Value.Immutable()
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface OutputAccountType extends BaseOutputAccountType {

}

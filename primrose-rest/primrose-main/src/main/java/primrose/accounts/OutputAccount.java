package primrose.accounts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import primrose.model.BaseOutputAccount;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface OutputAccount extends BaseOutputAccount {

}

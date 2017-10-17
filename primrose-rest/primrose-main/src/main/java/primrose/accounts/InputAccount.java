package primrose.accounts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import primrose.model.BaseInputAccount;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface InputAccount extends BaseInputAccount {

}

package primrose.immutables;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.model.input.BaseInputAccount;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize
@JsonDeserialize(builder = ImmutableInputAccount.Builder.class)
public interface InputAccount extends BaseInputAccount {

}

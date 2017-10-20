package primrose.immutables;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.accounts.ImmutableInputAccountAddress;
import primrose.accounts.ImmutableInputAccountAddress.Builder;
import primrose.model.input.BaseInputAccountAddress;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize
@JsonDeserialize(builder = ImmutableInputAccountAddress.Builder.class)
public interface InputAccountAddress extends BaseInputAccountAddress {

}

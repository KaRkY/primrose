package primrose.immutables;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.model.input.BaseInputAccountContact;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize
@JsonDeserialize(builder = ImmutableInputAccountContact.Builder.class)
public interface InputAccountContact extends BaseInputAccountContact {

}

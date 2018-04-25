package primrose.data.impl;

import lombok.Value;

@Value(staticConstructor = "of")
public class ContactEmailId {

  private final Long           contact;
  private final Long           email;
}

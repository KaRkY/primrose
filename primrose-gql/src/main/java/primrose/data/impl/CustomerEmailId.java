package primrose.data.impl;

import java.time.OffsetDateTime;

import lombok.Value;

@Value(staticConstructor = "of")
public class CustomerEmailId {

  private final Long           customer;
  private final Long           email;
  private final OffsetDateTime version;
}

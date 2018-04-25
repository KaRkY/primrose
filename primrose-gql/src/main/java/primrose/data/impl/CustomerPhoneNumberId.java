package primrose.data.impl;

import java.time.OffsetDateTime;

import lombok.Value;

@Value(staticConstructor = "of")
public class CustomerPhoneNumberId {

  private final Long           customer;
  private final Long           phoneNumber;
  private final OffsetDateTime version;
}

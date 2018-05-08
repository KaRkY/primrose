package primrose.data.impl;

import lombok.Value;

@Value(staticConstructor = "of")
public class ContactPhoneNumberId {

  private final Long contact;
  private final Long phoneNumber;
}

package primrose.data.impl;

import static primrose.jooq.Tables.EMAILS;
import static primrose.jooq.Tables.PHONE_NUMBERS;

import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import primrose.data.EmailAndPhoneNumberRepository;

@Component
public class EmailAndPhoneNumberRepositoryImpl implements EmailAndPhoneNumberRepository {

  private DSLContext create;

  public EmailAndPhoneNumberRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public long email(String value) {
    Long emailId = create
      .select(EMAILS.ID)
      .from(EMAILS)
      .where(EMAILS.EMAIL.eq(value))
      .fetchOne(0, Long.class);

    if (emailId == null) {
      emailId = create
        .insertInto(EMAILS)
        .columns(EMAILS.EMAIL)
        .values(value)
        .returning(EMAILS.ID)
        .fetchOne()
        .getId();
    }

    return emailId;
  }

  @Override
  public long phone(String value) {
    Long emailId = create
      .select(PHONE_NUMBERS.ID)
      .from(PHONE_NUMBERS)
      .where(PHONE_NUMBERS.PHONE_NUMBER.eq(value))
      .fetchOne(0, Long.class);

    if (emailId == null) {
      emailId = create
        .insertInto(PHONE_NUMBERS)
        .columns(PHONE_NUMBERS.PHONE_NUMBER)
        .values(value)
        .returning(PHONE_NUMBERS.ID)
        .fetchOne()
        .getId();
    }

    return emailId;
  }

}

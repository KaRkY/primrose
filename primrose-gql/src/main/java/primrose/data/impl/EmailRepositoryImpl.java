package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.EmailRepository;
import primrose.jooq.Tables;
import primrose.service.Email;

@Repository
public class EmailRepositoryImpl implements EmailRepository {
  private DSLContext create;

  public EmailRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public long save(String email) {
    Long id = create
      .select(Tables.EMAILS.ID)
      .from(Tables.EMAILS)
      .where(Tables.EMAILS.EMAIL.likeIgnoreCase(email))
      .fetchOne(0, Long.class);

    if (id == null) {
      id = create
        .insertInto(Tables.EMAILS)
        .columns(Tables.EMAILS.EMAIL)
        .values(email)
        .returning(Tables.EMAILS.ID)
        .fetchOne()
        .getId();
    }

    return id;
  }

  @Override
  public void assignToCustomer(long customerId, long emailId, String emailType, Boolean primary) {
    create
      .insertInto(Tables.CUSTOMER_EMAILS)
      .columns(
        Tables.CUSTOMER_EMAILS.CUSTOMER,
        Tables.CUSTOMER_EMAILS.EMAIL,
        Tables.CUSTOMER_EMAILS.EMAIL_TYPE,
        Tables.CUSTOMER_EMAILS.PRIM)
      .values(
        DSL.value(customerId),
        DSL.value(emailId),
        create
          .select(Tables.EMAIL_TYPES.ID)
          .from(Tables.EMAIL_TYPES)
          .where(Tables.EMAIL_TYPES.SLUG.eq(emailType))
          .asField(),
        DSL.value(primary != null ? primary : false))
      .execute();
  }

  @Override
  public void assignToContact(long contactId, long emailId, String emailType, Boolean primary) {
    create
      .insertInto(Tables.CONTACT_EMAILS)
      .columns(
        Tables.CONTACT_EMAILS.CONTACT,
        Tables.CONTACT_EMAILS.EMAIL,
        Tables.CONTACT_EMAILS.EMAIL_TYPE,
        Tables.CONTACT_EMAILS.PRIM)
      .values(
        DSL.value(contactId),
        DSL.value(emailId),
        create
          .select(Tables.EMAIL_TYPES.ID)
          .from(Tables.EMAIL_TYPES)
          .where(Tables.EMAIL_TYPES.SLUG.eq(emailType))
          .asField(),
        DSL.value(primary != null ? primary : false))
      .execute();
  }

  @Override
  public List<Email> customerEmails(long customerId) {
    return create
      .select(
        Tables.EMAIL_TYPES.SLUG,
        Tables.EMAILS.EMAIL,
        Tables.CUSTOMER_EMAILS.PRIM)
      .from(Tables.CUSTOMER_EMAILS)
      .leftJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL))
      .leftJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL_TYPE))
      .where(Tables.CUSTOMER_EMAILS.CUSTOMER.eq(customerId))
      .fetch()
      .map(record -> new Email(record.value1(), record.value2(), record.value3()));
  }

  @Override
  public List<Email> contactEmails(long contactId) {
    return create
      .select(
        Tables.EMAIL_TYPES.SLUG,
        Tables.EMAILS.EMAIL,
        Tables.CONTACT_EMAILS.PRIM)
      .from(Tables.CONTACT_EMAILS)
      .leftJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CONTACT_EMAILS.EMAIL))
      .leftJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CONTACT_EMAILS.EMAIL_TYPE))
      .where(Tables.CONTACT_EMAILS.CONTACT.eq(contactId))
      .fetch()
      .map(record -> new Email(record.value1(), record.value2(), record.value3()));
  }

}

package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.EmailRepository;
import primrose.jooq.Tables;
import primrose.service.EmailFullDisplay;

@Repository
public class EmailRepositoryImpl implements EmailRepository {
  private DSLContext create;

  public EmailRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public long save(String email) {
    return create
      .insertInto(Tables.EMAILS)
      .columns(Tables.EMAILS.EMAIL)
      .values(email)
      .returning(Tables.EMAILS.ID)
      .fetchOne()
      .getId();
  }

  @Override
  public Long get(String email) {
    return create
      .select(Tables.EMAILS.ID)
      .from(Tables.EMAILS)
      .where(Tables.EMAILS.EMAIL.likeIgnoreCase(email))
      .fetchOne(0, Long.class);
  }

  @Override
  public void assignToCustomer(long customerCodeId, long emailId, String emailType, Boolean primary) {
    create
      .insertInto(Tables.CUSTOMER_EMAILS)
      .columns(
        Tables.CUSTOMER_EMAILS.CUSTOMER,
        Tables.CUSTOMER_EMAILS.EMAIL,
        Tables.CUSTOMER_EMAILS.EMAIL_TYPE,
        Tables.CUSTOMER_EMAILS.PRIM)
      .values(
        DSL.value(customerCodeId),
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
  public List<EmailFullDisplay> customerEmails(long customerCodeId) {
    return create
      .select(
        Tables.EMAILS.ID,
        Tables.EMAIL_TYPES.SLUG,
        Tables.EMAILS.EMAIL,
        Tables.CUSTOMER_EMAILS.PRIM)
      .from(Tables.CUSTOMER_EMAILS)
      .innerJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL))
      .innerJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL_TYPE))
      .where(
        Tables.CUSTOMER_EMAILS.CUSTOMER.eq(customerCodeId),
        DSL.currentOffsetDateTime().between(Tables.CUSTOMER_EMAILS.VALID_FROM).and(DSL.isnull(Tables.CUSTOMER_EMAILS.VALID_TO, DSL.currentOffsetDateTime())))
      .fetch()
      .map(record -> new EmailFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
  }

  @Override
  public List<EmailFullDisplay> customerEmailsForUpdate(long customerCodeId) {
    return create
      .select(
        Tables.EMAILS.ID,
        Tables.EMAIL_TYPES.SLUG,
        Tables.EMAILS.EMAIL,
        Tables.CUSTOMER_EMAILS.PRIM)
      .from(Tables.CUSTOMER_EMAILS)
      .innerJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL))
      .innerJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL_TYPE))
      .where(
        Tables.CUSTOMER_EMAILS.CUSTOMER.eq(customerCodeId),
        DSL.currentOffsetDateTime().between(Tables.CUSTOMER_EMAILS.VALID_FROM).and(DSL.isnull(Tables.CUSTOMER_EMAILS.VALID_TO, DSL.currentOffsetDateTime())))
      .forUpdate()
      .fetch()
      .map(record -> new EmailFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
  }

  @Override
  public List<EmailFullDisplay> contactEmails(long contactId) {
    return create
      .select(
        Tables.EMAILS.ID,
        Tables.EMAIL_TYPES.SLUG,
        Tables.EMAILS.EMAIL,
        Tables.CONTACT_EMAILS.PRIM)
      .from(Tables.CONTACT_EMAILS)
      .innerJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CONTACT_EMAILS.EMAIL))
      .innerJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CONTACT_EMAILS.EMAIL_TYPE))
      .where(
        Tables.CONTACT_EMAILS.CONTACT.eq(contactId),
        DSL.currentOffsetDateTime().between(Tables.CONTACT_EMAILS.VALID_FROM).and(DSL.isnull(Tables.CONTACT_EMAILS.VALID_TO, DSL.currentOffsetDateTime())))
      .fetch()
      .map(record -> new EmailFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
  }

  @Override
  public List<EmailFullDisplay> contactEmailsForUpdate(long contactId) {
    return create
      .select(
        Tables.EMAILS.ID,
        Tables.EMAIL_TYPES.SLUG,
        Tables.EMAILS.EMAIL,
        Tables.CONTACT_EMAILS.PRIM)
      .from(Tables.CONTACT_EMAILS)
      .innerJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CONTACT_EMAILS.EMAIL))
      .innerJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CONTACT_EMAILS.EMAIL_TYPE))
      .where(
        Tables.CONTACT_EMAILS.CONTACT.eq(contactId),
        DSL.currentOffsetDateTime().between(Tables.CONTACT_EMAILS.VALID_FROM).and(DSL.isnull(Tables.CONTACT_EMAILS.VALID_TO, DSL.currentOffsetDateTime())))
      .forUpdate()
      .fetch()
      .map(record -> new EmailFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
  }

  @Override
  public boolean isAssignedToCustomer(long customerCodeId, long emailId) {
    return create
      .selectOne()
      .from(Tables.CUSTOMER_EMAILS)
      .where(
        Tables.CUSTOMER_EMAILS.CUSTOMER.eq(customerCodeId),
        Tables.CUSTOMER_EMAILS.EMAIL.eq(emailId),
        DSL.currentOffsetDateTime()
          .between(Tables.CUSTOMER_EMAILS.VALID_FROM)
          .and(DSL.isnull(Tables.CUSTOMER_EMAILS.VALID_TO, DSL.currentOffsetDateTime())))
      .fetchOne(0, Integer.class) != null;
  }

  @Override
  public boolean isAssignedToContact(long contactId, long emailId) {
    return create
      .selectOne()
      .from(Tables.CONTACT_EMAILS)
      .where(
        Tables.CONTACT_EMAILS.CONTACT.eq(contactId),
        Tables.CONTACT_EMAILS.EMAIL.eq(emailId),
        DSL.currentOffsetDateTime()
          .between(Tables.CONTACT_EMAILS.VALID_FROM)
          .and(DSL.isnull(Tables.CONTACT_EMAILS.VALID_TO, DSL.currentOffsetDateTime())))
      .fetchOne(0, Integer.class) != null;
  }

  @Override
  public void removeFromCustomer(long customerCodeId, long emailId) {
    create
      .update(Tables.CUSTOMER_EMAILS)
      .set(Tables.CUSTOMER_EMAILS.VALID_TO, DSL.currentOffsetDateTime())
      .where(
        Tables.CUSTOMER_EMAILS.CUSTOMER.eq(customerCodeId),
        Tables.CUSTOMER_EMAILS.EMAIL.eq(emailId),
        Tables.CUSTOMER_EMAILS.VALID_TO.isNull())
      .execute();
  }

  @Override
  public void removeFromContact(long contactId, long emailId) {
    create
      .update(Tables.CONTACT_EMAILS)
      .set(Tables.CONTACT_EMAILS.VALID_TO, DSL.currentOffsetDateTime())
      .where(
        Tables.CONTACT_EMAILS.CONTACT.eq(contactId),
        Tables.CONTACT_EMAILS.EMAIL.eq(emailId),
        Tables.CONTACT_EMAILS.VALID_TO.isNull())
      .execute();
  }

}

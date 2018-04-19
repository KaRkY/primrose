package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.ContactRepository;
import primrose.jooq.Tables;
import primrose.service.EmailFullDisplay;
import primrose.service.PhoneFullDisplay;
import primrose.service.Search;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;

@Repository
public class ContactRepositoryImpl implements ContactRepository {

  private DSLContext create;

  public ContactRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public long create(ContactCreate contact) {
    return create
      .insertInto(Tables.CONTACTS)
      .columns(
        Tables.CONTACTS.FULL_NAME,
        Tables.CONTACTS.DESCRIPTION)
      .values(
        DSL.value(contact.getFullName()),
        DSL.value(contact.getDescription()))
      .returning(Tables.CONTACTS.ID)
      .fetchOne()
      .getId();
  }

  @Override
  public List<ContactReducedDisplay> search(Search search) {
    int limit = search.getSize();
    int offset = search.getPage() * search.getSize();
    return create
      .select(
        Tables.CONTACTS.ID,
        Tables.CONTACTS.FULL_NAME,
        create
          .select(Tables.EMAILS.EMAIL)
          .from(Tables.CONTACT_EMAILS)
          .leftJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CONTACT_EMAILS.EMAIL))
          .where(Tables.CONTACT_EMAILS.CONTACT.eq(Tables.CONTACTS.ID))
          .orderBy(Tables.CONTACT_EMAILS.PRIM.desc(), Tables.CONTACT_EMAILS.VALID_FROM.asc())
          .limit(1)
          .<String>asField(),
        create
          .select(Tables.PHONE_NUMBERS.PHONE)
          .from(Tables.CONTACT_PHONE_NUMBERS)
          .leftJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CONTACT_PHONE_NUMBERS.PHONE))
          .where(Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(Tables.CONTACTS.ID))
          .orderBy(Tables.CONTACT_PHONE_NUMBERS.PRIM.desc(), Tables.CONTACT_PHONE_NUMBERS.VALID_FROM.asc())
          .limit(1)
          .<String>asField())
      .from(Tables.CONTACTS)
      .where(DSL.currentOffsetDateTime().between(Tables.CONTACTS.VALID_FROM).and(DSL.isnull(Tables.CONTACTS.VALID_TO, DSL.currentOffsetDateTime())))
      .limit(limit)
      .offset(offset)
      .fetch()
      .map(record -> new ContactReducedDisplay(
        record.value1(),
        record.value2(),
        record.value3(),
        record.value4()));
  }

  @Override
  public long count(Search search) {
    return create
      .selectCount()
      .from(Tables.CONTACTS)
      .where(DSL.currentOffsetDateTime().between(Tables.CONTACTS.VALID_FROM).and(DSL.isnull(Tables.CONTACTS.VALID_TO, DSL.currentOffsetDateTime())))
      .fetchOne()
      .value1();
  }

  @Override
  public ContactFullDisplay get(long contactId, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones) {
    return create
      .select(
        Tables.CONTACTS.ID,
        Tables.CONTACTS.FULL_NAME,
        Tables.CONTACTS.DESCRIPTION,
        Tables.CONTACTS.VALID_FROM,
        Tables.CONTACTS.VALID_TO)
      .from(Tables.CONTACTS)
      .where(Tables.CONTACTS.ID.eq(contactId))
      .fetchOne(record -> new ContactFullDisplay(
        record.value1(),
        record.value2(),
        record.value3(),
        emails,
        phones,
        record.value4(),
        record.value5()));
  }

  @Override
  public ContactFullDisplay getForUpdate(long contactId, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones) {
    return create
      .select(
        Tables.CONTACTS.ID,
        Tables.CONTACTS.FULL_NAME,
        Tables.CONTACTS.DESCRIPTION,
        Tables.CONTACTS.VALID_FROM,
        Tables.CONTACTS.VALID_TO)
      .from(Tables.CONTACTS)
      .where(
        Tables.CONTACTS.ID.eq(contactId),
        Tables.CONTACTS.VALID_TO.isNull())
      .forUpdate()
      .fetchOne(record -> new ContactFullDisplay(
        record.value1(),
        record.value2(),
        record.value3(),
        emails,
        phones,
        record.value4(),
        record.value5()));
  }

  @Override
  public void deactivate(long contactId) {
    create
      .update(Tables.CONTACTS)
      .set(Tables.CONTACTS.VALID_TO, DSL.currentOffsetDateTime())
      .where(
        Tables.CONTACTS.ID.eq(contactId),
        Tables.CONTACTS.VALID_TO.isNull())
      .execute();
  }

}

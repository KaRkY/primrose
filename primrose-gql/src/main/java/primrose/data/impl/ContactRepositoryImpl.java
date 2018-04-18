package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.ContactRepository;
import primrose.jooq.Tables;
import primrose.service.Email;
import primrose.service.Phone;
import primrose.service.Search;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactSearch;

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
  public List<ContactSearch> search(Search search) {
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
      .limit(limit)
      .offset(offset)
      .fetch()
      .map(record -> new ContactSearch(
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
      .fetchOne()
      .value1();
  }

  @Override
  public Contact get(long contactId, List<Email> emails, List<Phone> phones) {
    return create
      .select(
        Tables.CONTACTS.ID,
        Tables.CONTACTS.FULL_NAME,
        Tables.CONTACTS.DESCRIPTION,
        Tables.CONTACTS.VALID_FROM,
        Tables.CONTACTS.VALID_TO)
      .from(Tables.CONTACTS)
      .where(Tables.CONTACTS.ID.eq(contactId))
      .fetchOne(record -> new Contact(
        record.value1(),
        record.value2(),
        record.value3(),
        emails,
        phones,
        record.value4(),
        record.value5()));
  }

}

package primrose.contacts;

import static org.jooq.impl.DSL.value;
import static pimrose.jooq.Primrose.PRIMROSE;
import static pimrose.jooq.Sequences.CONTACTS_SEQ;

import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class ContactsRepository {

  private final DSLContext create;

  public ContactsRepository(final DSLContext create) {
    this.create = create;
  }

  public long nextValAddresses() {
    return create
      .select(CONTACTS_SEQ.nextval())
      .fetchOne()
      .value1();
  }

  public void insert(final long contactId, final Contact contact, final String user) {
    create
      .insertInto(PRIMROSE.CONTACTS)
      .columns(
        PRIMROSE.CONTACTS.ID,
        PRIMROSE.CONTACTS.NAME,
        PRIMROSE.CONTACTS.EMAIL,
        PRIMROSE.CONTACTS.PHONE,
        PRIMROSE.CONTACTS.CREATED_BY)
      .values(
        value(contactId),
        value(contact.name()),
        value(contact.email()),
        value(contact.phone()),
        create
          .select(PRIMROSE.PRINCIPALS.ID)
          .from(PRIMROSE.PRINCIPALS)
          .where(PRIMROSE.PRINCIPALS.NAME.eq(user))
          .asField())
      .execute();
  }

  public void insert(final long accountId, final long contactId, final String contactType, final String user) {
    create
      .insertInto(PRIMROSE.ACCOUNT_CONTACTS)
      .columns(
        PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT,
        PRIMROSE.ACCOUNT_CONTACTS.CONTACT,
        PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT_CONTACT_TYPE,
        PRIMROSE.ACCOUNT_CONTACTS.CREATED_BY)
      .values(
        value(accountId),
        value(contactId),
        create
          .select(PRIMROSE.ACCOUNT_CONTACT_TYPES.ID)
          .from(PRIMROSE.ACCOUNT_CONTACT_TYPES)
          .where(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME.eq(contactType))
          .asField(),
        create
          .select(PRIMROSE.PRINCIPALS.ID)
          .from(PRIMROSE.PRINCIPALS)
          .where(PRIMROSE.PRINCIPALS.NAME.eq(user))
          .asField())
      .execute();
  }

  public Contact loadById(final long contactId) {
    return create
      .select(
        PRIMROSE.CONTACTS.ID,
        PRIMROSE.CONTACTS.NAME,
        PRIMROSE.CONTACTS.EMAIL,
        PRIMROSE.CONTACTS.PHONE)
      .from(PRIMROSE.CONTACTS)
      .where(PRIMROSE.CONTACTS.ID.eq(contactId))
      .fetchOptional(record -> ImmutableContact
        .builder()
        .id(record.getValue(PRIMROSE.CONTACTS.ID))
        .name(record.getValue(PRIMROSE.CONTACTS.NAME))
        .email(record.getValue(PRIMROSE.CONTACTS.EMAIL))
        .phone(record.getValue(PRIMROSE.CONTACTS.PHONE))
        .build())
      .orElseThrow(() -> new NoDataFoundException("No data"));
  }

  public Contact loadByName(final String contactName) {
    return create
      .select(
        PRIMROSE.CONTACTS.ID,
        PRIMROSE.CONTACTS.NAME,
        PRIMROSE.CONTACTS.EMAIL,
        PRIMROSE.CONTACTS.PHONE)
      .from(PRIMROSE.CONTACTS)
      .where(PRIMROSE.CONTACTS.NAME.eq(contactName))
      .fetchOptional(record -> ImmutableContact
        .builder()
        .id(record.getValue(PRIMROSE.CONTACTS.ID))
        .name(record.getValue(PRIMROSE.CONTACTS.NAME))
        .email(record.getValue(PRIMROSE.CONTACTS.EMAIL))
        .phone(record.getValue(PRIMROSE.CONTACTS.PHONE))
        .build())
      .orElseThrow(() -> new NoDataFoundException("No data"));
  }

  public Map<String, List<Contact>> loadByAccountId(final Long accountId) {
    return create
      .select(
        PRIMROSE.CONTACTS.ID,
        PRIMROSE.CONTACTS.NAME,
        PRIMROSE.CONTACTS.EMAIL,
        PRIMROSE.CONTACTS.PHONE,
        PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME)
      .from(PRIMROSE.CONTACTS)
      .join(PRIMROSE.ACCOUNT_CONTACTS).on(PRIMROSE.ACCOUNT_CONTACTS.CONTACT.eq(PRIMROSE.CONTACTS.ID))
      .join(PRIMROSE.ACCOUNT_CONTACT_TYPES)
      .on(PRIMROSE.ACCOUNT_CONTACT_TYPES.ID.eq(PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT_CONTACT_TYPE))
      .where(PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT.eq(accountId))
      .fetchGroups(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME, record -> ImmutableContact
        .builder()
        .id(record.getValue(PRIMROSE.CONTACTS.ID))
        .name(record.getValue(PRIMROSE.CONTACTS.NAME))
        .email(record.getValue(PRIMROSE.CONTACTS.EMAIL))
        .phone(record.getValue(PRIMROSE.CONTACTS.PHONE))
        .build());
  }

}

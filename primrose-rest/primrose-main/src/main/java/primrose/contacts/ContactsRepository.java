package primrose.contacts;

import static org.jooq.impl.DSL.value;
import static pimrose.jooq.Primrose.PRIMROSE;
import static pimrose.jooq.Sequences.CONTACTS_SEQ;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import primrose.util.IdUtil;

@Repository
public class ContactsRepository {

  private final DSLContext create;

  public ContactsRepository(final DSLContext create) {
    this.create = create;
  }

  public String nextValContact() {
    return IdUtil.toStringId(create
      .select(CONTACTS_SEQ.nextval())
      .fetchOne()
      .value1());
  }

  public void insert(final Contact contact, final String user) {
    create
      .insertInto(PRIMROSE.CONTACTS)
      .columns(
        PRIMROSE.CONTACTS.ID,
        PRIMROSE.CONTACTS.NAME,
        PRIMROSE.CONTACTS.EMAIL,
        PRIMROSE.CONTACTS.PHONE,
        PRIMROSE.CONTACTS.CREATED_BY)
      .values(
        value(IdUtil.valueOfLongId(contact.id())),
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

  public Optional<Contact> loadById(final String contactId) {
    return create
      .select(
        PRIMROSE.CONTACTS.ID,
        PRIMROSE.CONTACTS.NAME,
        PRIMROSE.CONTACTS.EMAIL,
        PRIMROSE.CONTACTS.PHONE)
      .from(PRIMROSE.CONTACTS)
      .where(PRIMROSE.CONTACTS.ID.eq(IdUtil.valueOfLongId(contactId)))
      .fetchOptional(record -> ImmutableContact
      .builder()
      .id(IdUtil.toStringId(record.getValue(PRIMROSE.CONTACTS.ID)))
      .name(record.getValue(PRIMROSE.CONTACTS.NAME))
      .email(record.getValue(PRIMROSE.CONTACTS.EMAIL))
      .phone(record.getValue(PRIMROSE.CONTACTS.PHONE))
      .build());
  }

  public Optional<Contact> loadById(final String accountId, final String type, final String contactId) {
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
      .where(
        PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT.eq(IdUtil.valueOfLongId(accountId)),
        PRIMROSE.ACCOUNT_ADDRESS_TYPES.NAME.eq(type),
        PRIMROSE.CONTACTS.ID.eq(IdUtil.valueOfLongId(contactId)))
      .fetchOptional(record -> ImmutableContact
      .builder()
      .id(IdUtil.toStringId(record.getValue(PRIMROSE.CONTACTS.ID)))
      .name(record.getValue(PRIMROSE.CONTACTS.NAME))
      .email(record.getValue(PRIMROSE.CONTACTS.EMAIL))
      .phone(record.getValue(PRIMROSE.CONTACTS.PHONE))
      .build());
  }

  public Optional<Contact> loadByName(final String contactName) {
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
      .id(IdUtil.toStringId(record.getValue(PRIMROSE.CONTACTS.ID)))
      .name(record.getValue(PRIMROSE.CONTACTS.NAME))
      .email(record.getValue(PRIMROSE.CONTACTS.EMAIL))
      .phone(record.getValue(PRIMROSE.CONTACTS.PHONE))
      .build());
  }

  public Map<String, List<Contact>> loadByAccountId(final String accountId) {
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
      .where(PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT.eq(IdUtil.valueOfLongId(accountId)))
      .fetchGroups(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME, record -> ImmutableContact
      .builder()
      .id(IdUtil.toStringId(record.getValue(PRIMROSE.CONTACTS.ID)))
      .name(record.getValue(PRIMROSE.CONTACTS.NAME))
      .email(record.getValue(PRIMROSE.CONTACTS.EMAIL))
      .phone(record.getValue(PRIMROSE.CONTACTS.PHONE))
      .build());
  }

}

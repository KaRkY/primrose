package primrose.contacts;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import pimrose.jooq.DefaultCatalog;
import pimrose.jooq.Primrose;
import pimrose.jooq.Sequences;
import pimrose.jooq.tables.TAccountContactTypes;
import pimrose.jooq.tables.TAccountContacts;
import pimrose.jooq.tables.TAccounts;
import pimrose.jooq.tables.TContacts;

@Repository
public class ContactsRepository {
  private static final Primrose PRIMROSE = DefaultCatalog.DEFAULT_CATALOG.PRIMROSE;
  private static final TContacts CONTACT = PRIMROSE.T_CONTACTS.as("contact");
  private static final TAccounts ACCOUNT = PRIMROSE.T_ACCOUNTS.as("account");
  private static final TAccountContacts ACCOUNT_CONTACT = PRIMROSE.T_ACCOUNT_CONTACTS.as("account_contact");
  private static final TAccountContactTypes ACCOUNT_CONTACT_TYPE = PRIMROSE.T_ACCOUNT_CONTACT_TYPES
    .as("account_contact_title");
  private final DSLContext create;

  public ContactsRepository(final DSLContext create) {
    this.create = create;
  }

  public long getNewId() {
    return create.select(Sequences.S_CONTACT.nextval()).fetchOne().value1();
  }

  public long getNewContactTypeId() {
    return create.select(Sequences.S_CONTACT_TYPES.nextval()).fetchOne().value1();
  }

  public void insert(final long contactId, final Contact contact, final long addressId) {
    create
      .insertInto(PRIMROSE.T_CONTACTS)
      .columns(
        PRIMROSE.T_CONTACTS.CONTACT_ID,
        PRIMROSE.T_CONTACTS.PERSON_NAME,
        PRIMROSE.T_CONTACTS.EMAIL,
        PRIMROSE.T_CONTACTS.PHONE,
        PRIMROSE.T_CONTACTS.ADDRESS_ID)
      .values(
        DSL.value(id),
        DSL.value(contact.personName()),
        DSL.value(contact.email()),
        DSL.value(contact.phone()),
        DSL.value(addressId))
      .execute();
  }

  public void insert(final String contactType, final long contactId, final long accountId) {
    create
      .insertInto(PRIMROSE.T_ACCOUNT_CONTACTS)
      .columns(
        PRIMROSE.T_ACCOUNT_CONTACTS.ACCOUNT_ID,
        PRIMROSE.T_ACCOUNT_CONTACTS.CONTACT_ID,
        PRIMROSE.T_ACCOUNT_CONTACTS.ACCOUNT_CONTACT_TYPE_ID)
      .values(
        DSL.value(accountId),
        DSL.value(contactId),
        create.select(PRIMROSE.T_ACCOUNT_CONTACT_TYPES.ACCOUNT_CONTACT_TYPE_ID)
          .from(PRIMROSE.T_ACCOUNT_CONTACT_TYPES)
          .where(PRIMROSE.T_ACCOUNT_CONTACT_TYPES.ACCOUNT_CONTACT_TYPE_CODE.eq(DSL.value(contactType)))
          .asField())
      .execute();
  }

  public void insert(final String contactTitle, final long contactId, final String accountCode) {
    create
      .insertInto(PRIMROSE.T_ACCOUNT_CONTACTS)
      .columns(
        PRIMROSE.T_ACCOUNT_CONTACTS.ACCOUNT_ID,
        PRIMROSE.T_ACCOUNT_CONTACTS.CONTACT_ID,
        PRIMROSE.T_ACCOUNT_CONTACTS.ACCOUNT_CONTACT_TYPE_ID)
      .values(
        create.select(PRIMROSE.T_ACCOUNTS.ACCOUNT_ID)
          .from(PRIMROSE.T_ACCOUNTS)
          .where(PRIMROSE.T_ACCOUNTS.ACCOUNT_CODE.eq(DSL.value(accountCode)))
          .asField(),
        DSL.value(contactId),
        create.select(PRIMROSE.T_ACCOUNT_CONTACT_TYPES.ACCOUNT_CONTACT_TYPE_ID)
          .from(PRIMROSE.T_ACCOUNT_CONTACT_TYPES)
          .where(PRIMROSE.T_ACCOUNT_CONTACT_TYPES.ACCOUNT_CONTACT_TYPE_CODE.eq(DSL.value(contactTitle)))
          .asField())
      .execute();
  }

  public void insert(final long contactTitleId, final String contactType) {
    create
      .insertInto(PRIMROSE.T_ACCOUNT_CONTACT_TYPES)
      .columns(
        PRIMROSE.T_ACCOUNT_CONTACT_TYPES.ACCOUNT_CONTACT_TYPE_ID,
        PRIMROSE.T_ACCOUNT_CONTACT_TYPES.ACCOUNT_CONTACT_TYPE_CODE)
      .values(
        DSL.value(contactTitleId),
        DSL.value(contactType))
      .execute();
  }

  public Optional<Long> getContactType(final String contactType) {
    return create
      .select(PRIMROSE.T_ACCOUNT_CONTACT_TYPES.ACCOUNT_CONTACT_TYPE_ID)
      .from(PRIMROSE.T_ACCOUNT_CONTACT_TYPES)
      .where(DSL.upper(PRIMROSE.T_ACCOUNT_CONTACT_TYPES.ACCOUNT_CONTACT_TYPE_CODE).eq(DSL.upper(contactType)))
      .fetchOptional(PRIMROSE.T_ACCOUNT_CONTACT_TYPES.ACCOUNT_CONTACT_TYPE_ID);
  }

  public Multimap<String, Contact> getByAccountId(final long accountId) {
    return create
      .select(
        CONTACT.CONTACT_CODE,
        CONTACT.PERSON_NAME,
        CONTACT.EMAIL,
        CONTACT.PHONE,
        ACCOUNT_CONTACT_TYPE.ACCOUNT_CONTACT_TYPE_CODE)
      .from(CONTACT)
      .leftJoin(ACCOUNT_CONTACT).on(ACCOUNT_CONTACT.CONTACT_ID.eq(CONTACT.CONTACT_ID))
      .leftJoin(ACCOUNT_CONTACT_TYPE)
      .on(ACCOUNT_CONTACT_TYPE.ACCOUNT_CONTACT_TYPE_ID.eq(ACCOUNT_CONTACT.ACCOUNT_CONTACT_TYPE_ID))
      .where(ACCOUNT_CONTACT.ACCOUNT_ID.eq(DSL.val(accountId)))
      .fetch()
      .stream()
      .collect(Collector.of(
        HashMultimap::create,
        (map, row) -> map.put(row.get(ACCOUNT_CONTACT_TYPE.ACCOUNT_CONTACT_TYPE_CODE), map(row)),
        (map1, map2) -> {
          map1.putAll(map2);
          return map1;
        }));
  }

  public Multimap<String, Contact> getByAccountCode(final String accountCode) {
    return create
      .select(
        CONTACT.CONTACT_CODE,
        CONTACT.PERSON_NAME,
        CONTACT.EMAIL,
        CONTACT.PHONE,
        ACCOUNT_CONTACT_TYPE.ACCOUNT_CONTACT_TYPE_CODE)
      .from(CONTACT)
      .leftJoin(ACCOUNT_CONTACT).on(ACCOUNT_CONTACT.CONTACT_ID.eq(CONTACT.CONTACT_ID))
      .leftJoin(ACCOUNT).on(ACCOUNT.ACCOUNT_ID.eq(ACCOUNT_CONTACT.ACCOUNT_ID))
      .leftJoin(ACCOUNT_CONTACT_TYPE)
      .on(ACCOUNT_CONTACT_TYPE.ACCOUNT_CONTACT_TYPE_ID.eq(ACCOUNT_CONTACT.ACCOUNT_CONTACT_TYPE_ID))
      .where(ACCOUNT.ACCOUNT_CODE.eq(DSL.value(accountCode)))
      .fetch()
      .stream()
      .collect(Collector.of(
        HashMultimap::create,
        (map, row) -> map.put(row.get(ACCOUNT_CONTACT_TYPE.ACCOUNT_CONTACT_TYPE_CODE), map(row)),
        (map1, map2) -> {
          map1.putAll(map2);
          return map1;
        }));
  }

  public List<ContactType> getTypes() {
    return create
      .select(ACCOUNT_CONTACT_TYPE.ACCOUNT_CONTACT_TYPE_CODE)
      .from(ACCOUNT_CONTACT_TYPE)
      .fetch(record -> ImmutableContactType
        .builder()
        .code(record.get(ACCOUNT_CONTACT_TYPE.ACCOUNT_CONTACT_TYPE_CODE))
        .def(record.get(ACCOUNT_CONTACT_TYPE.ACCOUNT_CONTACT_TYPE_DEFAULT))
        .build());
  }

  private Contact map(final Record record) {
    return ImmutableContact.builder()
      .code(record.getValue(PRIMROSE.T_CONTACTS.CONTACT_CODE))
      .personName(record.getValue(PRIMROSE.T_CONTACTS.PERSON_NAME))
      .email(record.getValue(PRIMROSE.T_CONTACTS.EMAIL))
      .phone(record.getValue(PRIMROSE.T_CONTACTS.PHONE))
      .build();
  }

}

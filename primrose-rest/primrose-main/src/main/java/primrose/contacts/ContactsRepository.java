package primrose.contacts;

import static org.jooq.impl.DSL.currentLocalDateTime;
import static org.jooq.impl.DSL.value;
import static pimrose.jooq.Primrose.PRIMROSE;
import static pimrose.jooq.Sequences.CONTACTS_SEQ;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import primrose.model.BaseInputContact;
import primrose.model.BaseOutputAccountContact;
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

  public void insert(
    final String contactId,
    final BaseInputContact contact,
    final String user) {
    create
      .insertInto(PRIMROSE.CONTACTS)
      .columns(
        PRIMROSE.CONTACTS.ID,
        PRIMROSE.CONTACTS.NAME,
        PRIMROSE.CONTACTS.EMAIL,
        PRIMROSE.CONTACTS.PHONE,
        PRIMROSE.CONTACTS.CREATED_BY)
      .values(
        value(IdUtil.valueOfLongId(contactId)),
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

  public void update(
    final String contactId,
    final BaseInputContact contact,
    final String user) {
    create
      .update(PRIMROSE.CONTACTS)
      .set(PRIMROSE.CONTACTS.NAME, contact.name())
      .set(PRIMROSE.CONTACTS.EMAIL, contact.email())
      .set(PRIMROSE.CONTACTS.PHONE, contact.phone())
      .set(PRIMROSE.CONTACTS.EDITED_BY, create
        .select(PRIMROSE.PRINCIPALS.ID)
        .from(PRIMROSE.PRINCIPALS)
        .where(PRIMROSE.PRINCIPALS.NAME.eq(user))
        .asField())
      .set(PRIMROSE.CONTACTS.EDITED_AT, currentLocalDateTime())
      .where(PRIMROSE.CONTACTS.ID.eq(IdUtil.valueOfLongId(contactId)))
      .execute();
  }

  public List<List<BaseOutputAccountContact>> loadByAccountId(final List<String> accountId) {
    final Map<Long, List<BaseOutputAccountContact>> groupedContacts = create
      .select(
        PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT,
        PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME,
        PRIMROSE.CONTACTS.ID,
        PRIMROSE.CONTACTS.NAME,
        PRIMROSE.CONTACTS.EMAIL,
        PRIMROSE.CONTACTS.PHONE)
      .from(PRIMROSE.CONTACTS)
      .join(PRIMROSE.ACCOUNT_CONTACTS).on(PRIMROSE.ACCOUNT_CONTACTS.CONTACT.eq(PRIMROSE.CONTACTS.ID))
      .join(PRIMROSE.ACCOUNT_CONTACT_TYPES)
      .on(PRIMROSE.ACCOUNT_CONTACT_TYPES.ID.eq(PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT_CONTACT_TYPE))
      .where(PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT
        .in(accountId.stream().map(IdUtil::valueOfLongId).collect(Collectors.toList())))
      .fetchGroups(PRIMROSE.ACCOUNT_CONTACTS.ACCOUNT, record -> ImmutableOutputAccountContact.builder()
        .id(IdUtil.toStringId(record.getValue(PRIMROSE.CONTACTS.ID)))
        .type(record.getValue(PRIMROSE.ACCOUNT_CONTACT_TYPES.NAME))
        .name(record.getValue(PRIMROSE.CONTACTS.NAME))
        .email(record.getValue(PRIMROSE.CONTACTS.EMAIL))
        .phone(record.getValue(PRIMROSE.CONTACTS.PHONE))
        .build());

    return accountId
      .stream()
      .map(IdUtil::valueOfLongId)
      .map(groupedContacts::get)
      .map(contacts -> contacts != null ? contacts : Collections.<BaseOutputAccountContact>emptyList())
      .collect(Collectors.toList());
  }

  public int count() {
    return create
      .fetchCount(PRIMROSE.CONTACTS);
  }

}

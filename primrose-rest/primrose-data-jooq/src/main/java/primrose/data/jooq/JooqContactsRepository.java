package primrose.data.jooq;

import static org.jooq.impl.DSL.value;
import static pimrose.data.jooq.Primrose.PRIMROSE;
import static pimrose.data.jooq.Sequences.CONTACTS_SEQ;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import primrose.data.ContactsRepository;
import primrose.model.input.BaseInputContact;
import primrose.model.output.BaseOutputAccountContact;
import primrose.util.IdUtil;

@Repository
public class JooqContactsRepository implements ContactsRepository {

  private final DSLContext create;

  public JooqContactsRepository(final DSLContext create) {
    this.create = create;
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.ContactsRepository#count()
   */
  @Override
  public int count() {
    return create
      .fetchCount(PRIMROSE.CONTACTS);
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.ContactsRepository#insert(java.lang.String,
   * primrose.model.input.BaseInputContact, java.lang.String)
   */
  @Override
  public void insert(final String contactId, final BaseInputContact contact) {
    create
      .insertInto(PRIMROSE.CONTACTS)
      .columns(
        PRIMROSE.CONTACTS.ID,
        PRIMROSE.CONTACTS.NAME,
        PRIMROSE.CONTACTS.EMAIL,
        PRIMROSE.CONTACTS.PHONE)
      .values(
        value(IdUtil.valueOfLongId(contactId)),
        value(contact.name()),
        value(contact.email()),
        value(contact.phone()))
      .execute();
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.ContactsRepository#loadByAccountId(java.util.List)
   */
  @Override
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

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.ContactsRepository#nextValContact()
   */
  @Override
  public String nextValContact() {
    return IdUtil.toStringId(create
      .select(CONTACTS_SEQ.nextval())
      .fetchOne()
      .value1());
  }

  /*
   * (non-Javadoc)
   *
   * @see primrose.data.jooq.ContactsRepository#update(java.lang.String,
   * primrose.model.input.BaseInputContact, java.lang.String)
   */
  @Override
  public void update(final String contactId, final BaseInputContact contact) {
    create
      .update(PRIMROSE.CONTACTS)
      .set(PRIMROSE.CONTACTS.NAME, contact.name())
      .set(PRIMROSE.CONTACTS.EMAIL, contact.email())
      .set(PRIMROSE.CONTACTS.PHONE, contact.phone())
      .where(PRIMROSE.CONTACTS.ID.eq(IdUtil.valueOfLongId(contactId)))
      .execute();
  }

}

package primrose.data.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;

import primrose.data.ContactRepository;
import primrose.jooq.JooqUtil;
import primrose.jooq.Tables;
import primrose.jooq.tables.records.ContactsRecord;
import primrose.service.CreateEmail;
import primrose.service.CreatePhone;
import primrose.service.EmailFullDisplay;
import primrose.service.Pagination;
import primrose.service.PhoneFullDisplay;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactEdit;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;

@Repository
public class ContactRepositoryImpl implements ContactRepository {

  private DSLContext create;

  public ContactRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public String create(ContactCreate contact) {
    ContactsRecord contactRecord = create
      .insertInto(Tables.CONTACTS)
      .columns(
        Tables.CONTACTS.FULL_NAME,
        Tables.CONTACTS.DESCRIPTION)
      .values(
        DSL.value(contact.getFullName()),
        DSL.value(contact.getDescription()))
      .returning(
        Tables.CONTACTS.ID,
        Tables.CONTACTS.CODE)
      .fetchOne();

    contact.getEmails().forEach(email -> {
      assignEmail(contactRecord.getId(), email);
    });

    contact.getPhones().forEach(phone -> {
      assignPhone(contactRecord.getId(), phone);
    });

    return contactRecord.getCode();
  }

  @Override
  public void update(String code, ContactEdit contact) {
    Long contactId = create
      .select(Tables.CONTACTS.ID)
      .from(Tables.CONTACTS)
      .where(Tables.CONTACTS.CODE.eq(code))
      .fetchOne(Tables.CONTACTS.ID);

    create
      .update(Tables.CONTACTS)
      .set(Tables.CONTACTS.FULL_NAME, contact.getFullName())
      .set(Tables.CONTACTS.DESCRIPTION, contact.getDescription())
      .where(
        Tables.CONTACTS.ID.eq(contactId),
        Tables.CONTACTS.MODIFIED_AT.eq(contact.getVersion()));

    Set<Long> assignedEmails = new HashSet<>();
    contact.getEmails().forEach(email -> {
      Long id = assignedEmail(contactId, email);
      if (id == null) {
        id = assignEmail(contactId, email);
      }
      assignedEmails.add(id);
    });
    removeEmailsExcept(contactId, assignedEmails);

    Set<Long> assignedPhones = new HashSet<>();
    contact.getPhones().forEach(phone -> {
      Long id = assignedPhone(contactId, phone);
      if (id == null) {
        id = assignPhone(contactId, phone);
      }
      assignedPhones.add(id);
    });
    removePhoneNumbersExcept(contactId, assignedPhones);
  }

  private long assignEmail(long contactId, CreateEmail email) {
    return create
      .insertInto(Tables.CONTACT_EMAILS)
      .columns(
        Tables.CONTACT_EMAILS.CONTACT,
        Tables.CONTACT_EMAILS.EMAIL,
        Tables.CONTACT_EMAILS.EMAIL_TYPE,
        Tables.CONTACT_EMAILS.PRIM)
      .values(
        DSL.value(contactId),
        DSL.value(email.getValue()),
        create
          .select(Tables.EMAIL_TYPES.ID)
          .from(Tables.EMAIL_TYPES)
          .where(Tables.EMAIL_TYPES.SLUG.eq(email.getType()))
          .asField(),
        DSL.value(email.getPrimary() != null ? email.getPrimary() : false))
      .returning(Tables.CONTACT_EMAILS.ID)
      .fetchOne()
      .get(Tables.CONTACT_EMAILS.ID);
  }

  private Long assignedEmail(long contactId, CreateEmail email) {
    return create
      .select(Tables.CONTACT_EMAILS.ID)
      .from(Tables.CONTACT_EMAILS)
      .where(
        Tables.CONTACT_EMAILS.CONTACT.eq(contactId),
        Tables.CONTACT_EMAILS.EMAIL_TYPE.eq(
          create
            .select(Tables.EMAIL_TYPES.ID)
            .from(Tables.EMAIL_TYPES)
            .where(Tables.EMAIL_TYPES.SLUG.eq(email.getType()))
            .asField()),
        Tables.CONTACT_EMAILS.EMAIL.likeIgnoreCase(email.getValue()))
      .forUpdate()
      .fetchOne(Tables.CONTACT_EMAILS.ID);
  }

  private ImmutableList<EmailFullDisplay> assignedEmail(long contact) {
    return create
      .select(
        Tables.EMAIL_TYPES.SLUG,
        Tables.CONTACT_EMAILS.EMAIL,
        Tables.CONTACT_EMAILS.PRIM)
      .from(Tables.CONTACT_EMAILS)
      .innerJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CONTACT_EMAILS.EMAIL_TYPE))
      .where(Tables.CONTACT_EMAILS.CONTACT.eq(contact))
      .fetch()
      .stream()
      .map(record -> EmailFullDisplay.builder()
        .type(record.get(Tables.EMAIL_TYPES.SLUG))
        .value(record.get(Tables.CONTACT_EMAILS.EMAIL))
        .primary(record.get(Tables.CONTACT_EMAILS.PRIM))
        .build())
      .collect(ImmutableList.toImmutableList());
  }

  public void removeEmailsExcept(long contactId, Set<Long> emailIds) {
    create
      .deleteFrom(Tables.CONTACT_EMAILS)
      .where(
        Tables.CONTACT_EMAILS.CONTACT.eq(contactId),
        Tables.CONTACT_EMAILS.ID.notIn(emailIds))
      .execute();
  }

  private long assignPhone(long contactId, CreatePhone phone) {
    return create
      .insertInto(Tables.CONTACT_PHONE_NUMBERS)
      .columns(
        Tables.CONTACT_PHONE_NUMBERS.CONTACT,
        Tables.CONTACT_PHONE_NUMBERS.PHONE,
        Tables.CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE,
        Tables.CONTACT_PHONE_NUMBERS.PRIM)
      .values(
        DSL.value(contactId),
        DSL.value(phone.getValue()),
        create
          .select(Tables.PHONE_NUMBER_TYPES.ID)
          .from(Tables.PHONE_NUMBER_TYPES)
          .where(Tables.PHONE_NUMBER_TYPES.SLUG.eq(phone.getType()))
          .asField(),
        DSL.value(phone.getPrimary() != null ? phone.getPrimary() : false))
      .returning(Tables.CONTACT_PHONE_NUMBERS.ID)
      .fetchOne()
      .get(Tables.CONTACT_PHONE_NUMBERS.ID);
  }

  private Long assignedPhone(long contactId, CreatePhone phone) {
    return create
      .select(Tables.CONTACT_PHONE_NUMBERS.ID)
      .from(Tables.CONTACT_PHONE_NUMBERS)
      .where(
        Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId),
        Tables.CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE.eq(
          create
            .select(Tables.PHONE_NUMBER_TYPES.ID)
            .from(Tables.PHONE_NUMBER_TYPES)
            .where(Tables.PHONE_NUMBER_TYPES.SLUG.eq(phone.getType()))
            .asField()),
        Tables.CONTACT_PHONE_NUMBERS.PHONE.likeIgnoreCase(phone.getValue()))
      .forUpdate()
      .fetchOne(Tables.CONTACT_PHONE_NUMBERS.ID);
  }

  private ImmutableList<PhoneFullDisplay> assignedPhone(long contact) {
    return create
      .select(
        Tables.PHONE_NUMBER_TYPES.SLUG,
        Tables.CONTACT_PHONE_NUMBERS.PHONE,
        Tables.CONTACT_PHONE_NUMBERS.PRIM)
      .from(Tables.CONTACT_PHONE_NUMBERS)
      .innerJoin(Tables.PHONE_NUMBER_TYPES).on(Tables.PHONE_NUMBER_TYPES.ID.eq(Tables.CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(contact))
      .fetch()
      .stream()
      .map(record -> PhoneFullDisplay.builder()
        .type(record.get(Tables.PHONE_NUMBER_TYPES.SLUG))
        .value(record.get(Tables.CONTACT_PHONE_NUMBERS.PHONE))
        .primary(record.get(Tables.CONTACT_PHONE_NUMBERS.PRIM))
        .build())
      .collect(ImmutableList.toImmutableList());
  }

  private void removePhoneNumbersExcept(long contactId, Set<Long> phoneIds) {
    create
      .deleteFrom(Tables.CONTACT_PHONE_NUMBERS)
      .where(
        Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId),
        Tables.CONTACT_PHONE_NUMBERS.ID.notIn(phoneIds))
      .execute();
  }

  @Override
  public ImmutableList<ContactReducedDisplay> list(Pagination pagination) {
    int limit = pagination.getSize();
    int offset = pagination.getPage() * pagination.getSize();
    Condition searchCondition = buildSearchCondition(pagination);

    Field<String> primaryEmail = create
      .select(Tables.CONTACT_EMAILS.EMAIL)
      .from(Tables.CONTACT_EMAILS)
      .where(Tables.CONTACT_EMAILS.CONTACT.eq(Tables.CONTACTS.ID))
      .orderBy(Tables.CONTACT_EMAILS.PRIM.desc())
      .limit(1)
      .<String>asField("primaryEmail");

    Field<String> primaryPhone = create
      .select(Tables.CONTACT_PHONE_NUMBERS.PHONE)
      .from(Tables.CONTACT_PHONE_NUMBERS)
      .where(Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(Tables.CONTACTS.ID))
      .orderBy(Tables.CONTACT_PHONE_NUMBERS.PRIM.desc())
      .limit(1)
      .<String>asField("primaryPhone");

    return create
      .select(
        Tables.CONTACTS.CODE,
        Tables.CONTACTS.FULL_NAME,
        primaryEmail,
        primaryPhone)
      .from(Tables.CONTACTS)
      .where(searchCondition)
      .limit(limit)
      .offset(offset)
      .fetch()
      .stream()
      .map(record -> ContactReducedDisplay.builder()
        .code(record.get(Tables.CONTACTS.CODE))
        .fullName(record.get(Tables.CONTACTS.FULL_NAME))
        .primaryEmail(record.get(primaryEmail))
        .primaryPhone(record.get(primaryPhone))
        .build())
      .collect(ImmutableList.toImmutableList());
  }

  @Override
  public long count(Pagination pagination) {

    Condition searchCondition = buildSearchCondition(pagination);

    return create
      .selectCount()
      .from(Tables.CONTACTS)
      .where(searchCondition)
      .fetchOne()
      .value1();
  }

  private Condition buildSearchCondition(Pagination pagination) {
    Select<Record1<Integer>> hasEmail = create
      .selectOne()
      .from(Tables.CONTACT_EMAILS)
      .where(
        JooqUtil.search(pagination.getQuery(), Tables.CONTACT_EMAILS.EMAIL),
        Tables.CONTACTS.ID.eq(Tables.CONTACT_EMAILS.CONTACT));

    Select<Record1<Integer>> hasPhone = create
      .selectOne()
      .from(Tables.CONTACT_PHONE_NUMBERS)
      .where(
        JooqUtil.search(pagination.getQuery(), Tables.CONTACT_PHONE_NUMBERS.PHONE),
        Tables.CONTACTS.ID.eq(Tables.CONTACT_PHONE_NUMBERS.CONTACT));

    List<Condition> conditions = JooqUtil.search(
      pagination.getQuery(),
      Tables.CONTACTS.CODE,
      Tables.CONTACTS.FULL_NAME);
    conditions.add(DSL.exists(hasEmail));
    conditions.add(DSL.exists(hasPhone));

    Condition searchCondition = DSL.or(conditions);
    return searchCondition;
  }

  @Override
  public ContactFullDisplay get(String code) {
    Long contactId = create
      .select(Tables.CONTACTS.ID)
      .from(Tables.CONTACTS)
      .where(Tables.CONTACTS.CODE.eq(code))
      .fetchOne(Tables.CONTACTS.ID);

    return create
      .select(
        Tables.CONTACTS.CODE,
        Tables.CONTACTS.FULL_NAME,
        Tables.CONTACTS.DESCRIPTION)
      .from(Tables.CONTACTS)
      .where(Tables.CONTACTS.CODE.eq(code))
      .fetchOne()
      .map(record -> ContactFullDisplay.builder()
        .code(record.get(Tables.CONTACTS.CODE))
        .fullName(record.get(Tables.CONTACTS.FULL_NAME))
        .description(record.get(Tables.CONTACTS.DESCRIPTION))
        .version(record.get(Tables.CONTACTS.MODIFIED_AT))
        .emails(assignedEmail(contactId))
        .phones(assignedPhone(contactId))
        .build());
  }

  @Override
  public void delete(String code) {
    create
      .deleteFrom(Tables.CONTACTS)
      .where(Tables.CONTACTS.CODE.eq(code))
      .execute();
  }

  @Override
  public void delete(Set<String> codes) {
    create
    .deleteFrom(Tables.CONTACTS)
    .where(Tables.CONTACTS.CODE.in(codes))
    .execute();
  }
}

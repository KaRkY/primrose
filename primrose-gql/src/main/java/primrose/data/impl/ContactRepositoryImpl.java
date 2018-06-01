package primrose.data.impl;

import static org.jooq.impl.DSL.currentOffsetDateTime;
import static org.jooq.impl.DSL.value;
import static primrose.jooq.JooqUtil.search;
import static primrose.jooq.Tables.CONTACTS;
import static primrose.jooq.Tables.CONTACT_EMAILS;
import static primrose.jooq.Tables.CONTACT_PHONE_NUMBERS;
import static primrose.jooq.Tables.EMAIL_TYPES;
import static primrose.jooq.Tables.PHONE_NUMBER_TYPES;

import java.util.List;
import java.util.stream.Collector;

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
import primrose.service.Email;
import primrose.service.Pagination;
import primrose.service.PhoneNumber;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCode;
import primrose.service.contact.ContactPreview;

@Repository
public class ContactRepositoryImpl implements ContactRepository {

  private DSLContext create;

  public ContactRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public ContactCode create(Contact contact) {
    ContactsRecord record = create
      .insertInto(CONTACTS)
      .columns(
        CONTACTS.FULL_NAME,
        CONTACTS.DESCRIPTION,
        CONTACTS.CREATED_BY,
        CONTACTS.CREATED_AT,
        CONTACTS.CHANGED_BY,
        CONTACTS.CHANGED_AT)
      .values(
        value(contact.getFullName()),
        value(contact.getDescription()),
        value("test"),
        currentOffsetDateTime(),
        value("test"),
        currentOffsetDateTime())
      .returning(CONTACTS.CODE, CONTACTS.ID)
      .fetchOne();

    contact.getEmails().forEach(email -> {
      create
        .insertInto(CONTACT_EMAILS)
        .columns(
          CONTACT_EMAILS.CONTACT,
          CONTACT_EMAILS.EMAIL,
          CONTACT_EMAILS.EMAIL_TYPE)
        .values(
          value(record.getId()),
          value(email.getValue()),
          create.select(EMAIL_TYPES.ID).from(EMAIL_TYPES).where(EMAIL_TYPES.CODE.eq(email.getType())).asField())
        .execute();
    });

    contact.getPhoneNumbers().forEach(phone -> {
      create
        .insertInto(CONTACT_PHONE_NUMBERS)
        .columns(
          CONTACT_PHONE_NUMBERS.CONTACT,
          CONTACT_PHONE_NUMBERS.PHONE_NUMBER,
          CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE)
        .values(
          value(record.getId()),
          value(phone.getValue()),
          create.select(PHONE_NUMBER_TYPES.ID).from(PHONE_NUMBER_TYPES).where(PHONE_NUMBER_TYPES.CODE.eq(phone.getType())).asField())
        .execute();
    });

    return ContactCode.of(record.getCode());
  }

  @Override
  public ContactCode update(Contact contact) {
    Long contactId = create
      .select(CONTACTS.ID)
      .from(CONTACTS)
      .where(CONTACTS.CODE.eq(contact.getCode().getCode()))
      .forUpdate()
      .fetchOne(CONTACTS.ID);

    if (contactId == null) {
      // throw exception unknown contact
      throw new RuntimeException();
    }

    int updated = create
      .update(CONTACTS)
      .set(CONTACTS.FULL_NAME, contact.getFullName())
      .set(CONTACTS.DESCRIPTION, (contact.getDescription() == null || contact.getDescription().isEmpty()) ? null : contact.getDescription())
      .set(CONTACTS.CHANGED_BY, "test")
      .set(CONTACTS.CHANGED_AT, currentOffsetDateTime())
      .where(
        CONTACTS.CODE.eq(contact.getCode().getCode()),
        CONTACTS.CHANGED_AT.eq(contact.getVersion()))
      .execute();

    create.deleteFrom(CONTACT_EMAILS).where(CONTACT_EMAILS.CONTACT.eq(contactId)).execute();
    create.deleteFrom(CONTACT_PHONE_NUMBERS).where(CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId)).execute();

    contact.getEmails().forEach(email -> {
      create
        .insertInto(CONTACT_EMAILS)
        .columns(
          CONTACT_EMAILS.CONTACT,
          CONTACT_EMAILS.EMAIL,
          CONTACT_EMAILS.EMAIL_TYPE)
        .values(
          value(contactId),
          value(email.getValue()),
          create.select(EMAIL_TYPES.ID).from(EMAIL_TYPES).where(EMAIL_TYPES.CODE.eq(email.getType())).asField())
        .execute();
    });

    contact.getPhoneNumbers().forEach(phone -> {
      create
        .insertInto(CONTACT_PHONE_NUMBERS)
        .columns(
          CONTACT_PHONE_NUMBERS.CONTACT,
          CONTACT_PHONE_NUMBERS.PHONE_NUMBER,
          CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE)
        .values(
          value(contactId),
          value(phone.getValue()),
          create.select(PHONE_NUMBER_TYPES.ID).from(PHONE_NUMBER_TYPES).where(PHONE_NUMBER_TYPES.CODE.eq(phone.getType())).asField())
        .execute();
    });

    if (updated != 1) {
      // Throw error
    }

    return contact.getCode();
  }

  @Override
  public ImmutableList<ContactPreview> list(Pagination pagination) {
    int limit = pagination.getSize();
    int offset = pagination.getPage() * pagination.getSize();
    Condition searchCondition = buildSearchCondition(pagination);

    Field<String> primaryEmail = create
      .select(CONTACT_EMAILS.EMAIL)
      .from(CONTACT_EMAILS)
      .where(CONTACT_EMAILS.CONTACT.eq(CONTACTS.ID))
      .orderBy(CONTACT_EMAILS.EMAIL)
      .limit(1)
      .<String>asField("primaryEmail");

    Field<String> primaryPhone = create
      .select(CONTACT_PHONE_NUMBERS.PHONE_NUMBER)
      .from(CONTACT_PHONE_NUMBERS)
      .where(CONTACT_PHONE_NUMBERS.CONTACT.eq(CONTACTS.ID))
      .orderBy(CONTACT_PHONE_NUMBERS.PHONE_NUMBER)
      .limit(1)
      .<String>asField("primaryPhone");

    return create
      .select(
        CONTACTS.CODE,
        CONTACTS.FULL_NAME,
        primaryEmail,
        primaryPhone)
      .from(CONTACTS)
      .where(searchCondition)
      .limit(limit)
      .offset(offset)
      .fetch()
      .stream()
      .map(record -> ContactPreview.builder()
        .code(record.get(CONTACTS.CODE))
        .fullName(record.get(CONTACTS.FULL_NAME))
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
      .from(CONTACTS)
      .where(searchCondition)
      .fetchOne()
      .value1();
  }

  private Condition buildSearchCondition(Pagination pagination) {
    Select<Record1<Integer>> hasEmail = create
      .selectOne()
      .from(CONTACT_EMAILS)
      .where(
        search(value(pagination.getSearch()), CONTACT_EMAILS.EMAIL),
        CONTACTS.ID.eq(CONTACT_EMAILS.CONTACT));

    Select<Record1<Integer>> hasPhone = create
      .selectOne()
      .from(CONTACT_PHONE_NUMBERS)
      .where(
        search(value(pagination.getSearch()), CONTACT_PHONE_NUMBERS.PHONE_NUMBER),
        CONTACTS.ID.eq(CONTACT_PHONE_NUMBERS.CONTACT));

    List<Condition> conditions = JooqUtil.search(
      pagination.getSearch(),
      CONTACTS.CODE,
      CONTACTS.FULL_NAME);
    conditions.add(DSL.exists(hasEmail));
    conditions.add(DSL.exists(hasPhone));

    Condition searchCondition = DSL.or(conditions);
    return searchCondition;
  }

  @Override
  public Contact get(ContactCode code) {
    Long contactId = create
      .select(Tables.CONTACTS.ID)
      .from(Tables.CONTACTS)
      .where(Tables.CONTACTS.CODE.eq(code.getCode()))
      .fetchOne(Tables.CONTACTS.ID);

    if (contactId == null) {
      // throw exception unknown contact
      throw new RuntimeException();
    }

    return create
      .select(
        CONTACTS.CODE,
        CONTACTS.FULL_NAME,
        CONTACTS.DESCRIPTION,
        CONTACTS.CHANGED_AT)
      .from(CONTACTS)
      .where(CONTACTS.CODE.eq(code.getCode()))
      .fetchOne(record -> Contact.builder()
        .code(ContactCode.of(record.get(CONTACTS.CODE)))
        .fullName(record.get(CONTACTS.FULL_NAME))
        .description(record.get(CONTACTS.DESCRIPTION))
        .version(record.get(CONTACTS.CHANGED_AT))
        .emails(listEmails(contactId, ImmutableList.toImmutableList()))
        .phoneNumbers(listPhoneNumbers(contactId, ImmutableList.toImmutableList()))
        .build());
  }

  private <C> C listEmails(long contactId, Collector<Email, ?, C> collector) {
    return create
      .select(
        EMAIL_TYPES.CODE,
        CONTACT_EMAILS.EMAIL)
      .from(CONTACT_EMAILS)
      .innerJoin(EMAIL_TYPES).on(EMAIL_TYPES.ID.eq(CONTACT_EMAILS.EMAIL_TYPE))
      .where(CONTACT_EMAILS.CONTACT.eq(contactId))
      .fetch()
      .stream()
      .map(record -> Email.builder()
        .type(record.get(EMAIL_TYPES.CODE))
        .value(record.get(CONTACT_EMAILS.EMAIL))
        .build())
      .collect(collector);
  }

  private <C> C listPhoneNumbers(long contactId, Collector<PhoneNumber, ?, C> collector) {
    return create
      .select(
        PHONE_NUMBER_TYPES.CODE,
        CONTACT_PHONE_NUMBERS.PHONE_NUMBER)
      .from(CONTACT_PHONE_NUMBERS)
      .innerJoin(PHONE_NUMBER_TYPES).on(PHONE_NUMBER_TYPES.ID.eq(CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(
        CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId))
      .fetch()
      .stream()
      .map(record -> PhoneNumber.builder()
        .type(record.get(PHONE_NUMBER_TYPES.CODE))
        .value(record.get(CONTACT_PHONE_NUMBERS.PHONE_NUMBER))
        .build())
      .collect(collector);
  }
}

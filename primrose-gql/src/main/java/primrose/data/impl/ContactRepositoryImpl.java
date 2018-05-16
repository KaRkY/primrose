package primrose.data.impl;

import static org.jooq.impl.DSL.currentOffsetDateTime;
import static org.jooq.impl.DSL.defaultValue;
import static org.jooq.impl.DSL.value;
import static primrose.jooq.JooqUtil.containes;
import static primrose.jooq.JooqUtil.search;
import static primrose.jooq.JooqUtil.tstzrange;
import static primrose.jooq.Tables.CONTACTS;
import static primrose.jooq.Tables.CONTACT_DATA;
import static primrose.jooq.Tables.CONTACT_EMAILS;
import static primrose.jooq.Tables.CONTACT_PHONE_NUMBERS;
import static primrose.jooq.Tables.EMAILS;
import static primrose.jooq.Tables.EMAIL_TYPES;
import static primrose.jooq.Tables.PHONE_NUMBERS;
import static primrose.jooq.Tables.PHONE_NUMBER_TYPES;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;

import primrose.data.ContactRepository;
import primrose.data.EmailAndPhoneNumberRepository;
import primrose.jooq.JooqUtil;
import primrose.jooq.Tables;
import primrose.service.Email;
import primrose.service.Pagination;
import primrose.service.PhoneNumber;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCode;
import primrose.service.contact.ContactPreview;

@Repository
public class ContactRepositoryImpl implements ContactRepository {

  private DSLContext                    create;
  private EmailAndPhoneNumberRepository emailAndPhoneNumberRepository;

  public ContactRepositoryImpl(
    DSLContext create,
    EmailAndPhoneNumberRepository emailAndPhoneNumberRepository) {
    this.create = create;
    this.emailAndPhoneNumberRepository = emailAndPhoneNumberRepository;
  }

  @Override
  public ContactCode generate() {
    return ContactCode.of(create
      .insertInto(CONTACTS)
      .columns(CONTACTS.ID, CONTACTS.CODE)
      .values(defaultValue(CONTACTS.ID), defaultValue(CONTACTS.CODE))
      .returning(CONTACTS.CODE)
      .fetchOne()
      .getCode());
  }

  @Override
  public void create(Contact contact) {
    Long contactId = create
      .select(CONTACTS.ID)
      .from(CONTACTS)
      .where(CONTACTS.CODE.eq(contact.getCode().getCode()))
      .forUpdate()
      .fetchOne()
      .value1();

    OffsetDateTime version = create
      .insertInto(CONTACT_DATA)
      .columns(
        CONTACT_DATA.CONTACT,
        CONTACT_DATA.FULL_NAME,
        CONTACT_DATA.DESCRIPTION)
      .values(
        contactId,
        contact.getFullName(),
        contact.getDescription())
      .returning(CONTACT_DATA.VALID_FROM)
      .fetchOne()
      .getValidFrom();

    contact.getEmails().forEach(email -> {
      long emailId = emailAndPhoneNumberRepository.email(email.getValue());
      assignEmail(contactId, email.getType(), emailId, value(version));
    });

    contact.getPhoneNumbers().forEach(phone -> {
      long phoneId = emailAndPhoneNumberRepository.phone(phone.getValue());
      assignPhone(contactId, phone.getType(), phoneId, value(version));
    });
  }

  @Override
  public void update(Contact contact) {
    Long contactId = create
      .select(CONTACTS.ID)
      .from(CONTACTS)
      .where(CONTACTS.CODE.eq(contact.getCode().getCode()))
      .forUpdate()
      .fetchOne(0, Long.class);

    if (contactId == null) {
      // throw exception unknown contact
      throw new RuntimeException();
    }

    OffsetDateTime currentVersion = create
      .select(CONTACT_DATA.VALID_FROM)
      .from(CONTACT_DATA)
      .where(
        CONTACT_DATA.CONTACT.eq(contactId),
        containes(tstzrange(CONTACT_DATA.VALID_FROM, CONTACT_DATA.VALID_TO, value("[)")), currentOffsetDateTime()))
      .fetchOne()
      .get(CONTACT_DATA.VALID_FROM);

    boolean update = !create.fetchExists(create
      .selectOne()
      .from(CONTACT_DATA)
      .where(
        CONTACT_DATA.FULL_NAME.eq(contact.getFullName()),
        contact.getDescription() != null ? CONTACT_DATA.DESCRIPTION.eq(contact.getDescription()) : CONTACT_DATA.DESCRIPTION.isNull(),
        CONTACT_DATA.CONTACT.eq(contactId),
        containes(tstzrange(CONTACT_DATA.VALID_FROM, CONTACT_DATA.VALID_TO, value("[)")), currentOffsetDateTime())));

    Set<Email> oldEmails = listEmails(contactId, value(currentVersion), Collectors.toSet());
    Set<PhoneNumber> oldPhoneNumbers = listPhoneNumbers(contactId, value(currentVersion), Collectors.toSet());
    Set<Email> newEmails = contact.getEmails().stream().collect(Collectors.toSet());
    Set<PhoneNumber> newPhoneNumbers = contact.getPhoneNumbers().stream().collect(Collectors.toSet());

    if (update || !oldEmails.equals(newEmails) || !oldPhoneNumbers.equals(newPhoneNumbers)) {
      int updated = create
        .update(CONTACT_DATA)
        .set(CONTACT_DATA.VALID_TO, currentOffsetDateTime())
        .where(
          CONTACT_DATA.CONTACT.eq(contactId),
          CONTACT_DATA.VALID_FROM.eq(contact.getVersion()),
          CONTACT_DATA.VALID_TO.isNull())
        .execute();

      if (updated != 1) {
        // throw exception concurent modification
        throw new RuntimeException();
      }

      removeEmails(contactId, value(contact.getVersion()));
      removePhoneNumbers(contactId, value(contact.getVersion()));

      create(contact);
    }
  }

  @Override
  public ImmutableList<ContactPreview> list(Pagination pagination) {
    int limit = pagination.getSize();
    int offset = pagination.getPage() * pagination.getSize();
    Condition searchCondition = buildSearchCondition(pagination);

    Field<String> primaryEmail = create
      .select(EMAILS.EMAIL)
      .from(CONTACT_EMAILS)
      .innerJoin(EMAILS).on(EMAILS.ID.eq(CONTACT_EMAILS.EMAIL))
      .where(CONTACT_EMAILS.CONTACT.eq(CONTACTS.ID))
      .orderBy(CONTACT_EMAILS.VALID_FROM)
      .limit(1)
      .<String>asField("primaryEmail");

    Field<String> primaryPhone = create
      .select(PHONE_NUMBERS.PHONE_NUMBER)
      .from(CONTACT_PHONE_NUMBERS)
      .innerJoin(PHONE_NUMBERS).on(PHONE_NUMBERS.ID.eq(CONTACT_PHONE_NUMBERS.PHONE_NUMBER))
      .where(CONTACT_PHONE_NUMBERS.CONTACT.eq(CONTACTS.ID))
      .orderBy(CONTACT_PHONE_NUMBERS.VALID_FROM)
      .limit(1)
      .<String>asField("primaryPhone");

    return create
      .select(
        CONTACTS.CODE,
        CONTACT_DATA.FULL_NAME,
        primaryEmail,
        primaryPhone)
      .from(CONTACTS)
      .innerJoin(CONTACT_DATA).on(
        CONTACT_DATA.CONTACT.eq(CONTACTS.ID),
        containes(tstzrange(CONTACT_DATA.VALID_FROM, CONTACT_DATA.VALID_TO, value("[)")), currentOffsetDateTime()))
      .where(searchCondition)
      .limit(limit)
      .offset(offset)
      .fetch()
      .stream()
      .map(record -> ContactPreview.builder()
        .code(record.get(CONTACTS.CODE))
        .fullName(record.get(CONTACT_DATA.FULL_NAME))
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
      .innerJoin(CONTACT_DATA).on(
        CONTACT_DATA.CONTACT.eq(CONTACTS.ID),
        containes(tstzrange(CONTACT_DATA.VALID_FROM, CONTACT_DATA.VALID_TO, value("[)")), currentOffsetDateTime()))
      .where(searchCondition)
      .fetchOne()
      .value1();
  }

  private Condition buildSearchCondition(Pagination pagination) {
    Select<Record1<Integer>> hasEmail = create
      .selectOne()
      .from(CONTACT_EMAILS)
      .innerJoin(EMAILS).on(EMAILS.ID.eq(CONTACT_EMAILS.EMAIL))
      .where(
        search(value(pagination.getSearch()), EMAILS.EMAIL),
        CONTACTS.ID.eq(CONTACT_EMAILS.CONTACT));

    Select<Record1<Integer>> hasPhone = create
      .selectOne()
      .from(CONTACT_PHONE_NUMBERS)
      .innerJoin(PHONE_NUMBERS).on(PHONE_NUMBERS.ID.eq(CONTACT_PHONE_NUMBERS.PHONE_NUMBER))
      .where(
        search(value(pagination.getSearch()), PHONE_NUMBERS.PHONE_NUMBER),
        CONTACTS.ID.eq(CONTACT_PHONE_NUMBERS.CONTACT));

    List<Condition> conditions = JooqUtil.search(
      pagination.getSearch(),
      CONTACTS.CODE,
      CONTACT_DATA.FULL_NAME);
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

    return create
      .select(
        CONTACTS.CODE,
        CONTACT_DATA.FULL_NAME,
        CONTACT_DATA.DESCRIPTION,
        CONTACT_DATA.VALID_FROM)
      .from(CONTACTS)
      .innerJoin(CONTACT_DATA).on(
        CONTACT_DATA.CONTACT.eq(CONTACTS.ID),
        containes(tstzrange(CONTACT_DATA.VALID_FROM, CONTACT_DATA.VALID_TO, value("[)")), currentOffsetDateTime()))
      .where(CONTACTS.CODE.eq(code.getCode()))
      .fetchOne(record -> Contact.builder()
        .code(ContactCode.of(record.get(CONTACTS.CODE)))
        .fullName(record.get(CONTACT_DATA.FULL_NAME))
        .description(record.get(CONTACT_DATA.DESCRIPTION))
        .version(record.get(CONTACT_DATA.VALID_FROM))
        .emails(listEmails(contactId, value(record.get(CONTACT_DATA.VALID_FROM)), ImmutableList.toImmutableList()))
        .phoneNumbers(listPhoneNumbers(contactId, value(record.get(CONTACT_DATA.VALID_FROM)), ImmutableList.toImmutableList()))
        .build());
  }

  private <C> C listEmails(long contactId, Field<OffsetDateTime> version, Collector<Email, ?, C> collector) {
    return create
      .select(
        EMAIL_TYPES.CODE,
        EMAILS.EMAIL)
      .from(CONTACT_EMAILS)
      .innerJoin(EMAILS).on(EMAILS.ID.eq(CONTACT_EMAILS.EMAIL))
      .innerJoin(EMAIL_TYPES).on(EMAIL_TYPES.ID.eq(CONTACT_EMAILS.EMAIL_TYPE))
      .where(
        CONTACT_EMAILS.CONTACT.eq(contactId),
        CONTACT_EMAILS.VALID_FROM.eq(version))
      .fetch()
      .stream()
      .map(record -> Email.builder()
        .type(record.get(EMAIL_TYPES.CODE))
        .value(record.get(EMAILS.EMAIL))
        .build())
      .collect(collector);
  }

  private <C> C listPhoneNumbers(long contactId, Field<OffsetDateTime> version, Collector<PhoneNumber, ?, C> collector) {
    return create
      .select(
        PHONE_NUMBER_TYPES.CODE,
        PHONE_NUMBERS.PHONE_NUMBER)
      .from(CONTACT_PHONE_NUMBERS)
      .innerJoin(PHONE_NUMBERS).on(PHONE_NUMBERS.ID.eq(CONTACT_PHONE_NUMBERS.PHONE_NUMBER))
      .innerJoin(PHONE_NUMBER_TYPES).on(PHONE_NUMBER_TYPES.ID.eq(CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(
        CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId),
        CONTACT_PHONE_NUMBERS.VALID_FROM.eq(version))
      .fetch()
      .stream()
      .map(record -> PhoneNumber.builder()
        .type(record.get(PHONE_NUMBER_TYPES.CODE))
        .value(record.get(PHONE_NUMBERS.PHONE_NUMBER))
        .build())
      .collect(collector);
  }

  private void assignEmail(Long contactId, String type, long emailId, Field<OffsetDateTime> version) {
    create
      .insertInto(CONTACT_EMAILS)
      .columns(
        CONTACT_EMAILS.CONTACT,
        CONTACT_EMAILS.EMAIL_TYPE,
        CONTACT_EMAILS.EMAIL,
        CONTACT_EMAILS.VALID_FROM)
      .values(
        value(contactId),
        create
          .select(EMAIL_TYPES.ID)
          .from(EMAIL_TYPES)
          .where(EMAIL_TYPES.CODE.eq(type))
          .asField(),
        value(emailId),
        version)
      .execute();
  }

  private void assignPhone(Long contactId, String type, long phoneId, Field<OffsetDateTime> version) {
    create
      .insertInto(CONTACT_PHONE_NUMBERS)
      .columns(
        CONTACT_PHONE_NUMBERS.CONTACT,
        CONTACT_PHONE_NUMBERS.PHONE_NUMBER_TYPE,
        CONTACT_PHONE_NUMBERS.PHONE_NUMBER,
        CONTACT_PHONE_NUMBERS.VALID_FROM)
      .values(
        value(contactId),
        create
          .select(PHONE_NUMBER_TYPES.ID)
          .from(PHONE_NUMBER_TYPES)
          .where(PHONE_NUMBER_TYPES.CODE.eq(type))
          .asField(),
        value(phoneId),
        version)
      .execute();
  }

  private void removeEmails(long contactId, Field<OffsetDateTime> version) {
    create
      .update(CONTACT_EMAILS)
      .set(CONTACT_EMAILS.VALID_TO, version)
      .where(
        CONTACT_EMAILS.CONTACT.eq(contactId),
        CONTACT_EMAILS.VALID_FROM.eq(version))
      .execute();
  }

  private void removePhoneNumbers(Long contactId, Field<OffsetDateTime> version) {
    create
      .update(CONTACT_PHONE_NUMBERS)
      .set(CONTACT_EMAILS.VALID_TO, currentOffsetDateTime())
      .where(
        CONTACT_PHONE_NUMBERS.CONTACT.eq(contactId),
        CONTACT_PHONE_NUMBERS.VALID_FROM.eq(version))
      .execute();
  }
}

package primrose.data.impl;

import static org.jooq.impl.DSL.currentOffsetDateTime;
import static org.jooq.impl.DSL.defaultValue;
import static org.jooq.impl.DSL.value;
import static primrose.jooq.JooqUtil.containes;
import static primrose.jooq.JooqUtil.search;
import static primrose.jooq.JooqUtil.tstzrange;
import static primrose.jooq.Tables.CUSTOMERS;
import static primrose.jooq.Tables.CUSTOMER_DATA;
import static primrose.jooq.Tables.CUSTOMER_EMAILS;
import static primrose.jooq.Tables.CUSTOMER_PHONE_NUMBERS;
import static primrose.jooq.Tables.CUSTOMER_RELATION_TYPES;
import static primrose.jooq.Tables.CUSTOMER_TYPES;
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

import primrose.data.CustomerRepository;
import primrose.data.EmailAndPhoneNumberRepository;
import primrose.jooq.JooqUtil;
import primrose.jooq.Tables;
import primrose.service.Email;
import primrose.service.Pagination;
import primrose.service.PhoneNumber;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCode;
import primrose.service.customer.CustomerPreview;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

  private DSLContext                    create;
  private EmailAndPhoneNumberRepository emailAndPhoneNumberRepository;

  public CustomerRepositoryImpl(
    DSLContext create,
    EmailAndPhoneNumberRepository emailAndPhoneNumberRepository) {
    this.create = create;
    this.emailAndPhoneNumberRepository = emailAndPhoneNumberRepository;
  }

  @Override
  public CustomerCode generate() {
    return CustomerCode.of(create
      .insertInto(CUSTOMERS)
      .columns(CUSTOMERS.ID, CUSTOMERS.CODE)
      .values(defaultValue(CUSTOMERS.ID), defaultValue(CUSTOMERS.CODE))
      .returning(CUSTOMERS.CODE)
      .fetchOne()
      .getCode());
  }

  @Override
  public void create(Customer customer) {
    Long customerId = create
      .select(CUSTOMERS.ID)
      .from(CUSTOMERS)
      .where(CUSTOMERS.CODE.eq(customer.getCode().getCode()))
      .forUpdate()
      .fetchOne(0, Long.class);

    if (customerId == null) {
      // throw exception unknown contact
      throw new RuntimeException();
    }

    Select<Record1<Long>> type = create
      .select(CUSTOMER_TYPES.ID)
      .from(CUSTOMER_TYPES)
      .where(CUSTOMER_TYPES.CODE.eq(customer.getType()));

    Select<Record1<Long>> relationType = create
      .select(CUSTOMER_RELATION_TYPES.ID)
      .from(CUSTOMER_RELATION_TYPES)
      .where(CUSTOMER_RELATION_TYPES.CODE.eq(customer.getRelationType()));

    OffsetDateTime version = create
      .insertInto(CUSTOMER_DATA)
      .columns(
        CUSTOMER_DATA.CUSTOMER,
        CUSTOMER_DATA.CUSTOMER_TYPE,
        CUSTOMER_DATA.CUSTOMER_RELATION_TYPE,
        CUSTOMER_DATA.FULL_NAME,
        CUSTOMER_DATA.DISPLAY_NAME,
        CUSTOMER_DATA.DESCRIPTION)
      .values(
        value(customerId),
        type.asField(),
        relationType.asField(),
        value(customer.getFullName()),
        value(customer.getDisplayName()),
        value(customer.getDescription()))
      .returning(CUSTOMER_DATA.VALID_FROM)
      .fetchOne()
      .getValidFrom();

    customer.getEmails().forEach(email -> {
      long emailId = emailAndPhoneNumberRepository.email(email.getValue());
      assignEmail(customerId, email.getType(), emailId, value(version));
    });

    customer.getPhoneNumbers().forEach(phone -> {
      long phoneId = emailAndPhoneNumberRepository.phone(phone.getValue());
      assignPhone(customerId, phone.getType(), phoneId, value(version));
    });
  }

  @Override
  public void update(Customer customer) {
    Long customerId = create
      .select(CUSTOMERS.ID)
      .from(CUSTOMERS)
      .where(CUSTOMERS.CODE.eq(customer.getCode().getCode()))
      .forUpdate()
      .fetchOne(0, Long.class);

    if (customerId == null) {
      // throw exception unknown contact
      throw new RuntimeException();
    }

    OffsetDateTime currentVersion = create
      .select(CUSTOMER_DATA.VALID_FROM)
      .from(CUSTOMER_DATA)
      .where(
        CUSTOMER_DATA.CUSTOMER.eq(customerId),
        containes(tstzrange(CUSTOMER_DATA.VALID_FROM, CUSTOMER_DATA.VALID_TO, value("[)")), currentOffsetDateTime()))
      .fetchOne()
      .get(CUSTOMER_DATA.VALID_FROM);

    Select<Record1<Long>> type = create
      .select(CUSTOMER_TYPES.ID)
      .from(CUSTOMER_TYPES)
      .where(CUSTOMER_TYPES.CODE.eq(customer.getType()));

    Select<Record1<Long>> relationType = create
      .select(CUSTOMER_RELATION_TYPES.ID)
      .from(CUSTOMER_RELATION_TYPES)
      .where(CUSTOMER_RELATION_TYPES.CODE.eq(customer.getRelationType()));

    boolean update = !create.fetchExists(create
      .selectOne()
      .from(CUSTOMER_DATA)
      .where(
        CUSTOMER_DATA.FULL_NAME.eq(customer.getFullName()),
        customer.getDisplayName() != null ? CUSTOMER_DATA.DISPLAY_NAME.eq(customer.getDisplayName()) : CUSTOMER_DATA.DISPLAY_NAME.isNull(),
        customer.getDescription() != null ? CUSTOMER_DATA.DESCRIPTION.eq(customer.getDescription()) : CUSTOMER_DATA.DESCRIPTION.isNull(),
        CUSTOMER_DATA.CUSTOMER_TYPE.eq(type),
        CUSTOMER_DATA.CUSTOMER_RELATION_TYPE.eq(relationType),
        CUSTOMER_DATA.CUSTOMER.eq(customerId),
        containes(tstzrange(CUSTOMER_DATA.VALID_FROM, CUSTOMER_DATA.VALID_TO, value("[)")), currentOffsetDateTime())));

    Set<Email> oldEmails = listEmails(customerId, value(currentVersion), Collectors.toSet());
    Set<PhoneNumber> oldPhoneNumbers = listPhoneNumbers(customerId, value(currentVersion), Collectors.toSet());
    Set<Email> newEmails = customer.getEmails().stream().collect(Collectors.toSet());
    Set<PhoneNumber> newPhoneNumbers = customer.getPhoneNumbers().stream().collect(Collectors.toSet());

    if (update || !oldEmails.equals(newEmails) || !oldPhoneNumbers.equals(newPhoneNumbers)) {
      int updated = create
        .update(CUSTOMER_DATA)
        .set(CUSTOMER_DATA.VALID_TO, currentOffsetDateTime())
        .where(
          CUSTOMER_DATA.CUSTOMER.eq(customerId),
          CUSTOMER_DATA.VALID_FROM.eq(customer.getVersion()),
          CUSTOMER_DATA.VALID_TO.isNull())
        .execute();

      if (updated != 1) {
        // throw exception concurent modification
        throw new RuntimeException();
      }

      removeEmails(customerId, value(customer.getVersion()));
      removePhoneNumbers(customerId, value(customer.getVersion()));

      create(customer);
    }
  }

  @Override
  public ImmutableList<CustomerPreview> list(Pagination pagination) {
    int limit = pagination.getSize();
    int offset = pagination.getPage() * pagination.getSize();
    Condition searchCondition = buildSearchCondition(pagination);

    Field<String> primaryEmail = create
      .select(CUSTOMER_EMAILS.EMAIL)
      .from(CUSTOMER_EMAILS)
      .where(CUSTOMER_EMAILS.CUSTOMER.eq(Tables.CUSTOMERS.ID))
      .orderBy(CUSTOMER_EMAILS.VALID_FROM)
      .limit(1)
      .<String>asField("primaryEmail");

    Field<String> primaryPhone = create
      .select(CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER)
      .from(CUSTOMER_PHONE_NUMBERS)
      .where(CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(Tables.CUSTOMERS.ID))
      .orderBy(CUSTOMER_PHONE_NUMBERS.VALID_FROM)
      .limit(1)
      .<String>asField("primaryPhone");

    return create
      .select(
        CUSTOMERS.CODE,
        CUSTOMER_TYPES.CODE,
        CUSTOMER_RELATION_TYPES.CODE,
        CUSTOMER_DATA.DISPLAY_NAME,
        CUSTOMER_DATA.FULL_NAME,
        primaryEmail,
        primaryPhone)
      .from(CUSTOMERS)
      .innerJoin(CUSTOMER_TYPES).on(CUSTOMER_TYPES.ID.eq(CUSTOMER_DATA.CUSTOMER_TYPE))
      .innerJoin(CUSTOMER_RELATION_TYPES).on(CUSTOMER_RELATION_TYPES.ID.eq(CUSTOMER_DATA.CUSTOMER_RELATION_TYPE))
      .innerJoin(CUSTOMER_DATA).on(
        CUSTOMER_DATA.CUSTOMER.eq(CUSTOMERS.ID),
        containes(tstzrange(CUSTOMER_DATA.VALID_FROM, CUSTOMER_DATA.VALID_TO, value("[)")), currentOffsetDateTime()))
      .where(searchCondition)
      .limit(limit)
      .offset(offset)
      .fetch()
      .stream()
      .map(record -> CustomerPreview.builder()
        .code(record.get(Tables.CUSTOMERS.CODE))
        .type(record.get(Tables.CUSTOMER_TYPES.CODE))
        .relationType(record.get(Tables.CUSTOMER_RELATION_TYPES.CODE))
        .displayName(record.get(Tables.CUSTOMER_DATA.DISPLAY_NAME))
        .fullName(record.get(Tables.CUSTOMER_DATA.FULL_NAME))
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
      .from(CUSTOMERS)
      .innerJoin(CUSTOMER_TYPES).on(CUSTOMER_TYPES.ID.eq(CUSTOMER_DATA.CUSTOMER_TYPE))
      .innerJoin(CUSTOMER_RELATION_TYPES).on(CUSTOMER_RELATION_TYPES.ID.eq(CUSTOMER_DATA.CUSTOMER_RELATION_TYPE))
      .innerJoin(CUSTOMER_DATA).on(
        CUSTOMER_DATA.CUSTOMER.eq(CUSTOMERS.ID),
        containes(tstzrange(CUSTOMER_DATA.VALID_FROM, CUSTOMER_DATA.VALID_TO, value("[)")), currentOffsetDateTime()))
      .where(searchCondition)
      .fetchOne()
      .value1();
  }

  private Condition buildSearchCondition(Pagination pagination) {
    Select<Record1<Integer>> hasEmail = create
      .selectOne()
      .from(CUSTOMER_EMAILS)
      .innerJoin(EMAILS).on(EMAILS.ID.eq(CUSTOMER_EMAILS.EMAIL))
      .where(
        search(pagination.getQuery(), EMAILS.EMAIL),
        CUSTOMERS.ID.eq(CUSTOMER_EMAILS.CUSTOMER));

    Select<Record1<Integer>> hasPhone = create
      .selectOne()
      .from(CUSTOMER_PHONE_NUMBERS)
      .innerJoin(PHONE_NUMBERS).on(PHONE_NUMBERS.ID.eq(CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER))
      .where(
        search(pagination.getQuery(), PHONE_NUMBERS.PHONE_NUMBER),
        CUSTOMERS.ID.eq(CUSTOMER_PHONE_NUMBERS.CUSTOMER));

    List<Condition> conditions = JooqUtil.search(
      pagination.getQuery(),
      CUSTOMERS.CODE,
      CUSTOMER_TYPES.CODE,
      CUSTOMER_TYPES.DEFAULT_NAME,
      CUSTOMER_RELATION_TYPES.CODE,
      CUSTOMER_RELATION_TYPES.DEFAULT_NAME,
      CUSTOMER_DATA.DISPLAY_NAME,
      CUSTOMER_DATA.FULL_NAME,
      CUSTOMER_DATA.DESCRIPTION);
    conditions.add(DSL.exists(hasEmail));
    conditions.add(DSL.exists(hasPhone));

    Condition searchCondition = DSL.or(conditions);
    return searchCondition;
  }

  @Override
  public Customer get(CustomerCode code) {
    Long customerId = create
      .select(Tables.CUSTOMERS.ID)
      .from(Tables.CUSTOMERS)
      .where(Tables.CUSTOMERS.CODE.eq(code.getCode()))
      .fetchOne(Tables.CUSTOMERS.ID);

    return create
      .select(
        CUSTOMERS.CODE,
        CUSTOMER_TYPES.CODE,
        CUSTOMER_RELATION_TYPES.CODE,
        CUSTOMER_DATA.DISPLAY_NAME,
        CUSTOMER_DATA.FULL_NAME,
        CUSTOMER_DATA.DESCRIPTION,
        CUSTOMER_DATA.VALID_FROM)
      .from(Tables.CUSTOMERS)
      .innerJoin(CUSTOMER_TYPES).on(CUSTOMER_TYPES.ID.eq(CUSTOMER_DATA.CUSTOMER_TYPE))
      .innerJoin(CUSTOMER_RELATION_TYPES).on(CUSTOMER_RELATION_TYPES.ID.eq(CUSTOMER_DATA.CUSTOMER_RELATION_TYPE))
      .innerJoin(CUSTOMER_DATA).on(
        CUSTOMER_DATA.CUSTOMER.eq(CUSTOMERS.ID),
        containes(tstzrange(CUSTOMER_DATA.VALID_FROM, CUSTOMER_DATA.VALID_TO, value("[)")), currentOffsetDateTime()))
      .where(CUSTOMERS.CODE.eq(code.getCode()))
      .fetchOne()
      .map(record -> Customer.builder()
        .code(CustomerCode.of(record.get(CUSTOMERS.CODE)))
        .type(record.get(CUSTOMER_TYPES.CODE))
        .relationType(record.get(CUSTOMER_RELATION_TYPES.CODE))
        .displayName(record.get(CUSTOMER_DATA.DISPLAY_NAME))
        .fullName(record.get(CUSTOMER_DATA.FULL_NAME))
        .description(record.get(CUSTOMER_DATA.DESCRIPTION))
        .version(record.get(CUSTOMER_DATA.VALID_FROM))
        .emails(listEmails(customerId, value(record.get(CUSTOMER_DATA.VALID_FROM)), ImmutableList.toImmutableList()))
        .phoneNumbers(listPhoneNumbers(customerId, value(record.get(CUSTOMER_DATA.VALID_FROM)), ImmutableList.toImmutableList()))
        .build());
  }

  private <C> C listEmails(long customerId, Field<OffsetDateTime> version, Collector<Email, ?, C> collector) {
    return create
      .select(
        EMAIL_TYPES.CODE,
        EMAILS.EMAIL)
      .from(CUSTOMER_EMAILS)
      .innerJoin(EMAILS).on(EMAILS.ID.eq(CUSTOMER_EMAILS.EMAIL))
      .innerJoin(EMAIL_TYPES).on(EMAIL_TYPES.ID.eq(CUSTOMER_EMAILS.EMAIL_TYPE))
      .where(
        CUSTOMER_EMAILS.CUSTOMER.eq(customerId),
        CUSTOMER_EMAILS.VALID_FROM.eq(version))
      .fetch()
      .stream()
      .map(record -> Email.builder()
        .type(record.get(EMAIL_TYPES.CODE))
        .value(record.get(EMAILS.EMAIL))
        .build())
      .collect(collector);
  }

  private <C> C listPhoneNumbers(long customerId, Field<OffsetDateTime> version, Collector<PhoneNumber, ?, C> collector) {
    return create
      .select(
        PHONE_NUMBER_TYPES.CODE,
        PHONE_NUMBERS.PHONE_NUMBER)
      .from(CUSTOMER_PHONE_NUMBERS)
      .innerJoin(PHONE_NUMBERS).on(PHONE_NUMBERS.ID.eq(CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER))
      .innerJoin(PHONE_NUMBER_TYPES).on(PHONE_NUMBER_TYPES.ID.eq(CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE))
      .where(
        CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customerId),
        CUSTOMER_PHONE_NUMBERS.VALID_FROM.eq(version))
      .fetch()
      .stream()
      .map(record -> PhoneNumber.builder()
        .type(record.get(PHONE_NUMBER_TYPES.CODE))
        .value(record.get(PHONE_NUMBERS.PHONE_NUMBER))
        .build())
      .collect(collector);
  }

  private void assignEmail(Long customerId, String type, long emailId, Field<OffsetDateTime> version) {
    create
      .insertInto(CUSTOMER_EMAILS)
      .columns(
        CUSTOMER_EMAILS.CUSTOMER,
        CUSTOMER_EMAILS.EMAIL_TYPE,
        CUSTOMER_EMAILS.EMAIL,
        CUSTOMER_EMAILS.VALID_FROM)
      .values(
        value(customerId),
        create
          .select(EMAIL_TYPES.ID)
          .from(EMAIL_TYPES)
          .where(EMAIL_TYPES.CODE.eq(type))
          .asField(),
        value(emailId),
        version)
      .execute();
  }

  private void assignPhone(Long customerId, String type, long phoneId, Field<OffsetDateTime> version) {
    create
      .insertInto(CUSTOMER_PHONE_NUMBERS)
      .columns(
        CUSTOMER_PHONE_NUMBERS.CUSTOMER,
        CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER_TYPE,
        CUSTOMER_PHONE_NUMBERS.PHONE_NUMBER,
        CUSTOMER_PHONE_NUMBERS.VALID_FROM)
      .values(
        value(customerId),
        create
          .select(PHONE_NUMBER_TYPES.ID)
          .from(PHONE_NUMBER_TYPES)
          .where(PHONE_NUMBER_TYPES.CODE.eq(type))
          .asField(),
        value(phoneId),
        version)
      .execute();
  }

  private void removeEmails(long customerId, Field<OffsetDateTime> version) {
    create
      .update(CUSTOMER_EMAILS)
      .set(CUSTOMER_EMAILS.VALID_TO, version)
      .where(
        CUSTOMER_EMAILS.CUSTOMER.eq(customerId),
        CUSTOMER_EMAILS.VALID_FROM.eq(version))
      .execute();
  }

  private void removePhoneNumbers(Long customerId, Field<OffsetDateTime> version) {
    create
      .update(CUSTOMER_PHONE_NUMBERS)
      .set(CUSTOMER_EMAILS.VALID_TO, currentOffsetDateTime())
      .where(
        CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(customerId),
        CUSTOMER_PHONE_NUMBERS.VALID_FROM.eq(version))
      .execute();
  }
}

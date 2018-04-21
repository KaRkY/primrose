package primrose.data.impl;

import java.time.OffsetDateTime;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;

import primrose.data.CustomerRepository;
import primrose.jooq.JooqUtil;
import primrose.jooq.Tables;
import primrose.service.CodeId;
import primrose.service.Pagination;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

  private DSLContext create;

  public CustomerRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public CodeId generateCode() {
    return create
        .insertInto(Tables.CUSTOMER_CODES)
        .values(DSL.defaultValue(), DSL.defaultValue())
        .returning(
            Tables.CUSTOMER_CODES.ID,
            Tables.CUSTOMER_CODES.CODE)
        .fetchOne()
        .map(record -> CodeId.builder()
            .id(record.get(Tables.CUSTOMER_CODES.ID))
            .code(record.get(Tables.CUSTOMER_CODES.CODE))
            .build());
  }

  @Override
  public CodeId codeId(String code) {
    return create
        .select(Tables.CUSTOMER_CODES.ID, Tables.CUSTOMER_CODES.CODE)
        .from(Tables.CUSTOMER_CODES)
        .where(Tables.CUSTOMER_CODES.CODE.eq(code))
        .fetchOne(record -> CodeId.builder()
            .id(record.get(Tables.CUSTOMER_CODES.ID))
            .code(record.get(Tables.CUSTOMER_CODES.CODE))
            .build());
  }

  @Override
  public void create(CodeId code, CustomerCreate customer) {
    create
        .insertInto(Tables.CUSTOMERS)
        .columns(
            Tables.CUSTOMERS.CODE,
            Tables.CUSTOMERS.CUSTOMER_TYPE,
            Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE,
            Tables.CUSTOMERS.FULL_NAME,
            Tables.CUSTOMERS.DISPLAY_NAME,
            Tables.CUSTOMERS.DESCRIPTION)
        .values(
            DSL.value(code.getId()),
            create
                .select(Tables.CUSTOMER_TYPES.ID)
                .from(Tables.CUSTOMER_TYPES)
                .where(Tables.CUSTOMER_TYPES.SLUG.eq(customer.getType()))
                .asField(),
            create
                .select(Tables.CUSTOMER_RELATION_TYPES.ID)
                .from(Tables.CUSTOMER_RELATION_TYPES)
                .where(Tables.CUSTOMER_RELATION_TYPES.SLUG.eq(customer.getRelationType()))
                .asField(),
            DSL.value(customer.getFullName()),
            DSL.value(customer.getDisplayName()),
            DSL.value(customer.getDescription()))
        .execute();
  }

  @Override
  public ImmutableList<CustomerReducedDisplay> list(Pagination pagination) {
    int limit = pagination.getSize();
    int offset = pagination.getPage() * pagination.getSize();
    Condition searchCondition = buildSearchCondition(pagination);

    Field<String> primaryEmail = create
        .select(Tables.EMAILS.EMAIL)
        .from(Tables.CUSTOMER_EMAILS)
        .leftJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL))
        .where(
            Tables.CUSTOMER_EMAILS.CUSTOMER.eq(Tables.CUSTOMERS.CODE),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_EMAILS.VALID_FROM,
                Tables.CUSTOMER_EMAILS.VALID_TO))
        .orderBy(Tables.CUSTOMER_EMAILS.PRIM.desc(), Tables.CUSTOMER_EMAILS.VALID_FROM.asc())
        .limit(1)
        .<String>asField("primaryEmail");

    Field<String> primaryPhone = create
        .select(Tables.PHONE_NUMBERS.PHONE)
        .from(Tables.CUSTOMER_PHONE_NUMBERS)
        .leftJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.PHONE))
        .where(
            Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(Tables.CUSTOMERS.CODE),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM,
                Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO))
        .orderBy(Tables.CUSTOMER_PHONE_NUMBERS.PRIM.desc(), Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM.asc())
        .limit(1)
        .<String>asField("primaryPhone");

    return create
        .select(
            Tables.CUSTOMER_CODES.CODE,
            Tables.CUSTOMER_TYPES.SLUG,
            Tables.CUSTOMER_RELATION_TYPES.SLUG,
            Tables.CUSTOMERS.DISPLAY_NAME,
            Tables.CUSTOMERS.FULL_NAME,
            primaryEmail,
            primaryPhone)
        .from(Tables.CUSTOMERS)
        .innerJoin(Tables.CUSTOMER_CODES).on(Tables.CUSTOMER_CODES.ID.eq(Tables.CUSTOMERS.CODE))
        .innerJoin(Tables.CUSTOMER_TYPES).on(Tables.CUSTOMER_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_TYPE))
        .innerJoin(Tables.CUSTOMER_RELATION_TYPES)
        .on(Tables.CUSTOMER_RELATION_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE))
        .where(
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMERS.VALID_FROM, Tables.CUSTOMERS.VALID_TO),
            searchCondition)
        .limit(limit)
        .offset(offset)
        .fetch()
        .stream()
        .map(record -> CustomerReducedDisplay.builder()
            .code(record.get(Tables.CUSTOMER_CODES.CODE))
            .type(record.get(Tables.CUSTOMER_TYPES.SLUG))
            .relationType(record.get(Tables.CUSTOMER_RELATION_TYPES.SLUG))
            .displayName(record.get(Tables.CUSTOMERS.DISPLAY_NAME))
            .fullName(record.get(Tables.CUSTOMERS.FULL_NAME))
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
        .from(Tables.CUSTOMERS)
        .innerJoin(Tables.CUSTOMER_CODES).on(Tables.CUSTOMER_CODES.ID.eq(Tables.CUSTOMERS.CODE))
        .innerJoin(Tables.CUSTOMER_TYPES).on(Tables.CUSTOMER_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_TYPE))
        .innerJoin(Tables.CUSTOMER_RELATION_TYPES)
        .on(Tables.CUSTOMER_RELATION_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE))
        .where(
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMERS.VALID_FROM, Tables.CUSTOMERS.VALID_TO),
            searchCondition)
        .fetchOne()
        .value1();
  }

  private Condition buildSearchCondition(Pagination pagination) {
    Select<Record1<Integer>> hasEmail = create
        .selectOne()
        .from(Tables.EMAILS)
        .innerJoin(Tables.CUSTOMER_EMAILS).on(Tables.CUSTOMER_EMAILS.EMAIL.eq(Tables.EMAILS.ID))
        .where(
            JooqUtil.search(pagination.getQuery(), Tables.EMAILS.EMAIL),
            Tables.CUSTOMERS.CODE.eq(Tables.CUSTOMER_EMAILS.CUSTOMER),
            JooqUtil.between(
                DSL.currentOffsetDateTime(),
                Tables.CUSTOMER_EMAILS.VALID_FROM,
                Tables.CUSTOMER_EMAILS.VALID_TO));

    Select<Record1<Integer>> hasPhone = create
        .selectOne()
        .from(Tables.PHONE_NUMBERS)
        .innerJoin(Tables.CUSTOMER_PHONE_NUMBERS).on(Tables.CUSTOMER_PHONE_NUMBERS.PHONE.eq(Tables.PHONE_NUMBERS.ID))
        .where(
            JooqUtil.search(pagination.getQuery(), Tables.PHONE_NUMBERS.PHONE),
            Tables.CUSTOMERS.CODE.eq(Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER),
            JooqUtil.between(
                DSL.currentOffsetDateTime(),
                Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM,
                Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO));

    List<Condition> conditions = JooqUtil.search(
        pagination.getQuery(),
        Tables.CUSTOMER_CODES.CODE,
        Tables.CUSTOMER_TYPES.SLUG,
        Tables.CUSTOMER_TYPES.NAME,
        Tables.CUSTOMER_RELATION_TYPES.SLUG,
        Tables.CUSTOMER_RELATION_TYPES.NAME,
        Tables.CUSTOMERS.DISPLAY_NAME,
        Tables.CUSTOMERS.FULL_NAME);
    conditions.add(DSL.exists(hasEmail));
    conditions.add(DSL.exists(hasPhone));

    Condition searchCondition = DSL.or(conditions);
    return searchCondition;
  }

  @Override
  public CustomerFullDisplay get(CodeId code) {
    return buildSelect(code, DSL.currentOffsetDateTime()).fetchOne(this::map);
  }

  @Override
  public CustomerFullDisplay getForUpdate(CodeId code) {
    return buildSelect(code, DSL.currentOffsetDateTime())
        .forUpdate()
        .fetchOne(this::map);
  }

  private SelectConditionStep<? extends Record> buildSelect(CodeId code, Field<OffsetDateTime> atInstant) {
    return create
        .select(
            Tables.CUSTOMER_CODES.CODE,
            Tables.CUSTOMER_TYPES.SLUG,
            Tables.CUSTOMER_RELATION_TYPES.SLUG,
            Tables.CUSTOMERS.DISPLAY_NAME,
            Tables.CUSTOMERS.FULL_NAME,
            Tables.CUSTOMERS.DESCRIPTION,
            Tables.CUSTOMERS.VALID_FROM,
            Tables.CUSTOMERS.VALID_TO)
        .from(Tables.CUSTOMERS)
        .innerJoin(Tables.CUSTOMER_CODES).on(Tables.CUSTOMER_CODES.ID.eq(Tables.CUSTOMERS.CODE))
        .innerJoin(Tables.CUSTOMER_TYPES).on(Tables.CUSTOMER_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_TYPE))
        .innerJoin(Tables.CUSTOMER_RELATION_TYPES)
        .on(Tables.CUSTOMER_RELATION_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE))
        .where(
            Tables.CUSTOMERS.CODE.eq(code.getId()),
            JooqUtil.between(atInstant, Tables.CUSTOMERS.VALID_FROM, Tables.CUSTOMERS.VALID_TO));
  }

  private CustomerFullDisplay map(Record record) {
    return CustomerFullDisplay.builder()
        .code(record.get(Tables.CUSTOMER_CODES.CODE))
        .type(record.get(Tables.CUSTOMER_TYPES.SLUG))
        .relationType(record.get(Tables.CUSTOMER_RELATION_TYPES.SLUG))
        .displayName(record.get(Tables.CUSTOMERS.DISPLAY_NAME))
        .fullName(record.get(Tables.CUSTOMERS.FULL_NAME))
        .description(record.get(Tables.CUSTOMERS.DESCRIPTION))
        .validFrom(record.get(Tables.CUSTOMERS.VALID_FROM))
        .validTo(record.get(Tables.CUSTOMERS.VALID_TO))
        .build();
  }

  @Override
  public void deactivate(CodeId code) {
    create
        .update(Tables.CUSTOMERS)
        .set(Tables.CUSTOMERS.VALID_TO, DSL.currentOffsetDateTime())
        .where(
            Tables.CUSTOMERS.CODE.eq(code.getId()),
            Tables.CUSTOMERS.VALID_TO.isNull())
        .execute();
  }

}

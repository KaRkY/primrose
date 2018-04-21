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

import primrose.data.ContactRepository;
import primrose.jooq.JooqUtil;
import primrose.jooq.Tables;
import primrose.service.CodeId;
import primrose.service.Pagination;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;
import primrose.service.customer.CustomerFullDisplay;

@Repository
public class ContactRepositoryImpl implements ContactRepository {

  private DSLContext create;

  public ContactRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public CodeId generateCode() {
    return create
        .insertInto(Tables.CONTACT_CODES)
        .values(DSL.defaultValue(), DSL.defaultValue())
        .returning(
            Tables.CONTACT_CODES.ID,
            Tables.CONTACT_CODES.CODE)
        .fetchOne()
        .map(record -> CodeId.builder()
            .id(record.get(Tables.CONTACT_CODES.ID))
            .code(record.get(Tables.CONTACT_CODES.CODE))
            .build());
  }

  @Override
  public CodeId codeId(String code) {
    return create
        .select(Tables.CONTACT_CODES.ID, Tables.CONTACT_CODES.CODE)
        .from(Tables.CONTACT_CODES)
        .where(Tables.CONTACT_CODES.CODE.eq(code))
        .fetchOne(record -> CodeId.builder()
            .id(record.get(Tables.CONTACT_CODES.ID))
            .code(record.get(Tables.CONTACT_CODES.CODE))
            .build());
  }

  @Override
  public void create(CodeId code, ContactCreate contact) {
    create
        .insertInto(Tables.CONTACTS)
        .columns(
            Tables.CONTACTS.CODE,
            Tables.CONTACTS.FULL_NAME,
            Tables.CONTACTS.DESCRIPTION)
        .values(
            DSL.value(code.getId()),
            DSL.value(contact.getFullName()),
            DSL.value(contact.getDescription()))
        .returning(Tables.CONTACTS.ID)
        .fetchOne()
        .getId();
  }

  @Override
  public ImmutableList<ContactReducedDisplay> list(Pagination pagination) {
    int limit = pagination.getSize();
    int offset = pagination.getPage() * pagination.getSize();
    Condition searchCondition = buildSearchCondition(pagination);

    Field<String> primaryEmail = create
        .select(Tables.EMAILS.EMAIL)
        .from(Tables.CONTACT_EMAILS)
        .leftJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CONTACT_EMAILS.EMAIL))
        .where(
            Tables.CONTACT_EMAILS.CONTACT.eq(Tables.CONTACTS.CODE),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACT_EMAILS.VALID_FROM,
                Tables.CONTACT_EMAILS.VALID_TO))
        .orderBy(Tables.CONTACT_EMAILS.PRIM.desc(), Tables.CONTACT_EMAILS.VALID_FROM.asc())
        .limit(1)
        .<String>asField("primaryEmail");

    Field<String> primaryPhone = create
        .select(Tables.PHONE_NUMBERS.PHONE)
        .from(Tables.CONTACT_PHONE_NUMBERS)
        .leftJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CONTACT_PHONE_NUMBERS.PHONE))
        .where(
            Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(Tables.CONTACTS.CODE),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACT_PHONE_NUMBERS.VALID_FROM,
                Tables.CONTACT_PHONE_NUMBERS.VALID_TO))
        .orderBy(Tables.CONTACT_PHONE_NUMBERS.PRIM.desc(), Tables.CONTACT_PHONE_NUMBERS.VALID_FROM.asc())
        .limit(1)
        .<String>asField("primaryPhone");

    return create
        .select(
            Tables.CONTACT_CODES.CODE,
            Tables.CONTACTS.FULL_NAME,
            primaryEmail,
            primaryPhone)
        .from(Tables.CONTACTS)
        .innerJoin(Tables.CONTACT_CODES).on(Tables.CONTACT_CODES.ID.eq(Tables.CONTACTS.CODE))
        .where(
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACTS.VALID_FROM, Tables.CONTACTS.VALID_TO),
            searchCondition)
        .limit(limit)
        .offset(offset)
        .fetch()
        .stream()
        .map(record -> ContactReducedDisplay.builder()
            .code(record.get(Tables.CONTACT_CODES.CODE))
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
        .innerJoin(Tables.CONTACT_CODES).on(Tables.CONTACT_CODES.ID.eq(Tables.CONTACTS.CODE))
        .where(
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACTS.VALID_FROM, Tables.CONTACTS.VALID_TO),
            searchCondition)
        .fetchOne()
        .value1();
  }

  private Condition buildSearchCondition(Pagination pagination) {
    Select<Record1<Integer>> hasEmail = create
        .selectOne()
        .from(Tables.EMAILS)
        .innerJoin(Tables.CONTACT_EMAILS).on(Tables.CONTACT_EMAILS.EMAIL.eq(Tables.EMAILS.ID))
        .where(
            JooqUtil.search(pagination.getQuery(), Tables.EMAILS.EMAIL),
            Tables.CONTACTS.CODE.eq(Tables.CONTACT_EMAILS.CONTACT),
            JooqUtil.between(
                DSL.currentOffsetDateTime(),
                Tables.CONTACT_EMAILS.VALID_FROM,
                Tables.CONTACT_EMAILS.VALID_TO));

    Select<Record1<Integer>> hasPhone = create
        .selectOne()
        .from(Tables.PHONE_NUMBERS)
        .innerJoin(Tables.CONTACT_PHONE_NUMBERS).on(Tables.CONTACT_PHONE_NUMBERS.PHONE.eq(Tables.PHONE_NUMBERS.ID))
        .where(
            JooqUtil.search(pagination.getQuery(), Tables.PHONE_NUMBERS.PHONE),
            Tables.CONTACTS.CODE.eq(Tables.CONTACT_PHONE_NUMBERS.CONTACT),
            JooqUtil.between(
                DSL.currentOffsetDateTime(),
                Tables.CONTACT_PHONE_NUMBERS.VALID_FROM,
                Tables.CONTACT_PHONE_NUMBERS.VALID_TO));

    List<Condition> conditions = JooqUtil.search(
        pagination.getQuery(),
        Tables.CONTACT_CODES.CODE,
        Tables.CONTACTS.FULL_NAME);
    conditions.add(DSL.exists(hasEmail));
    conditions.add(DSL.exists(hasPhone));

    Condition searchCondition = DSL.or(conditions);
    return searchCondition;
  }

  @Override
  public ContactFullDisplay get(CodeId code) {
    return buildSelect(code, DSL.currentOffsetDateTime()).fetchOne(this::map);
  }

  @Override
  public ContactFullDisplay getForUpdate(CodeId code) {
    return buildSelect(code, DSL.currentOffsetDateTime())
        .forUpdate()
        .fetchOne(this::map);
  }

  private SelectConditionStep<? extends Record> buildSelect(CodeId code, Field<OffsetDateTime> atInstant) {
    return create
        .select(
            Tables.CONTACT_CODES.CODE,
            Tables.CONTACTS.FULL_NAME,
            Tables.CONTACTS.DESCRIPTION,
            Tables.CONTACTS.VALID_FROM,
            Tables.CONTACTS.VALID_TO)
        .from(Tables.CONTACTS)
        .innerJoin(Tables.CONTACT_CODES).on(Tables.CONTACT_CODES.ID.eq(Tables.CONTACTS.CODE))
        .where(
            Tables.CONTACTS.CODE.eq(code.getId()),
            JooqUtil.between(atInstant, Tables.CONTACTS.VALID_FROM, Tables.CONTACTS.VALID_TO));
  }

  private ContactFullDisplay map(Record record) {
    return ContactFullDisplay.builder()
        .code(record.get(Tables.CONTACT_CODES.CODE))
        .fullName(record.get(Tables.CONTACTS.FULL_NAME))
        .description(record.get(Tables.CONTACTS.DESCRIPTION))
        .validFrom(record.get(Tables.CONTACTS.VALID_FROM))
        .validTo(record.get(Tables.CONTACTS.VALID_TO))
        .build();
  }

  @Override
  public void deactivate(CodeId code) {
    create
        .update(Tables.CONTACTS)
        .set(Tables.CONTACTS.VALID_TO, DSL.currentOffsetDateTime())
        .where(
            Tables.CONTACTS.CODE.eq(code.getId()),
            Tables.CONTACTS.VALID_TO.isNull())
        .execute();
  }

}

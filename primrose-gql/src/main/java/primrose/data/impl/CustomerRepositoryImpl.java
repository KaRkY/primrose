package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.CustomerRepository;
import primrose.jooq.JooqUtil;
import primrose.jooq.Tables;
import primrose.service.CodeId;
import primrose.service.EmailFullDisplay;
import primrose.service.PhoneFullDisplay;
import primrose.service.Search;
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
         .map(record -> new CodeId(record.get(Tables.CUSTOMER_CODES.ID), record.get(Tables.CUSTOMER_CODES.CODE)));
   }

   @Override
   public CodeId codeId(String code) {
      return create
         .select(Tables.CUSTOMER_CODES.ID, Tables.CUSTOMER_CODES.CODE)
         .from(Tables.CUSTOMER_CODES)
         .where(Tables.CUSTOMER_CODES.CODE.eq(code))
         .fetchOne(record -> new CodeId(record.value1(), record.value2()));
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
   public List<CustomerReducedDisplay> search(Search search) {
      int limit = search.getSize();
      int offset = search.getPage() * search.getSize();
      return create
         .select(
            Tables.CUSTOMER_CODES.CODE,
            Tables.CUSTOMER_TYPES.SLUG,
            Tables.CUSTOMER_RELATION_TYPES.SLUG,
            Tables.CUSTOMERS.DISPLAY_NAME,
            Tables.CUSTOMERS.FULL_NAME,
            create
               .select(Tables.EMAILS.EMAIL)
               .from(Tables.CUSTOMER_EMAILS)
               .innerJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL))
               .where(Tables.CUSTOMER_EMAILS.CUSTOMER.eq(Tables.CUSTOMERS.CODE))
               .orderBy(Tables.CUSTOMER_EMAILS.PRIM.desc(), Tables.CUSTOMER_EMAILS.VALID_FROM.asc())
               .limit(1)
               .<String>asField(),
            create
               .select(Tables.PHONE_NUMBERS.PHONE)
               .from(Tables.CUSTOMER_PHONE_NUMBERS)
               .innerJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.PHONE))
               .where(Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(Tables.CUSTOMERS.CODE))
               .orderBy(Tables.CUSTOMER_PHONE_NUMBERS.PRIM.desc(), Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM.asc())
               .limit(1)
               .<String>asField())
         .from(Tables.CUSTOMERS)
         .innerJoin(Tables.CUSTOMER_CODES).on(Tables.CUSTOMER_CODES.ID.eq(Tables.CUSTOMERS.CODE))
         .innerJoin(Tables.CUSTOMER_TYPES).on(Tables.CUSTOMER_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_TYPE))
         .innerJoin(Tables.CUSTOMER_RELATION_TYPES).on(Tables.CUSTOMER_RELATION_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE))
         .where(
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMERS.VALID_FROM, Tables.CUSTOMERS.VALID_TO),
            DSL.or(
               DSL.upper(Tables.CUSTOMER_CODES.CODE).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMER_TYPES.SLUG).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMER_TYPES.NAME).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMER_RELATION_TYPES.SLUG).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMER_RELATION_TYPES.NAME).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMERS.DISPLAY_NAME).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMERS.FULL_NAME).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(search.getQuery()).in(create
                  .select(DSL.upper(Tables.EMAILS.EMAIL))
                  .from(Tables.EMAILS)
                  .innerJoin(Tables.CUSTOMER_EMAILS).on(Tables.CUSTOMER_EMAILS.EMAIL.eq(Tables.EMAILS.ID))
                  .where(
                     Tables.CUSTOMER_EMAILS.CUSTOMER.eq(Tables.CUSTOMER_CODES.ID),
                     JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_EMAILS.VALID_FROM, Tables.CUSTOMER_EMAILS.VALID_TO))),
               DSL.upper(search.getQuery()).in(create
                  .select(DSL.upper(Tables.PHONE_NUMBERS.PHONE))
                  .from(Tables.PHONE_NUMBERS)
                  .innerJoin(Tables.CUSTOMER_PHONE_NUMBERS)
                  .on(Tables.CUSTOMER_PHONE_NUMBERS.PHONE.eq(Tables.PHONE_NUMBERS.ID))
                  .where(
                     Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(Tables.CUSTOMER_CODES.ID),
                     JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM, Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO)))))
         .limit(limit)
         .offset(offset)
         .fetch()
         .map(record -> new CustomerReducedDisplay(
            record.value1(),
            record.value2(),
            record.value3(),
            record.value4(),
            record.value5(),
            record.value6(),
            record.value7()));
   }

   @Override
   public long count(Search search) {
      return create
         .selectCount()
         .from(Tables.CUSTOMERS)
         .innerJoin(Tables.CUSTOMER_CODES).on(Tables.CUSTOMER_CODES.ID.eq(Tables.CUSTOMERS.CODE))
         .innerJoin(Tables.CUSTOMER_TYPES).on(Tables.CUSTOMER_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_TYPE))
         .innerJoin(Tables.CUSTOMER_RELATION_TYPES).on(Tables.CUSTOMER_RELATION_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE))
         .where(
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMERS.VALID_FROM, Tables.CUSTOMERS.VALID_TO),
            DSL.or(
               DSL.upper(Tables.CUSTOMER_CODES.CODE).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMER_TYPES.SLUG).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMER_TYPES.NAME).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMER_RELATION_TYPES.SLUG).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMER_RELATION_TYPES.NAME).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMERS.DISPLAY_NAME).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(Tables.CUSTOMERS.FULL_NAME).like(DSL.concat(DSL.value("%"), DSL.upper(DSL.isnull(search.getQuery(), "")), DSL.value("%"))),
               DSL.upper(search.getQuery()).in(create
                  .select(DSL.upper(Tables.EMAILS.EMAIL))
                  .from(Tables.EMAILS)
                  .innerJoin(Tables.CUSTOMER_EMAILS).on(Tables.CUSTOMER_EMAILS.EMAIL.eq(Tables.EMAILS.ID))
                  .where(
                     Tables.CUSTOMER_EMAILS.CUSTOMER.eq(Tables.CUSTOMER_CODES.ID),
                     JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_EMAILS.VALID_FROM, Tables.CUSTOMER_EMAILS.VALID_TO))),
               DSL.upper(search.getQuery()).in(create
                  .select(DSL.upper(Tables.PHONE_NUMBERS.PHONE))
                  .from(Tables.PHONE_NUMBERS)
                  .innerJoin(Tables.CUSTOMER_PHONE_NUMBERS)
                  .on(Tables.CUSTOMER_PHONE_NUMBERS.PHONE.eq(Tables.PHONE_NUMBERS.ID))
                  .where(
                     Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(Tables.CUSTOMER_CODES.ID),
                     JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM, Tables.CUSTOMER_PHONE_NUMBERS.VALID_TO)))))
         .fetchOne()
         .value1();
   }

   @Override
   public CustomerFullDisplay get(CodeId code, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones) {
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
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMERS.VALID_FROM, Tables.CUSTOMERS.VALID_TO))
         .fetchOne(record -> new CustomerFullDisplay(
            record.value1(),
            record.value2(),
            record.value3(),
            record.value4(),
            record.value5(),
            record.value6(),
            emails,
            phones,
            record.value7(),
            record.value8()));
   }

   @Override
   public CustomerFullDisplay getForUpdate(CodeId code, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones) {
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
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMERS.VALID_FROM, Tables.CUSTOMERS.VALID_TO))
         .forUpdate()
         .fetchOne(record -> new CustomerFullDisplay(
            record.value1(),
            record.value2(),
            record.value3(),
            record.value4(),
            record.value5(),
            record.value6(),
            emails,
            phones,
            record.value7(),
            record.value8()));
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

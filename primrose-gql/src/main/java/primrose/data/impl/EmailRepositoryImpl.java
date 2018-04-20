package primrose.data.impl;

import java.util.List;
import java.util.Set;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.EmailRepository;
import primrose.jooq.JooqUtil;
import primrose.jooq.Tables;
import primrose.service.CodeId;
import primrose.service.EmailFullDisplay;

@Repository
public class EmailRepositoryImpl implements EmailRepository {
   private DSLContext create;

   public EmailRepositoryImpl(DSLContext create) {
      this.create = create;
   }

   @Override
   public long save(String email) {
      return create
         .insertInto(Tables.EMAILS)
         .columns(Tables.EMAILS.EMAIL)
         .values(email)
         .returning(Tables.EMAILS.ID)
         .fetchOne()
         .getId();
   }

   @Override
   public Long get(String email) {
      return create
         .select(Tables.EMAILS.ID)
         .from(Tables.EMAILS)
         .where(Tables.EMAILS.EMAIL.likeIgnoreCase(email))
         .fetchOne(0, Long.class);
   }

   @Override
   public void assignToCustomer(CodeId code, long emailId, String emailType, Boolean primary) {
      create
         .insertInto(Tables.CUSTOMER_EMAILS)
         .columns(
            Tables.CUSTOMER_EMAILS.CUSTOMER,
            Tables.CUSTOMER_EMAILS.EMAIL,
            Tables.CUSTOMER_EMAILS.EMAIL_TYPE,
            Tables.CUSTOMER_EMAILS.PRIM)
         .values(
            DSL.value(code.getId()),
            DSL.value(emailId),
            create
               .select(Tables.EMAIL_TYPES.ID)
               .from(Tables.EMAIL_TYPES)
               .where(Tables.EMAIL_TYPES.SLUG.eq(emailType))
               .asField(),
            DSL.value(primary != null ? primary : false))
         .execute();
   }

   @Override
   public void assignToContact(CodeId code, long emailId, String emailType, Boolean primary) {
      create
         .insertInto(Tables.CONTACT_EMAILS)
         .columns(
            Tables.CONTACT_EMAILS.CONTACT,
            Tables.CONTACT_EMAILS.EMAIL,
            Tables.CONTACT_EMAILS.EMAIL_TYPE,
            Tables.CONTACT_EMAILS.PRIM)
         .values(
            DSL.value(code.getId()),
            DSL.value(emailId),
            create
               .select(Tables.EMAIL_TYPES.ID)
               .from(Tables.EMAIL_TYPES)
               .where(Tables.EMAIL_TYPES.SLUG.eq(emailType))
               .asField(),
            DSL.value(primary != null ? primary : false))
         .execute();
   }

   @Override
   public List<EmailFullDisplay> customerEmails(CodeId code) {
      return create
         .select(
            Tables.EMAILS.ID,
            Tables.EMAIL_TYPES.SLUG,
            Tables.EMAILS.EMAIL,
            Tables.CUSTOMER_EMAILS.PRIM)
         .from(Tables.CUSTOMER_EMAILS)
         .innerJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL))
         .innerJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL_TYPE))
         .where(
            Tables.CUSTOMER_EMAILS.CUSTOMER.eq(code.getId()),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_EMAILS.VALID_FROM, Tables.CUSTOMER_EMAILS.VALID_TO))
         .fetch()
         .map(record -> new EmailFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
   }

   @Override
   public List<EmailFullDisplay> customerEmailsForUpdate(CodeId code) {
      return create
         .select(
            Tables.EMAILS.ID,
            Tables.EMAIL_TYPES.SLUG,
            Tables.EMAILS.EMAIL,
            Tables.CUSTOMER_EMAILS.PRIM)
         .from(Tables.CUSTOMER_EMAILS)
         .innerJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL))
         .innerJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL_TYPE))
         .where(
            Tables.CUSTOMER_EMAILS.CUSTOMER.eq(code.getId()),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_EMAILS.VALID_FROM, Tables.CUSTOMER_EMAILS.VALID_TO))
         .forUpdate()
         .fetch()
         .map(record -> new EmailFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
   }

   @Override
   public List<EmailFullDisplay> contactEmails(CodeId code) {
      return create
         .select(
            Tables.EMAILS.ID,
            Tables.EMAIL_TYPES.SLUG,
            Tables.EMAILS.EMAIL,
            Tables.CONTACT_EMAILS.PRIM)
         .from(Tables.CONTACT_EMAILS)
         .innerJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CONTACT_EMAILS.EMAIL))
         .innerJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CONTACT_EMAILS.EMAIL_TYPE))
         .where(
            Tables.CONTACT_EMAILS.CONTACT.eq(code.getId()),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACT_EMAILS.VALID_FROM, Tables.CONTACT_EMAILS.VALID_TO))
         .fetch()
         .map(record -> new EmailFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
   }

   @Override
   public List<EmailFullDisplay> contactEmailsForUpdate(CodeId code) {
      return create
         .select(
            Tables.EMAILS.ID,
            Tables.EMAIL_TYPES.SLUG,
            Tables.EMAILS.EMAIL,
            Tables.CONTACT_EMAILS.PRIM)
         .from(Tables.CONTACT_EMAILS)
         .innerJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CONTACT_EMAILS.EMAIL))
         .innerJoin(Tables.EMAIL_TYPES).on(Tables.EMAIL_TYPES.ID.eq(Tables.CONTACT_EMAILS.EMAIL_TYPE))
         .where(
            Tables.CONTACT_EMAILS.CONTACT.eq(code.getId()),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACT_EMAILS.VALID_FROM, Tables.CONTACT_EMAILS.VALID_TO))
         .forUpdate()
         .fetch()
         .map(record -> new EmailFullDisplay(record.value1(), record.value2(), record.value3(), record.value4()));
   }

   @Override
   public boolean isAssignedToCustomer(CodeId code, long emailId) {
      return create
         .selectOne()
         .from(Tables.CUSTOMER_EMAILS)
         .where(
            Tables.CUSTOMER_EMAILS.CUSTOMER.eq(code.getId()),
            Tables.CUSTOMER_EMAILS.EMAIL.eq(emailId),
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CUSTOMER_EMAILS.VALID_FROM, Tables.CUSTOMER_EMAILS.VALID_TO))
         .fetchOne(0, Integer.class) != null;
   }

   @Override
   public boolean isAssignedToContact(CodeId code, long emailId) {
      return create
         .selectOne()
         .from(Tables.CONTACT_EMAILS)
         .where(
            Tables.CONTACT_EMAILS.CONTACT.eq(code.getId()),
            Tables.CONTACT_EMAILS.EMAIL.eq(emailId),
            DSL.currentOffsetDateTime()
               .between(Tables.CONTACT_EMAILS.VALID_FROM)
               .and(DSL.isnull(Tables.CONTACT_EMAILS.VALID_TO, DSL.currentOffsetDateTime())))
         .fetchOne(0, Integer.class) != null;
   }

   @Override
   public void removeFromCustomer(CodeId code, long emailId) {
      create
         .update(Tables.CUSTOMER_EMAILS)
         .set(Tables.CUSTOMER_EMAILS.VALID_TO, DSL.currentOffsetDateTime())
         .where(
            Tables.CUSTOMER_EMAILS.CUSTOMER.eq(code.getId()),
            Tables.CUSTOMER_EMAILS.EMAIL.eq(emailId),
            Tables.CUSTOMER_EMAILS.VALID_TO.isNull())
         .execute();
   }

   @Override
   public void removeExceptFromCustomer(CodeId code, Set<Long> emailIds) {
      create
         .update(Tables.CUSTOMER_EMAILS)
         .set(Tables.CUSTOMER_EMAILS.VALID_TO, DSL.currentOffsetDateTime())
         .where(
            Tables.CUSTOMER_EMAILS.CUSTOMER.eq(code.getId()),
            Tables.CUSTOMER_EMAILS.EMAIL.notIn(emailIds),
            Tables.CUSTOMER_EMAILS.VALID_TO.isNull())
         .execute();
   }

   @Override
   public void removeFromContact(CodeId code, long emailId) {
      create
         .update(Tables.CONTACT_EMAILS)
         .set(Tables.CONTACT_EMAILS.VALID_TO, DSL.currentOffsetDateTime())
         .where(
            Tables.CONTACT_EMAILS.CONTACT.eq(code.getId()),
            Tables.CONTACT_EMAILS.EMAIL.eq(emailId),
            Tables.CONTACT_EMAILS.VALID_TO.isNull())
         .execute();
   }

   @Override
   public void removeExceptFromContact(CodeId code, Set<Long> emailIds) {
      create
         .update(Tables.CONTACT_EMAILS)
         .set(Tables.CONTACT_EMAILS.VALID_TO, DSL.currentOffsetDateTime())
         .where(
            Tables.CONTACT_EMAILS.CONTACT.eq(code.getId()),
            Tables.CONTACT_EMAILS.EMAIL.notIn(emailIds),
            Tables.CONTACT_EMAILS.VALID_TO.isNull())
         .execute();
   }
}

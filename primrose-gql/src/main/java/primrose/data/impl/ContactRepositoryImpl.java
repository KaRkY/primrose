package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.ContactRepository;
import primrose.jooq.JooqUtil;
import primrose.jooq.Tables;
import primrose.service.CodeId;
import primrose.service.EmailFullDisplay;
import primrose.service.PhoneFullDisplay;
import primrose.service.Search;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;

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
        .map(record -> new CodeId(record.get(Tables.CONTACT_CODES.ID), record.get(Tables.CONTACT_CODES.CODE)));
  }

  @Override
  public CodeId codeId(String code) {
    return create
        .select(Tables.CONTACT_CODES.ID, Tables.CONTACT_CODES.CODE)
        .from(Tables.CONTACT_CODES)
        .where(Tables.CONTACT_CODES.CODE.eq(code))
        .fetchOne(record -> new CodeId(record.value1(), record.value2()));
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
  public List<ContactReducedDisplay> search(Search search) {
    int limit = search.getSize();
    int offset = search.getPage() * search.getSize();
    return create
        .select(
            Tables.CONTACT_CODES.CODE,
            Tables.CONTACTS.FULL_NAME,
            create
                .select(Tables.EMAILS.EMAIL)
                .from(Tables.CONTACT_EMAILS)
                .leftJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CONTACT_EMAILS.EMAIL))
                .where(Tables.CONTACT_EMAILS.CONTACT.eq(Tables.CONTACTS.ID))
                .orderBy(Tables.CONTACT_EMAILS.PRIM.desc(), Tables.CONTACT_EMAILS.VALID_FROM.asc())
                .limit(1)
                .<String>asField(),
            create
                .select(Tables.PHONE_NUMBERS.PHONE)
                .from(Tables.CONTACT_PHONE_NUMBERS)
                .leftJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CONTACT_PHONE_NUMBERS.PHONE))
                .where(Tables.CONTACT_PHONE_NUMBERS.CONTACT.eq(Tables.CONTACTS.ID))
                .orderBy(Tables.CONTACT_PHONE_NUMBERS.PRIM.desc(), Tables.CONTACT_PHONE_NUMBERS.VALID_FROM.asc())
                .limit(1)
                .<String>asField())
        .from(Tables.CONTACTS)
        .innerJoin(Tables.CONTACT_CODES).on(Tables.CONTACT_CODES.ID.eq(Tables.CONTACTS.CODE))
        .where(JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACTS.VALID_FROM, Tables.CONTACTS.VALID_TO))
        .limit(limit)
        .offset(offset)
        .fetch()
        .map(record -> new ContactReducedDisplay(
            record.value1(),
            record.value2(),
            record.value3(),
            record.value4()));
  }

  @Override
  public long count(Search search) {
    return create
        .selectCount()
        .from(Tables.CONTACTS)
        .where(JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACTS.VALID_FROM, Tables.CONTACTS.VALID_TO))
        .fetchOne()
        .value1();
  }

  @Override
  public ContactFullDisplay get(CodeId code, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones) {
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
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACTS.VALID_FROM, Tables.CONTACTS.VALID_TO))
        .fetchOne(record -> new ContactFullDisplay(
            record.value1(),
            record.value2(),
            record.value3(),
            emails,
            phones,
            record.value4(),
            record.value5()));
  }

  @Override
  public ContactFullDisplay getForUpdate(CodeId code, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones) {
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
            JooqUtil.between(DSL.currentOffsetDateTime(), Tables.CONTACTS.VALID_FROM, Tables.CONTACTS.VALID_TO))
        .forUpdate()
        .fetchOne(record -> new ContactFullDisplay(
            record.value1(),
            record.value2(),
            record.value3(),
            emails,
            phones,
            record.value4(),
            record.value5()));
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

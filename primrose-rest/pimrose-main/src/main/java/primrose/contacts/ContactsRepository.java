package primrose.contacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import pimrose.jooq.DefaultCatalog;
import pimrose.jooq.Primrose;
import pimrose.jooq.Sequences;
import pimrose.jooq.tables.TAccountContactTitles;
import pimrose.jooq.tables.TAccountContacts;
import pimrose.jooq.tables.TAccounts;
import pimrose.jooq.tables.TContactSearch;
import pimrose.jooq.tables.TContacts;
import primrose.repositories.SearchParameters;
import primrose.repositories.SearchResult;

@Repository
public class ContactsRepository {
  private static final Primrose              PRIMROSE              = DefaultCatalog.DEFAULT_CATALOG.PRIMROSE;
  private static final TContacts             CONTACT               = PRIMROSE.T_CONTACTS.as("contact");
  private static final TAccounts             ACCOUNT               = PRIMROSE.T_ACCOUNTS.as("account");
  private static final TAccountContacts      ACCOUNT_CONTACT       = PRIMROSE.T_ACCOUNT_CONTACTS.as("account_contact");
  private static final TAccountContactTitles ACCOUNT_CONTACT_TITLE = PRIMROSE.T_ACCOUNT_CONTACT_TITLES
    .as("account_contact_title");
  private static final TContactSearch        CONTACT_SEARCH        = PRIMROSE.T_CONTACT_SEARCH.as("contact_search");
  private final DSLContext                   create;

  public ContactsRepository(final DSLContext create) {
    this.create = create;
  }

  public long getNewId() {
    return create.select(Sequences.S_CONTACT.nextval()).fetchOne().value1();
  }

  public long getNewContactTitleId() {
    return create.select(Sequences.S_CONTACT_TITLES.nextval()).fetchOne().value1();
  }

  public SearchResult<Contact> search(final SearchParameters searchParameters) {
    final List<Condition> conditions = new ArrayList<>();

    if (searchParameters.getQuery() != null) {
      conditions.add(DSL
        .condition(
          "{0} @@ plainto_tsquery('pg_catalog.english', {1})",
          CONTACT_SEARCH.FULL_TEXT_SEARCH,
          searchParameters.getQuery()));
    }

    final Integer size = searchParameters.getSize();
    final Integer page = searchParameters.getPage();

    final Table<Record2<Long, Integer>> filtered = create
      .select(CONTACT.CONTACT_ID, DSL.count().over().as("count"))
      .from(CONTACT)
      .leftJoin(CONTACT_SEARCH).on(CONTACT_SEARCH.CONTACT_ID.eq(CONTACT.CONTACT_ID))
      .where(conditions)
      .limit(size == null ? Integer.MAX_VALUE : size)
      .offset(page == null || size == null ? 0 : page * size - size)
      .asTable("filtered_contact");

    return SearchResult.of(create
      .select(
        filtered.field(1, Integer.class),
        CONTACT.CONTACT_ID,
        CONTACT.PERSON_NAME,
        CONTACT.EMAIL,
        CONTACT.PHONE)
      .from(filtered)
      .leftJoin(CONTACT).on(CONTACT.CONTACT_ID.eq(filtered.field(0, Long.class)))
      .fetchGroups(filtered.field(1, Integer.class), this::map));
  }

  public void insert(final Contact contact, final long addressId) {
    create
      .insertInto(PRIMROSE.T_CONTACTS)
      .columns(
        PRIMROSE.T_CONTACTS.CONTACT_ID,
        PRIMROSE.T_CONTACTS.PERSON_NAME,
        PRIMROSE.T_CONTACTS.EMAIL,
        PRIMROSE.T_CONTACTS.PHONE,
        PRIMROSE.T_CONTACTS.ADDRESS_ID)
      .values(
        DSL.value(contact.getId()),
        DSL.value(contact.getPersonName()),
        DSL.value(contact.getEmail()),
        DSL.value(contact.getPhone()),
        DSL.value(addressId))
      .execute();
  }

  public void insert(final String contactTitle, final long contactId, final String accountCode) {
    create
      .insertInto(PRIMROSE.T_ACCOUNT_CONTACTS)
      .columns(
        PRIMROSE.T_ACCOUNT_CONTACTS.ACCOUNT_CODE,
        PRIMROSE.T_ACCOUNT_CONTACTS.CONTACT_ID,
        PRIMROSE.T_ACCOUNT_CONTACTS.ACCOUNT_CONTACT_TITLE_ID)
      .values(
        DSL.value(accountCode),
        DSL.value(contactId),
        create.select(PRIMROSE.T_ACCOUNT_CONTACT_TITLES.ACCOUNT_CONTACT_TITLE_ID)
          .from(PRIMROSE.T_ACCOUNT_CONTACT_TITLES)
          .where(PRIMROSE.T_ACCOUNT_CONTACT_TITLES.ACCOUNT_CONTACT_TITLE.eq(DSL.value(contactTitle)))
          .asField())
      .execute();
  }

  public void insert(final long contactTitleId, final String contactTitle) {
    create
      .insertInto(PRIMROSE.T_ACCOUNT_CONTACT_TITLES)
      .columns(
        PRIMROSE.T_ACCOUNT_CONTACT_TITLES.ACCOUNT_CONTACT_TITLE_ID,
        PRIMROSE.T_ACCOUNT_CONTACT_TITLES.ACCOUNT_CONTACT_TITLE)
      .values(
        DSL.value(contactTitleId),
        DSL.value(contactTitle))
      .execute();
  }

  public Optional<Long> getContactTitle(final String contactTitle) {
    return create
      .select(PRIMROSE.T_ACCOUNT_CONTACT_TITLES.ACCOUNT_CONTACT_TITLE_ID)
      .from(PRIMROSE.T_ACCOUNT_CONTACT_TITLES)
      .where(DSL.upper(PRIMROSE.T_ACCOUNT_CONTACT_TITLES.ACCOUNT_CONTACT_TITLE).eq(DSL.upper(contactTitle)))
      .fetchOptional(PRIMROSE.T_ACCOUNT_CONTACT_TITLES.ACCOUNT_CONTACT_TITLE_ID);
  }

  public Map<String, List<Contact>> getByAccountUrl(final String account) {
    return create
      .select(
        CONTACT.CONTACT_ID,
        CONTACT.CONTACT_URL,
        CONTACT.PERSON_NAME,
        CONTACT.EMAIL,
        CONTACT.PHONE,
        ACCOUNT_CONTACT_TITLE.ACCOUNT_CONTACT_TITLE)
      .from(CONTACT)
      .leftJoin(ACCOUNT_CONTACT).on(ACCOUNT_CONTACT.CONTACT_ID.eq(CONTACT.CONTACT_ID))
      .leftJoin(ACCOUNT).on(ACCOUNT.ACCOUNT_CODE.eq(ACCOUNT_CONTACT.ACCOUNT_CODE))
      .leftJoin(ACCOUNT_CONTACT_TITLE).on(ACCOUNT_CONTACT_TITLE.ACCOUNT_CONTACT_TITLE_ID.eq(ACCOUNT_CONTACT.ACCOUNT_CONTACT_TITLE_ID))
      .where(ACCOUNT.ACCOUNT_URL.eq(DSL.value(account)))
      .fetchGroups(ACCOUNT_CONTACT_TITLE.ACCOUNT_CONTACT_TITLE, this::map);
  }

  private Contact map(final Record record) {
    final Contact con = new Contact();

    con.setId(record.getValue(PRIMROSE.T_CONTACTS.CONTACT_ID));
    con.setUrlCode(record.getValue(PRIMROSE.T_CONTACTS.CONTACT_URL));
    con.setPersonName(record.getValue(PRIMROSE.T_CONTACTS.PERSON_NAME));
    con.setEmail(record.getValue(PRIMROSE.T_CONTACTS.EMAIL));
    con.setPhone(record.getValue(PRIMROSE.T_CONTACTS.PHONE));

    return con;
  }

}

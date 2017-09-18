package primrose.accounts.contacts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import primrose.contacts.Contact;
import primrose.contacts.ImmutableContact;
import primrose.spring.SQLLoader;

@Repository
public class AccountsContactsRepository {
  private final SQLLoader loader;
  private final NamedParameterJdbcTemplate template;

  public AccountsContactsRepository(
    final SQLLoader loader,
    final NamedParameterJdbcTemplate template) {
    this.loader = loader;
    this.template = template;
  }

  public void insert(final String contactType, final String contactCode, final long accountId) {
    template.update(
      loader.loadSQL("primrose.accounts.contacts.insertContactCodeAccountId"),
      new MapSqlParameterSource()
        .addValue("account_id", accountId)
        .addValue("contact_code", contactCode)
        .addValue("account_contact_type_code", contactType));
  }

  public void insert(final String contactType, final String contactCode, final String accountCode) {
    template.update(
      loader.loadSQL("primrose.accounts.contacts.insertContactCodeAccountCode"),
      new MapSqlParameterSource()
        .addValue("account_code", accountCode)
        .addValue("contact_code", contactCode)
        .addValue("account_contact_type_code", contactType));
  }

  public void insert(final String contactType, final long contactId, final long accountId) {
    template.update(
      loader.loadSQL("primrose.accounts.contacts.insertContactIdAccountId"),
      new MapSqlParameterSource()
        .addValue("account_id", accountId)
        .addValue("contact_id", contactId)
        .addValue("account_contact_type_code", contactType));
  }

  public void insert(final String contactType, final long contactId, final String accountCode) {
    template.update(
      loader.loadSQL("primrose.accounts.contacts.insertContactIdAccountCode"),
      new MapSqlParameterSource()
        .addValue("account_code", accountCode)
        .addValue("contact_id", contactId)
        .addValue("account_contact_type_code", contactType));
  }

  public Multimap<String, Contact> listByAccountId(final long accountId) {
    final ResultSetExtractor<Multimap<String, Contact>> rse = this::map;
    return template
      .query(
        loader.loadSQL("primrose.accounts.contacts.listByAccountCode"),
        new MapSqlParameterSource()
          .addValue("account_id", accountId),
        rse);
  }

  public Multimap<String, Contact> listByAccountCode(final String accountCode) {
    final ResultSetExtractor<Multimap<String, Contact>> rse = this::map;
    return template
      .query(
        loader.loadSQL("primrose.accounts.contacts.listByAccountCode"),
        new MapSqlParameterSource()
          .addValue("account_code", accountCode),
        rse);
  }

  public List<ContactType> listTypes() {
    return template
      .query(
        loader.loadSQL("primrose.accounts.contacts.listTypes"),
        EmptySqlParameterSource.INSTANCE,
        this::mapType);
  }

  private Multimap<String, Contact> map(final ResultSet rs) throws SQLException {
    final Multimap<String, Contact> result = HashMultimap.create();

    int rownum = 1;
    while (rs.next()) {
      result.put(rs.getString("account_contact_type_code"), map(rs, rownum));
      rownum++;
    }
    return result;
  }

  private Contact map(final ResultSet rs, final int rownum) throws SQLException {
    return ImmutableContact.builder()
      .code(rs.getString("contact_code"))
      .personName(rs.getString("person_name"))
      .phone(rs.getString("phone"))
      .email(rs.getString("email"))
      .build();
  }

  private ContactType mapType(final ResultSet rs, final int rownum) throws SQLException {
    return ImmutableContactType.builder()
      .code(rs.getString("account_contact_type_code"))
      .def(rs.getString("account_contact_type_default"))
      .build();
  }
}

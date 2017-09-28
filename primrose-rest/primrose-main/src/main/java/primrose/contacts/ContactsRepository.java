package primrose.contacts;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import primrose.spring.SQLLoader;

@Repository
public class ContactsRepository {
  private final SQLLoader loader;
  private final NamedParameterJdbcTemplate template;

  public ContactsRepository(
    final SQLLoader loader,
    final NamedParameterJdbcTemplate template) {
    this.loader = loader;
    this.template = template;
  }

  public void insert(final long contactId, final Contact contact) {
    template.update(
      loader.loadSQL("primrose.contacts.insert"),
      new MapSqlParameterSource()
        .addValue("contact_id", contactId)
        .addValue("email", contact.email())
        .addValue("person_name", contact.personName())
        .addValue("phone", contact.phone()));
  }

  public Contact getById(final long contactId) {
    return template
      .queryForObject(
        loader.loadSQL("primrose.contacts.getById"),
        new MapSqlParameterSource()
          .addValue("contact_id", contactId),
        this::map);
  }

  public Contact getByCode(final String contactCode) {
    return template
      .queryForObject(
        loader.loadSQL("primrose.contacts.getByCode"),
        new MapSqlParameterSource()
          .addValue("contact_code", contactCode),
        this::map);
  }

  private Contact map(final ResultSet rs, final int rownum) throws SQLException {
    return ImmutableContact.builder()
      .code(rs.getString("contact_code"))
      .personName(rs.getString("person_name"))
      .phone(rs.getString("phone"))
      .email(rs.getString("email"))
      .build();
  }
}

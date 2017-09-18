package primrose.accounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import primrose.spring.JdbcUtil;
import primrose.spring.SQLLoader;

@Repository
public class AccountsRepository {

  private final SQLLoader loader;
  private final NamedParameterJdbcTemplate template;

  public AccountsRepository(
    final SQLLoader loader,
    final NamedParameterJdbcTemplate template) {
    this.loader = loader;
    this.template = template;
  }

  public void insert(final long accountId, final Account account) {
    template.update(
      loader.loadSQL("primrose.accounts.insert"),
      new MapSqlParameterSource()
        .addValue("account_id", accountId)
        .addValue("account_type_code", account.type())
        .addValue("display_name", account.displayName())
        .addValue("full_name", account.fullName())
        .addValue("email", account.email())
        .addValue("phone", account.phone())
        .addValue("website", account.website())
        .addValue("description", account.description()));
  }

  public Account getById(final long accountId) {
    return template
      .queryForObject(
        loader.loadSQL("primrose.accounts.getById"),
        new MapSqlParameterSource()
          .addValue("account_id", accountId),
        this::map);
  }

  public Account getByCode(final String accountCode) {
    return template
      .queryForObject(
        loader.loadSQL("primrose.accounts.getByCode"),
        new MapSqlParameterSource()
          .addValue("account_code", accountCode),
        this::map);
  }

  public List<Account> getAllPaginated(final long pageNumber, final long pageSize) {
    return template
      .query(
        loader.loadSQL("primrose.accounts.getAllPaginated"),
        new MapSqlParameterSource()
          .addValue("page_number", pageNumber)
          .addValue("page_size", pageSize),
        this::map);
  }

  public long countAll() {
    return template
      .queryForObject(
        loader.loadSQL("primrose.accounts.countAll"),
        EmptySqlParameterSource.INSTANCE,
        (rs, rownum) -> rs.getLong(1));
  }

  public List<AccountType> listTypes() {
    return template
      .query(
        loader.loadSQL("primrose.accounts.listTypes"),
        EmptySqlParameterSource.INSTANCE,
        this::mapType);
  }

  private Account map(final ResultSet rs, final int rownum) throws SQLException {
    return ImmutableAccount.builder()
      .code(rs.getString("account_code"))
      .type(rs.getString("account_type_code"))
      .displayName(rs.getString("display_name"))
      .fullName(rs.getString("full_name"))
      .email(rs.getString("email"))
      .phone(rs.getString("phone"))
      .website(rs.getString("website"))
      .description(rs.getString("description"))
      .validFrom(JdbcUtil.toLocalDateTime(rs.getTimestamp("valid_from")))
      .validTo(JdbcUtil.toLocalDateTime(rs.getTimestamp("valid_to")))
      .build();
  }

  private AccountType mapType(final ResultSet rs, final int rownum) throws SQLException {
    return ImmutableAccountType.builder()
      .code(rs.getString("account_type_code"))
      .def(rs.getString("account_type_default"))
      .build();
  }
}

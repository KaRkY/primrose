package primrose.accounts.addresses;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import primrose.addresses.Address;
import primrose.addresses.ImmutableAddress;
import primrose.spring.SQLLoader;

@Repository
public class AccountsAddressesRepository {

  private final SQLLoader loader;
  private final NamedParameterJdbcTemplate template;

  public AccountsAddressesRepository(
    final SQLLoader loader,
    final NamedParameterJdbcTemplate template) {
    this.loader = loader;
    this.template = template;
  }

  public void insert(final String addressType, final long addressId, final long accountId) {
    template.update(
      loader.loadSQL("primrose.accounts.addresses.insertByAccountId"),
      new MapSqlParameterSource()
        .addValue("account_id", accountId)
        .addValue("address_id", addressId)
        .addValue("account_address_type_code", addressType));
  }

  public void insert(final String addressType, final long addressId, final String accountCode) {
    template.update(
      loader.loadSQL("primrose.accounts.addresses.insertByAccountCode"),
      new MapSqlParameterSource()
        .addValue("account_code", accountCode)
        .addValue("address_id", addressId)
        .addValue("account_address_type_code", addressType));
  }

  public Multimap<String, Address> getByAccountId(final long accountId) {
    final ResultSetExtractor<Multimap<String, Address>> rse = this::map;
    return template
      .query(
        loader.loadSQL("primrose.accounts.addresses.getByAccountId"),
        new MapSqlParameterSource()
          .addValue("account_id", accountId),
        rse);
  }

  public Multimap<String, Address> getByAccountCode(final String accountCode) {
    final ResultSetExtractor<Multimap<String, Address>> rse = this::map;
    return template
      .query(
        loader.loadSQL("primrose.accounts.addresses.getByAccountCode"),
        new MapSqlParameterSource()
          .addValue("account_code", accountCode),
        rse);
  }

  private Multimap<String, Address> map(final ResultSet rs) throws SQLException {
    final Multimap<String, Address> result = HashMultimap.create();

    int rownum = 1;
    while (rs.next()) {
      result.put(rs.getString("account_address_type_code"), map(rs, rownum));
      rownum++;
    }
    return result;
  }

  private Address map(final ResultSet rs, final int rownum) throws SQLException {
    return ImmutableAddress.builder()
      .code(rs.getString("address_code"))
      .street(rs.getString("street"))
      .state(rs.getString("state"))
      .postalCode(rs.getString("postal_code"))
      .country(rs.getString("country"))
      .city(rs.getString("city"))
      .build();
  }
}

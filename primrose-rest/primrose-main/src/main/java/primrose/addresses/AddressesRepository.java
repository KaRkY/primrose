package primrose.addresses;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import primrose.spring.SQLLoader;

@Repository
public class AddressesRepository {
  private final SQLLoader loader;
  private final NamedParameterJdbcTemplate template;

  public AddressesRepository(
    final SQLLoader loader,
    final NamedParameterJdbcTemplate template) {
    this.loader = loader;
    this.template = template;
  }

  public void insert(final long addressId, final Address address) {
    template.update(
      loader.loadSQL("primrose.addresses.insert"),
      new MapSqlParameterSource()
        .addValue("address_id", addressId)
        .addValue("street", address.street())
        .addValue("city", address.city())
        .addValue("postal_code", address.postalCode())
        .addValue("state", address.state())
        .addValue("country", address.country()));
  }

  public Address getById(final long addressId) {
    return template
      .queryForObject(
        loader.loadSQL("primrose.addresses.getById"),
        new MapSqlParameterSource()
          .addValue("address_id", addressId),
        this::map);
  }

  public Address getByCode(final long addressCode) {
    return template
      .queryForObject(
        loader.loadSQL("primrose.addresses.getByCode"),
        new MapSqlParameterSource()
          .addValue("address_code", addressCode),
        this::map);
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

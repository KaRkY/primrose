package primrose.addresses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
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

  public long getNewId() {
    return template.queryForObject(
      loader.loadSQL("primrose.addresses.newId"),
      EmptySqlParameterSource.INSTANCE,
      (rs, rownum) -> rs.getLong(1));
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



  public List<AddressType> getTypes() {
    return template
      .query(
        loader.loadSQL("primrose.addresses.getTypes"),
        EmptySqlParameterSource.INSTANCE,
        this::mapType);
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

  private AddressType mapType(final ResultSet rs, final int rownum) throws SQLException {
    return ImmutableAddressType.builder()
      .code(rs.getString("account_address_type_code"))
      .def(rs.getString("account_address_type_default"))
      .build();
  }
}

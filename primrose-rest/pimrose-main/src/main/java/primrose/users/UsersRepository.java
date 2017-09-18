package primrose.users;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import primrose.spring.SQLLoader;

@Repository
public class UsersRepository {

  private final SQLLoader loader;
  private final NamedParameterJdbcTemplate template;

  public UsersRepository(
    final SQLLoader loader,
    final NamedParameterJdbcTemplate template) {
    this.loader = loader;
    this.template = template;
  }
}
